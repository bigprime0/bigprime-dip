package com.bigprime.source.spi.interfaces.impl;

import com.bigprime.source.spi.interfaces.IAdapter;
import com.bigprime.source.spi.internals.AbstractConnection;
import com.bigprime.source.spi.internals.AbstractColumn;
import com.bigprime.source.spi.model.Response;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;

@Slf4j
public class NativeAdapter
        implements IAdapter
{
    protected AbstractConnection connection;

    @Getter
    private SqlParser parser;

    public NativeAdapter(AbstractConnection connection)
    {
        this.connection = connection;
    }

    public NativeAdapter(AbstractConnection connection, SqlParser parser)
    {
        this.connection = connection;
        this.parser = parser;
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
