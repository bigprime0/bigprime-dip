package com.bigprime.plugin.source.natived.redis;

import com.bigprime.source.spi.interfaces.ISourceCommand;
import com.bigprime.source.spi.model.CommandModel;

/**
 * @author lyw
 * @version 1.0
 */
public class RedisSourceCommand implements ISourceCommand {

    @Override
    public CommandModel port() {
        return CommandModel.builder()
                .required(true)
                .defaultValue(6379)
                .build();
    }

    @Override
    public CommandModel username() {
        return CommandModel.builder()
                .show(false)
                .build();
    }
}
