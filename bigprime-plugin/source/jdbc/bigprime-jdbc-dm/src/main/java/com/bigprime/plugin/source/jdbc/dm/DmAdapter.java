package com.bigprime.plugin.source.jdbc.dm;

import com.bigprime.source.spi.interfaces.impl.JdbcAdapter;
import com.bigprime.source.spi.internals.impl.JdbcConnection;

/**
 * @author lyw
 * @version 1.0
 */
public class DmAdapter extends JdbcAdapter {
    public DmAdapter(JdbcConnection connection) {
        super(connection);
    }
}
