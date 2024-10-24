package com.bigprime.plugin.source.natived.zookeeper;

import com.bigprime.source.spi.interfaces.impl.SqlParser;
import com.bigprime.parser.sql.SqlBase;
import org.apache.commons.lang3.StringUtils;

public class ZookeeperParser
        extends SqlParser
{
    public ZookeeperParser(String content)
    {
        super(content);
    }

    @Override
    public String getExecuteContext()
    {
        SqlBase sqlBase = this.getSqlBase();
        if (sqlBase.getToken().equalsIgnoreCase("SHOW")) {
            if (StringUtils.isEmpty(sqlBase.getTable())) {
                return ZookeeperPathConvert.start;
            }
            return ZookeeperPathConvert.toPath(sqlBase.getTable());
        }
        else if (sqlBase.getToken().equalsIgnoreCase("SELECT")) {
            return ZookeeperPathConvert.toPath(sqlBase.getTable());
        }
        return null;
    }
}
