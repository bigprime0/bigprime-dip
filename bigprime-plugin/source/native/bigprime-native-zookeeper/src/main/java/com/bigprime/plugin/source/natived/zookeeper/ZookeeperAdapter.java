package com.bigprime.plugin.source.natived.zookeeper;

import com.bigprime.parser.sql.SqlBase;
import com.bigprime.source.spi.interfaces.impl.NativeAdapter;
import com.bigprime.source.spi.model.Response;
import com.bigprime.source.spi.model.Time;
import com.google.common.base.Preconditions;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang3.ObjectUtils;

import java.util.*;

public class ZookeeperAdapter
        extends NativeAdapter {
    protected ZookeeperConnection zookeeperConnection;
    private final ZookeeperParser parser;

    public ZookeeperAdapter(ZookeeperConnection zookeeperConnection, ZookeeperParser parser) {
        super(zookeeperConnection, parser);
        this.zookeeperConnection = zookeeperConnection;
        this.parser = parser;
    }

    @Override
    public Response handlerExecute(String content) {
        Time processorTime = new Time();
        processorTime.setStart(new Date().getTime());
        Response response = this.zookeeperConnection.getResponse();
        if (response.getIsConnected()) {
            List<String> headers = new ArrayList<>();
            Map<String, String> types = new HashMap<>();
            List<Object> columns = new ArrayList<>();
            try {
                SqlBase sqlBase = this.parser.getSqlBase();
                if (sqlBase.isSuccessful()) {
                    ZkClient client = this.zookeeperConnection.getClient();
                    if (ObjectUtils.isNotEmpty(this.parser.getSqlBase().getColumns())) {
                        headers.addAll(this.parser.getSqlBase().getColumns());
                        headers.forEach(o -> {
                            types.put(o, "String");
                        });
                    } else {
                        headers.add("*");
                        types.put("*", "String");
                    }

                    client.getChildren(this.parser.getExecuteContext())
                            .forEach(column -> columns.add(Collections.singletonList(column)));
                    response.setIsSuccessful(Boolean.TRUE);
                } else {
                    Preconditions.checkArgument(true, sqlBase.getMessage());
                }
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
