package com.bigprime.plugin.source.natived.zookeeper;

import com.bigprime.source.spi.internals.AbstractConnection;
import com.bigprime.source.spi.model.SourceConfig;
import com.bigprime.source.spi.model.Response;
import org.I0Itec.zkclient.ZkClient;

public class ZookeeperConnection
        extends AbstractConnection
{
    private SourceConfig config;
    private Response response;
    private ZkClient client;

    public ZookeeperConnection(SourceConfig config, Response response)
    {
        super(config, response);
        this.config = config;
    }

    @Override
    protected String formatJdbcUrl()
    {
        return null;
    }

    @Override
    protected java.sql.Connection openConnection()
    {
        try {
            this.response = getResponse();
            this.client = new ZkClient(this.config.getHost(), 60000, 5000);
            response.setIsConnected(Boolean.TRUE);
        }
        catch (Exception ex) {
            response.setIsConnected(Boolean.FALSE);
            response.setMessage(ex.getMessage());
        }
        return null;
    }

    @Override
    public void destroy()
    {
        this.client.close();
    }

    public ZkClient getClient()
    {
        return client;
    }
}
