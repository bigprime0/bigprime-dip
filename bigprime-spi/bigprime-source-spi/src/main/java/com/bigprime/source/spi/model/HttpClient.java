package com.bigprime.source.spi.model;

import com.bigprime.source.spi.constant.HttpMethodType;
import com.bigprime.source.spi.internals.impl.HttpConnection;
import okhttp3.Response;
import okhttp3.*;
import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class HttpClient
{
    private static final int CONNECTION_TIME_OUT = 200000;
    private static final int SOCKET_TIME_OUT = 20000;
    private static final int MAX_IDLE_CONNECTIONS = 30;
    private static final long KEEP_ALLIVE_TIME = 60000L;
    private static volatile HttpClient httpClient;
    private final OkHttpClient okHttpClient;
    private final SourceHttpConfig configure;
    private final HttpConnection httpConnection;

    public HttpClient(SourceHttpConfig configure, HttpConnection httpConnection)
    {
        this.configure = configure;
        this.httpConnection = httpConnection;
        ConnectionPool connectionPool = new ConnectionPool(MAX_IDLE_CONNECTIONS, KEEP_ALLIVE_TIME, TimeUnit.MILLISECONDS);
        this.okHttpClient = new OkHttpClient().newBuilder()
                .readTimeout(SOCKET_TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(SOCKET_TIME_OUT, TimeUnit.MILLISECONDS)
                .connectionPool(connectionPool)
                .connectTimeout(CONNECTION_TIME_OUT, TimeUnit.MILLISECONDS)
                .build();
    }

    public static HttpClient getInstance(SourceHttpConfig configure, HttpConnection httpConnection)
    {
        httpClient = new HttpClient(configure, httpConnection);
        return httpClient;
    }

    public String execute()
    {
        switch (configure.getMethod()) {
            case GET:
                return this.get();
            case POST:
                if (configure.isDecoded()) {
                    return this.postEncoder();
                }
                return this.post();
            default:
                return null;
        }
    }

    private String get()
    {
        HttpUrl.Builder builder = HttpUrl.parse(httpConnection.formatJdbcUrl()).newBuilder();
        if (ObjectUtils.isNotEmpty(configure.getParams())) {
            for (String key : configure.getParams().keySet()) {
                builder.addQueryParameter(key, configure.getParams().get(key));
            }
        }
        Request request = new Request.Builder()
                .addHeader("Accept-Encoding", "identity")
                .url(builder.build().toString()).build();
        return execute(request);
    }

    private String post()
    {
        RequestBody requestBody = RequestBody.create(configure.getMediaType(), configure.getJsonBody());
        HttpUrl.Builder builder = HttpUrl.parse(httpConnection.formatJdbcUrl()).newBuilder();

        // Adding Path Parameters
        if (ObjectUtils.isNotEmpty(configure.getParams())) {
            for (String key : configure.getParams().keySet()) {
                builder.addQueryParameter(key, configure.getParams().get(key));
            }
        }

        Request request = new Request.Builder().post(requestBody)
                .addHeader("Accept-Encoding", "identity")
                .url(builder.build().toString()).build();
        return execute(request);
    }

    private String postEncoder()
    {
        AtomicReference<String> key = new AtomicReference<>();
        AtomicReference<String> value = new AtomicReference<>();
        configure.getParams().forEach((originKey, originValue) -> {
            key.set(originKey);
            value.set(originValue);
        });
        RequestBody body = new FormBody.Builder()
                .add(key.get(), value.get())
                .build();
        Request request = new Request.Builder()
                .url(httpConnection.formatJdbcUrl())
                .method(HttpMethodType.POST.name(), body)
                .build();
        return execute(request);
    }

    private String execute(Request request)
    {
        String responseBody = null;
        try {
            Response response = okHttpClient.newCall(request).execute();
            responseBody = response.body().string();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return responseBody;
    }
}
