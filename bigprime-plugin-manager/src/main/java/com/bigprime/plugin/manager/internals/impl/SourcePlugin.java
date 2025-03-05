package com.bigprime.plugin.manager.internals.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.bigprime.plugin.manager.annotation.EnableAspect;
import com.bigprime.plugin.manager.internals.AbstractPlugin;
import com.bigprime.plugin.manager.loader.SourceLoader;
import com.bigprime.plugin.manager.utils.ConvertUtils;
import com.bigprime.source.spi.Source;
import com.bigprime.source.spi.constant.DdlStatementType;
import com.bigprime.source.spi.constant.DmlStatementType;
import com.bigprime.source.spi.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author lyw
 * @version 1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SourcePlugin extends AbstractPlugin<SourcePlugin, Source> {
    private SourceConfig sourceConfig;

    @Override
    public Source plugin() {
        return SourceLoader.getInstance().getSource(sourceConfig.getProtocol(), sourceConfig.getType());
    }

    public Boolean supportDataWarehouse() {
        return SourceLoader.getInstance().supportDataWarehouse(sourceConfig.getProtocol(), sourceConfig.getType());
    }

    public ConcurrentMap<String, Map<String, List<CommandModel>>> getPluginSupport() {
        return SourceLoader.getInstance().pluginSupport();
    }

    public boolean testConnection(SourceConfig config) {
        this.sourceConfig = config;
        this.plugin = plugin();
        boolean result = false;
        try {
            String url = plugin.connect(config);
            Response response = plugin.execute(plugin.versionStatement());
            plugin.destroy();
            result = response.getIsConnected();
            if (result) {
                config.setUrl(url);
                config.setVersion("-");
                config.setJdbcDriver(plugin.driver());
                config.setJdbcType(plugin.connectType());
                if (config.getConfigures().isEmpty()) {
                    CommandModel configures = plugin.sourceCommand().configures();
                    if (configures != null && !configures.getCommandCustomModels().isEmpty()) {
                        config.setConfigures(configures.getCommandCustomModels().stream()
                                .collect(Collectors.toMap(
                                        CommandCustomModel::getKey,
                                        CommandCustomModel::getValue,
                                        (v1, v2) -> v1
                                )));
                    }
                }

                if (response.getColumns().size() > 0) {
                    try {
                        config.setVersion(response.getColumns().get(0).getStr("version"));
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {
        }
        return result;
    }

    @EnableAspect
    public Response execute(ConvertDmlConfig dmlConfig) {
        return handleExecute(dmlConfig);
    }

    @EnableAspect
    public Response execute(ConvertDmlConfig dmlConfig, Long cryptoId, List<String> cryptoColumns) {
        Response response = handleExecute(dmlConfig);
        return response;
    }

    @EnableAspect
    public Response execute(ConvertDmlConfig dmlConfig, Map<String, Long> securityMaps) {
        Response response = handleExecute(dmlConfig);
        return response;
    }

    @EnableAspect
    public Response execute(String sql) {
        return handleExecute(sql);
    }

    @EnableAspect
    public List<DbModel> getDatabases() {
        Response response = handleExecute(DdlStatementType.GET_DB_STATEMENT);
        return getModels(response, DbModel.class);
    }

    @EnableAspect
    public List<TableModel> getTables(boolean isCascade) {
        Response response = handleExecute(DdlStatementType.GET_TABLE_STATEMENT);
        List<TableModel> tables = getModels(response, TableModel.class);
        if (tables != null && tables.size() > 0 && isCascade) {
            Map<String, List<ColumnModel>> columnMaps = new HashMap<>();
            Map<String, List<IndexModel>> indexMaps = new HashMap<>();
            List<ColumnModel> columns = getColumns();
            if (columns != null && columns.size() > 0) {
                columnMaps = columns.stream().collect(Collectors.groupingBy(ColumnModel::getTableName));
            }
            List<IndexModel> indexes = getIndexes();
            if (indexes != null && indexes.size() > 0) {
                indexMaps = indexes.stream().collect(Collectors.groupingBy(IndexModel::getTableName));
            }
            for (TableModel table : tables) {
                if (columnMaps.containsKey(table.getName())) {
                    table.setColumns(columnMaps.get(table.getName()));
                }
                if (indexMaps.containsKey(table.getName())) {
                    table.setIndexes(indexMaps.get(table.getName()));
                }
            }
        }
        return tables;
    }

    @EnableAspect
    public TableModel getTable(String tableName) {
        Response response = handleExecute(DdlStatementType.GET_TABLE_STATEMENT, tableName);
        TableModel model = getModel(response, TableModel.class);
        if (model != null) {
            model.setColumns(getColumns(model.getName()));
            model.setIndexes(getIndexes(model.getName()));
        }
        return model;
    }

    @EnableAspect
    public List<ViewModel> getViews() {
        Response response = handleExecute(DdlStatementType.GET_VIEW_STATEMENT);
        return getModels(response, ViewModel.class);
    }

    @EnableAspect
    public ViewModel getView(String viewName) {
        Response response = handleExecute(DdlStatementType.GET_VIEW_STATEMENT, viewName);
        return getModel(response, ViewModel.class);
    }

    @EnableAspect
    public List<FunctionModel> getFunctions() {
        Response response = handleExecute(DdlStatementType.GET_FUNCTION_STATEMENT);
        return getModels(response, FunctionModel.class);
    }

    @EnableAspect
    public FunctionModel getFunction(String functionName) {
        Response response = handleExecute(DdlStatementType.GET_FUNCTION_STATEMENT, functionName);
        return getModel(response, FunctionModel.class);
    }

    @EnableAspect
    public String getCreateStatement(String tableName) {
        Response response = handleExecute(DdlStatementType.SHOW_CREATE_TABLE_STATEMENT, tableName);
        if(!response.getColumns().isEmpty()){
            JSONObject applyResponse = response.getColumns().get(0);
            Set<String> keys = applyResponse.keySet();
            for (String key : keys){
                String value = applyResponse.getStr(key);
                if(value.toLowerCase().indexOf("create") != - 1){
                    return value;
                }
            }
        }
        return "";
    }

    @EnableAspect
    public Response createTable(TableModel model) {
        return handleExecute(DdlStatementType.CREATE_TABLE_STATEMENT, model.getName(), model.getComment(), model.getColumns());
    }

    @EnableAspect
    public Response alterTable(TableModel model) {
        return handleExecute(DdlStatementType.ALTER_TABLE_STATEMENT, model.getName(), model.getComment());
    }

    @EnableAspect
    public Response dropTable(String tableName) {
        return handleExecute(DdlStatementType.DROP_TABLE_STATEMENT, tableName);
    }

    @EnableAspect
    public Response createColumn(String tableName, ColumnModel columnModel) {
        return handleExecute(DdlStatementType.CREATE_COLUMN_STATEMENT, tableName, Arrays.asList(columnModel));
    }

    @EnableAspect
    public Response alterColumn(String tableName, ColumnModel columnModel) {
        return handleExecute(DdlStatementType.ALTER_COLUMN_STATEMENT, tableName, Arrays.asList(columnModel));
    }

    @EnableAspect
    public Response dropColumn(String tableName, String columnName) {
        return handleExecute(DdlStatementType.DROP_COLUMN_STATEMENT, tableName, Arrays.asList(ColumnModel.builder().column(columnName).build()));
    }


    private List<ColumnModel> getColumns() {
        Response response = handleExecute(DdlStatementType.GET_COLUMN_STATEMENT);
        return convertColumns(getModels(response, ColumnModel.class));
    }

    private List<ColumnModel> getColumns(String tableName) {
        Response response = handleExecute(DdlStatementType.GET_COLUMN_STATEMENT, tableName);
        return convertColumns(getModels(response, ColumnModel.class));
    }

    private List<ColumnModel> convertColumns(List<ColumnModel> models) {
        if(!models.isEmpty()){
            models.forEach(model -> {
                if(model.getPrecision() == null){
                    model.setPrecision(model.getMaximumLength());
                }
            });
        }
        return models;
    }

    private List<IndexModel> getIndexes() {
        return getIndexes(null);
    }

    private List<IndexModel> getIndexes(String tableName) {
        Response response = handleExecute(DdlStatementType.GET_INDEX_STATEMENT, tableName);
        return convertIndexes(response);
    }

    private List<IndexModel> convertIndexes(Response response) {
        List<IndexModel> indexes = getModels(response, IndexModel.class);
        if (indexes == null) {
            indexes = new ArrayList<>();
        } else {
            return indexes.stream()
                    .collect(Collectors.groupingBy(
                            o -> Arrays.asList(o.getTableName(), o.getIndexName()),
                            Collectors.collectingAndThen(
                                    Collectors.toList(),
                                    (list) -> {
                                        list.sort(Comparator.comparing(IndexModel::getSeqIndex, Comparator.nullsLast(Long::compareTo)));

                                        String newColumnName = list.stream()
                                                .map(IndexModel::getColumnName)
                                                .collect(Collectors.joining(", "));

                                        return IndexModel.builder()
                                                .tableName(list.get(0).getTableName())
                                                .indexName(list.get(0).getIndexName())
                                                .columnName(newColumnName)
                                                .indexType(list.get(0).getIndexType())
                                                .indexComment(list.get(0).getIndexComment())
                                                .nonUnique(list.get(0).getNonUnique())
                                                .seqIndex(list.get(0).getSeqIndex())
                                                .property(list.get(0).getProperty())
                                                .build();
                                    }
                            )
                    )).values().stream().collect(Collectors.toList());
        }
        return indexes;
    }

    private Response handleExecute(String sql) {
        if (StrUtil.isBlank(sql)) {
            return Response.builder()
                    .columns(Arrays.asList())
                    .isSuccessful(Boolean.FALSE)
                    .message("sql is empty").build();
        }
        plugin.connect(sourceConfig);
        Response response = plugin.execute(sql);
        response.setContent(sql);
        plugin.destroy();
        return response;
    }

    private Response handleExecute(ConvertDmlConfig dmlConfig) {
        Long count = 0L;
        Boolean isCount = Boolean.FALSE;
        if (dmlConfig.getType() == DmlStatementType.SELECT_STATEMENT
                && BeanUtil.isNotEmpty(dmlConfig.getLimit()) && dmlConfig.getLimit() > 0
                && BeanUtil.isNotEmpty(dmlConfig.getOffset()) && dmlConfig.getOffset() > 0) {
            isCount = Boolean.TRUE;
            dmlConfig.setType(DmlStatementType.COUNT_STATEMENT);
            Response response = handleExecute(plugin.convertStatement(dmlConfig));
            count = getFirstValue(response, Long.class);
            dmlConfig.setType(DmlStatementType.SELECT_STATEMENT);
        }

        Response response = handleExecute(plugin.convertStatement(dmlConfig));

        if (isCount) {
            response.setTotalRecords(count);
        } else if (BeanUtil.isNotEmpty(response.getColumns())) {
            response.setTotalRecords(Long.valueOf(response.getColumns().size()));
        } else {
            response.setTotalRecords(0L);
        }
        return response;
    }

    private <T> T getFirstValue(Response response, Class<T> clazz) {
        if (BeanUtil.isNotEmpty(response.getColumns())) {
            JSONObject applyResponse = response.getColumns().get(0);
            Set<String> keys = applyResponse.keySet();
            String firstKey = keys.iterator().next();
            return applyResponse.get(firstKey, clazz);
        }
        return null;
    }

    private Response handleExecute(DdlStatementType type) {
        return handleExecute(type, null, null, null);
    }

    private Response handleExecute(DdlStatementType type, String tableName) {
        return handleExecute(type, tableName, null, null);
    }

    private Response handleExecute(DdlStatementType type, String tableName, String tableComment) {
        return handleExecute(type, tableName, tableComment, null);
    }

    private Response handleExecute(DdlStatementType type, String tableName, List<ColumnModel> columns) {
        return handleExecute(type, tableName, null, columns);
    }

    private Response handleExecute(DdlStatementType type, String tableName, String tableComment, List<ColumnModel> columns) {
        String sql = plugin.convertStatement(ConvertDdlConfig.builder()
                .type(type)
                .schema(sourceConfig.getSchema())
                .database(sourceConfig.getDatabase())
                .tableName(tableName)
                .tableComment(tableComment)
                .columns(columns)
                .build());
        return handleExecute(sql);
    }

    private <T> List<T> getModels(Response response, Class<T> clazz) {
        return ConvertUtils.getModels(response.getColumns(), clazz);
    }

    private <T> T getModel(Response response, Class<T> clazz) {
        List<T> models = ConvertUtils.getModels(response.getColumns(), clazz);
        if (models.size() > 0) {
            return models.get(0);
        }
        return null;
    }
}
