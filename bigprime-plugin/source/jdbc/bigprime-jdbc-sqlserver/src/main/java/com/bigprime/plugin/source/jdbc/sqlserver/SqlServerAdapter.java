package com.bigprime.plugin.source.jdbc.sqlserver;

import com.bigprime.source.spi.interfaces.impl.JdbcAdapter;
import com.bigprime.source.spi.internals.impl.JdbcConnection;

public class SqlServerAdapter
        extends JdbcAdapter
{
    public SqlServerAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
