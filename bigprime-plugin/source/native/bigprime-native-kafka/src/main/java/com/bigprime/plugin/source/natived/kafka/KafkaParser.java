package com.bigprime.plugin.source.natived.kafka;

import com.bigprime.source.spi.interfaces.impl.SqlParser;
import com.bigprime.parser.sql.SqlBase;

public class KafkaParser
        extends SqlParser
{
    public KafkaParser(String content)
    {
        super(content);
    }

    @Override
    public String getExecuteContext()
    {
        SqlBase sqlBase = this.getSqlBase();
        if (sqlBase.getToken().equalsIgnoreCase("SHOW")) {
            return sqlBase.getTable();
        }
        else if (sqlBase.getToken().equalsIgnoreCase("SELECT")) {
            return sqlBase.getTable();
        }
        return null;
    }
}
