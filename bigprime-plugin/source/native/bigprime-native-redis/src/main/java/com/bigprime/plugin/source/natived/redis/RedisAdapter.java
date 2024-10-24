package com.bigprime.plugin.source.natived.redis;

import com.bigprime.source.spi.interfaces.impl.NativeAdapter;
import com.bigprime.source.spi.model.Response;
import com.bigprime.source.spi.model.Time;
import org.apache.commons.lang3.reflect.MethodUtils;
import redis.clients.jedis.Connection;
import redis.clients.jedis.Protocol;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RedisAdapter
        extends NativeAdapter {
    protected RedisConnection redisConnection;

    public RedisAdapter(RedisConnection redisConnection) {
        super(redisConnection);
        this.redisConnection = redisConnection;
    }

    @Override
    public Response handlerExecute(String content) {
        Time processorTime = new Time();
        processorTime.setStart(new Date().getTime());
        Response response = this.redisConnection.getResponse();
        if (response.getIsConnected()) {
            List<String> headers = new ArrayList<>();
            List<Object> columns = new ArrayList<>();
            try {
                if (content.equals("PING Version")) {
                    String info = this.redisConnection.getJedis().info("server");
                    headers.add("version");
                    columns.add(parseRedisVersion(info));
                } else {
                    String[] commands = content.split(" ");
                    Protocol.Command cmd = Protocol.Command.valueOf(commands[0].toUpperCase());
                    Method method = MethodUtils.getMatchingMethod(Connection.class, "sendCommand", Protocol.Command.class, String[].class);
                    method.setAccessible(true);
                    Connection client = this.redisConnection.getJedis().getClient();
                    List<String> cmdParam = new ArrayList<>();
                    for (int i = 0; i < commands.length; i++) {
                        if (i == 0) {
                            continue;
                        }
                        cmdParam.add(commands[i]);
                    }
                    String[] cmdParamArr = cmdParam.toArray(new String[]{});
                    method.invoke(client, cmd, cmdParamArr);
                    Object body = client.getOne();
                    headers.add(commands[1]);
                    if (body instanceof List) {
                        List<Object> bodySplit = ((List) body);
                        for (Object obj : bodySplit) {
                            columns.add(List.of(new String((byte[]) obj, StandardCharsets.UTF_8)));
                        }
                    } else {
                        columns.add(List.of(new String((byte[]) body, StandardCharsets.UTF_8)));
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

    private String parseRedisVersion(String infoOutput) {
        try {
            String[] lines = infoOutput.split("\n");
            for (String line : lines) {
                if (line.startsWith("redis_version:")) {
                    String[] parts = line.split(":");
                    if (parts.length > 1) {
                        return parts[1].trim();
                    }
                }
            }
        } catch (Exception ex) {
        }
        return "-";
    }
}
