package com.bigprime.plugin.source.jdbc.oracle;

import com.bigprime.source.spi.interfaces.impl.JdbcAdapter;
import com.bigprime.source.spi.internals.impl.JdbcConnection;

public class OracleAdapter
        extends JdbcAdapter
{
    public OracleAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
