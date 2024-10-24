package com.bigprime.source.spi.internals;

import com.bigprime.source.spi.model.Response;
import com.bigprime.source.spi.model.SourceConfig;
import com.bigprime.source.spi.model.Time;

import java.util.Date;

public abstract class AbstractConnection {
    private final SourceConfig sourceConfig;
    private final Response response;
    private Object connection;
    private String url;

    public AbstractConnection(SourceConfig sourceConfig, Response response) {
        this.sourceConfig = sourceConfig;
        this.response = response;
        Time connectionTime = new Time();
        connectionTime.setStart(new Date().getTime());
        this.connection = this.openConnection();
        this.url = this.formatJdbcUrl();
        connectionTime.setEnd(new Date().getTime());
        this.response.setConnection(connectionTime);
    }

    protected abstract String formatJdbcUrl();

    protected abstract java.sql.Connection openConnection();

    public Object getConnection() {
        return this.connection;
    }

    public Response getResponse() {
        return this.response;
    }

    public SourceConfig getSourceConfig() {
        return this.sourceConfig;
    }

    public String getUrl() {
        return this.url;
    }

    public abstract void destroy();
}
