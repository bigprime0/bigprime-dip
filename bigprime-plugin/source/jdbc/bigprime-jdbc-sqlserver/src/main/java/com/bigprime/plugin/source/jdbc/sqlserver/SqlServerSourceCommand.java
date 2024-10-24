package com.bigprime.plugin.source.jdbc.sqlserver;

import com.bigprime.source.spi.interfaces.ISourceCommand;
import com.bigprime.source.spi.model.CommandModel;
import com.bigprime.source.spi.model.CommandCustomModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lyw
 * @version 1.0
 */
public class SqlServerSourceCommand implements ISourceCommand {

    @Override
    public CommandModel port() {
        return CommandModel.builder()
                .required(true)
                .defaultValue(1433)
                .build();
    }

    @Override
    public CommandModel ssl() {
        return CommandModel.builder()
                .show(true)
                .build();
    }

    @Override
    public CommandModel configures() {
        List<CommandCustomModel> list = new ArrayList<>();
        list.add(CommandCustomModel.builder()
                .key("trustServerCertificate")
                .value("true")
                .build());
        return CommandModel.builder()
                .commandCustomModels(list)
                .build();
    }
}
