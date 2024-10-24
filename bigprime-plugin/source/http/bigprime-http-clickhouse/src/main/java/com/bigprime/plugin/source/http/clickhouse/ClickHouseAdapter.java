package com.bigprime.plugin.source.http.clickhouse;

import com.bigprime.source.spi.constant.HttpMethodType;
import com.bigprime.source.spi.interfaces.impl.HttpAdapter;
import com.bigprime.source.spi.internals.impl.HttpConnection;
import com.bigprime.source.spi.model.HttpClient;
import com.bigprime.source.spi.model.Response;
import com.bigprime.source.spi.model.SourceHttpConfig;
import com.bigprime.source.spi.model.Time;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import org.apache.commons.beanutils.BeanUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
public class ClickHouseAdapter
        extends HttpAdapter {
    public ClickHouseAdapter(HttpConnection httpConnection) {
        super(httpConnection);
    }

    @Override
    public Response handlerExecute(String content) {
        Time processorTime = new Time();
        processorTime.setStart(new Date().getTime());
        Response response = this.connection.getResponse();
        SourceHttpConfig sourceHttpConfig = new SourceHttpConfig();
        if (response.getIsConnected()) {
            List<String> headers = new ArrayList<>();
            List<Object> columns = new ArrayList<>();
            try {
                BeanUtils.copyProperties(sourceHttpConfig, this.connection.getSourceConfig());
                sourceHttpConfig.setAutoConnected(Boolean.FALSE);
                sourceHttpConfig.setRetry(0);
                sourceHttpConfig.setMethod(HttpMethodType.POST);
                sourceHttpConfig.setJsonBody(content + " FORMAT TabSeparatedWithNamesAndTypes");
                sourceHttpConfig.setMediaType(MediaType.parse("text/plain; charset=utf-8"));
                HttpConnection httpConnection = new HttpConnection(sourceHttpConfig, new Response());
                HttpClient httpClient = HttpClient.getInstance(sourceHttpConfig, httpConnection);
                String responseBody = httpClient.execute();
                checkArgument(!responseBody.contains("DB::Exception"), responseBody);
                String[] data = responseBody.split("\n");
                for (int i = 0; i < data.length; i++) {
                    if (i == 0) {
                        headers.addAll(Arrays.asList(data[i].split("\t")));
                    } else {
                        columns.add(Arrays.asList(data[i].split("\t")));
                    }
                }
                response.setIsSuccessful(Boolean.TRUE);
            } catch (Exception ex) {
                response.setIsSuccessful(Boolean.FALSE);
                response.setMessage(ex.getMessage());
            } finally {
                response.setHeaders(headers);
                response.setColumns(formatJson(headers, columns));
            }
        }
        processorTime.setEnd(new Date().getTime());
        response.setProcessor(processorTime);
        return response;
    }
}
