package com.bigprime.plugin.source.jdbc.postgresql;

import com.bigprime.source.spi.Source;
import com.bigprime.source.spi.interfaces.ISourceCommand;
import com.bigprime.source.spi.model.ConvertDdlConfig;
import com.bigprime.source.spi.model.ConvertDmlConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostgreSQLSource
        implements Source {

    @Override
    public String versionStatement() {
        return "SELECT regexp_replace(version(), 'PostgreSQL (\\d+\\.\\d+).*', '\\1') AS version";
    }

    @Override
    public ISourceCommand sourceCommand() {
        return new PostgreSQLSourceCommand();
    }

    @Override
    public String convertStatement(ConvertDdlConfig config) {
        return new PostgreSQLDdlConvert(config).execute();
    }

    @Override
    public String convertStatement(ConvertDmlConfig config) {
        return new PostgreSQLDmlConvert(config).execute();
    }

    @Override
    public String driver() {
        return "org.postgresql.Driver";
    }

    @Override
    public String connectType() {
        return "postgresql";
    }
}
