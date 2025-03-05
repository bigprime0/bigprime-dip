package com.bigprime.plugin.manager.loader;

import cn.hutool.core.util.ServiceLoaderUtil;
import com.bigprime.source.spi.Source;
import com.bigprime.source.spi.interfaces.ISourceCommand;
import com.bigprime.source.spi.model.CommandModel;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @author lyw
 * @version 1.0
 */
public class SourceLoader {

    private ConcurrentMap<String, Map<String,List<CommandModel>>> pluginSupport = new ConcurrentHashMap<>();

    private List<String> supportDataWarehouses = new ArrayList<>();

    private ConcurrentMap<String, Source> sourceMap = new ConcurrentHashMap<>();

    private static class SourceLoaderHolder{
        private static final SourceLoader INSTANCE = new SourceLoader();
    }

    private SourceLoader(){
        load();
    }

    private void load(){
        List<Source> sources = ServiceLoaderUtil.loadList(Source.class);
        for (Source source : sources){
            String protocol = source.protocol().toString();
            String name = source.name();
            if(!pluginSupport.containsKey(protocol)){
                pluginSupport.put(protocol, new HashMap<>());
            }
            pluginSupport.get(protocol).put(name, getSourceCommand(source.sourceCommand()));

            String key = String.format("%s-%s", protocol, name);
            if(source.supportDataWarehouse()){
                supportDataWarehouses.add(key);
            }

            sourceMap.put(key, source);
        }
    }

    private List<CommandModel> getSourceCommand(ISourceCommand sourceCommand){
        List<CommandModel> commandModels = new ArrayList<>();

        if (sourceCommand == null) {
            return commandModels;
        }

        List<Function<ISourceCommand, CommandModel>> getters = Arrays.asList(
                ISourceCommand::host,
                ISourceCommand::port,
                ISourceCommand::username,
                ISourceCommand::password,
                ISourceCommand::schema,
                ISourceCommand::database,
                ISourceCommand::ssl,
                ISourceCommand::configures
        );

        List<String> keys = Arrays.asList(
                "host", "port", "username", "password", "schema", "database", "ssl", "configures"
        );

        for (int i = 0; i < getters.size(); i++) {
            CommandModel commandModel = getters.get(i).apply(sourceCommand);
            if (commandModel != null) {
                commandModel.setKey(keys.get(i));
                commandModels.add(commandModel);
            }
        }

        return commandModels;
    }

    public static SourceLoader getInstance(){
        return SourceLoaderHolder.INSTANCE;
    }

    public ConcurrentMap<String,Map<String,List<CommandModel>>> pluginSupport(){
        return pluginSupport;
    }
    public Boolean supportDataWarehouse(String protocol, String name){
        return supportDataWarehouses.contains(String.format("%s-%s", protocol, name));
    }

    public Source getSource(String protocol, String name){
        return sourceMap.get(String.format("%s-%s", protocol, name));
    }
}
