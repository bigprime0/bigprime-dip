package com.bigprime.plugin.source.natived.zookeeper;

import com.bigprime.source.spi.interfaces.ISourceCommand;
import com.bigprime.source.spi.model.CommandModel;

/**
 * @author lyw
 * @version 1.0
 */
public class ZookeeperSourceCommand implements ISourceCommand {

    @Override
    public CommandModel port() {
        return CommandModel.builder()
                .required(false)
                .show(false)
                .build();
    }


    @Override
    public CommandModel database() {
        return CommandModel.builder()
                .show(false)
                .build();
    }


    @Override
    public CommandModel username() {
        return CommandModel.builder()
                .show(false)
                .build();
    }

    @Override
    public CommandModel password() {
        return CommandModel.builder()
                .show(false)
                .build();
    }
}
