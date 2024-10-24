package com.bigprime.plugin.source.jdbc.doris;

import com.bigprime.source.spi.interfaces.ISourceCommand;
import com.bigprime.source.spi.model.CommandModel;

/**
 * @author lyw
 * @version 1.0
 */
public class DorisSourceCommand implements ISourceCommand {

    @Override
    public CommandModel port() {
        return CommandModel.builder()
                .required(true)
                .defaultValue(9093)
                .build();
    }

    @Override
    public CommandModel database() {
        return CommandModel.builder()
                .required(true)
                .build();
    }

    @Override
    public CommandModel username() {
        return CommandModel.builder()
                .required(true)
                .defaultValue("default")
                .build();
    }
}
