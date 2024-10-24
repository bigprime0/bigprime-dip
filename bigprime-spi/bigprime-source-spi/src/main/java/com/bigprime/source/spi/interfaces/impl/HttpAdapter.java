package com.bigprime.source.spi.interfaces.impl;

import com.bigprime.source.spi.interfaces.IAdapter;
import com.bigprime.source.spi.internals.AbstractConnection;
import com.bigprime.source.spi.internals.AbstractColumn;
import com.bigprime.source.spi.model.Response;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;

@Slf4j
public class HttpAdapter
        implements IAdapter
{
    protected AbstractConnection connection;

    public HttpAdapter(AbstractConnection connection)
    {
        this.connection = connection;
    }

    @Override
    public AbstractColumn handlerColumn(ResultSet resultSet) {
        return null;
    }

    @Override
    public Response handlerExecute(String content)
    {
        return null;
    }
}
