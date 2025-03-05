package com.bigprime.plugin.source.jdbc.mysql;

import com.bigprime.source.spi.Source;
import com.bigprime.source.spi.interfaces.ISourceCommand;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MySQLSource
        implements Source {

    @Override
    public ISourceCommand sourceCommand() {
        return new MySQLSourceCommand();
    }

    @Override
    public Boolean supportDataWarehouse() {
        return true;
    }

    @Override
    public String driver() {
        return "com.mysql.jdbc.Driver";
    }

    @Override
    public String connectType() {
        return "mysql";
    }
}
