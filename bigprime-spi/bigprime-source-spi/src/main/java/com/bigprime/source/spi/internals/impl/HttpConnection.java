package com.bigprime.source.spi.internals.impl;

import com.bigprime.source.spi.internals.AbstractConnection;
import com.bigprime.source.spi.model.Response;
import com.bigprime.source.spi.model.SourceHttpConfig;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;

public class HttpConnection
        extends AbstractConnection
{
    private SourceHttpConfig sourceHttpConfig;
    private Response response;

    public HttpConnection(SourceHttpConfig sourceHttpConfig, Response response)
    {
        super(sourceHttpConfig, response);
    }

    public String formatJdbcUrl()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.sourceHttpConfig.getProtocol().toLowerCase());
        buffer.append("://");
        buffer.append(this.sourceHttpConfig.getHost());
        buffer.append(":");
        buffer.append(this.sourceHttpConfig.getPort());
        if (StringUtils.isNotEmpty(this.sourceHttpConfig.getPath())) {
            buffer.append("/");
            buffer.append(this.sourceHttpConfig.getPath());
        }
        return buffer.toString();
    }

    protected Connection openConnection()
    {
        try {
            this.sourceHttpConfig = (SourceHttpConfig) getSourceConfig();
            this.response = getResponse();
            response.setIsConnected(Boolean.TRUE);
        }
        catch (Exception ex) {
            response.setIsConnected(Boolean.FALSE);
            response.setMessage(ex.getMessage());
        }
        return null;
    }

    public void destroy()
    {
    }
}
