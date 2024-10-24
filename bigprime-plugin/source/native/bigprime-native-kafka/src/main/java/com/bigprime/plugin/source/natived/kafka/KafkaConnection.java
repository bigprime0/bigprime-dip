package com.bigprime.plugin.source.natived.kafka;

import com.bigprime.source.spi.internals.AbstractConnection;
import com.bigprime.source.spi.model.SourceConfig;
import com.bigprime.source.spi.model.Response;
import lombok.Getter;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;

import java.util.Properties;

public class KafkaConnection
        extends AbstractConnection
{
    private SourceConfig config;
    private Response response;

    @Getter
    private AdminClient client;

    public KafkaConnection(SourceConfig config, Response response)
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
            Properties properties = new Properties();
            properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, config.getHost());
            this.client = AdminClient.create(properties);
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
}
