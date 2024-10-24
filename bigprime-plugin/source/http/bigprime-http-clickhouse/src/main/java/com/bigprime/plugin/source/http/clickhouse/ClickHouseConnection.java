package com.bigprime.plugin.source.http.clickhouse;

import com.bigprime.source.spi.internals.impl.HttpConnection;
import com.bigprime.source.spi.model.Response;
import com.bigprime.source.spi.model.SourceHttpConfig;

public class ClickHouseConnection
        extends HttpConnection {
    public ClickHouseConnection(SourceHttpConfig sourceHttpConfig, Response response) {
        super(sourceHttpConfig, response);
    }
}
