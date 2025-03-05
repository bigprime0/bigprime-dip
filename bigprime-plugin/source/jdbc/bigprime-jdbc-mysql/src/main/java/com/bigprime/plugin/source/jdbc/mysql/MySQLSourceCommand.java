package com.bigprime.plugin.source.jdbc.mysql;

import com.bigprime.source.spi.interfaces.ISourceCommand;
import com.bigprime.source.spi.model.CommandCustomModel;
import com.bigprime.source.spi.model.CommandModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lyw
 * @version 1.0
 */
public class MySQLSourceCommand implements ISourceCommand {

    @Override
    public CommandModel port() {
        return CommandModel.builder()
                .required(true)
                .defaultValue(3306)
                .build();
    }

    @Override
    public CommandModel database() {
        return CommandModel.builder()
                .required(true)
                .build();
    }

    @Override
    public CommandModel ssl() {
        return CommandModel.builder()
                .show(true)
                .build();
    }

    @Override
    public CommandModel username() {
        return CommandModel.builder()
                .required(true)
                .build();
    }

    @Override
    public CommandModel password() {
        return CommandModel.builder()
                .required(true)
                .build();
    }

    @Override
    public CommandModel configures() {
        List<CommandCustomModel> list = new ArrayList<>();
        list.add(CommandCustomModel.builder()
                .key("useOldAliasMetadataBehavior")
                .value("true")
                .build());
        return CommandModel.builder()
                .commandCustomModels(list)
                .build();
    }
}
