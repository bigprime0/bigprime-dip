package com.bigprime.source.spi.interfaces;

import com.bigprime.parser.sql.SqlBase;

public interface IParser
{
    SqlBase getSqlBase();

    String getExecuteContext();
}
