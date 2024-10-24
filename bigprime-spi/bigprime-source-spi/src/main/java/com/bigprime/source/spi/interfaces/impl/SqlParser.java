package com.bigprime.source.spi.interfaces.impl;

import com.bigprime.parser.sql.SqlBase;
import com.bigprime.parser.sql.SqlBaseFormatter;
import com.bigprime.source.spi.interfaces.IParser;

public class SqlParser
        implements IParser
{
    private final String content;
    private SqlBaseFormatter formatter;

    public SqlParser(String content)
    {
        this.content = content;
        this.formatter = new SqlBaseFormatter(this.content);
    }

    @Override
    public SqlBase getSqlBase()
    {
        return this.formatter.getParseResult();
    }

    @Override
    public String getExecuteContext()
    {
        return null;
    }
}
