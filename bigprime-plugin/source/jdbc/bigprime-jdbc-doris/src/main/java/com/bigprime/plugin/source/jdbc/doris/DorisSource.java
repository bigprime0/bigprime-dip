package com.bigprime.plugin.source.jdbc.doris;

import com.bigprime.source.spi.Source;
import com.bigprime.source.spi.interfaces.ISourceCommand;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DorisSource
        implements Source
{

    @Override
    public ISourceCommand sourceCommand() {
        return new DorisSourceCommand();
    }

    @Override
    public String driver() {
        return "com.mysql.cj.jdbc.Driver";
    }

    @Override
    public String connectType() {
        return "mysql";
    }
}
