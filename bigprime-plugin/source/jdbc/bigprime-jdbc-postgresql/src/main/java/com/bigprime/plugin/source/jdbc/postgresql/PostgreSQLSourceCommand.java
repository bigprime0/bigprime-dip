package com.bigprime.plugin.source.jdbc.postgresql;

import com.bigprime.source.spi.interfaces.ISourceCommand;
import com.bigprime.source.spi.model.CommandModel;

/**
 * @author lyw
 * @version 1.0
 */
public class PostgreSQLSourceCommand implements ISourceCommand {

    @Override
    public CommandModel port() {
        return CommandModel.builder()
                .required(true)
                .defaultValue(5432)
                .build();
    }

    @Override
    public CommandModel database() {
        return CommandModel.builder()
                .required(true)
                .defaultValue("postgres")
                .build();
    }
}
