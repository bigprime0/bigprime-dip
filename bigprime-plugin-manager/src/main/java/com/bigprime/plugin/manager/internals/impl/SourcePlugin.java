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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    public Response execute(String sql) {
        return handleExecute(sql);
    }

    @EnableAspect
    public List<TableModel> getTables() {
        Response response = handleExecute(DdlStatementType.GET_TABLE_STATEMENT);
        List<TableModel> tables = getModels(response, TableModel.class);
        return tables;
    }

    @EnableAspect
    public TableModel getTable(String tableName) {
        Response response = handleExecute(DdlStatementType.GET_TABLE_STATEMENT, tableName);
        TableModel model = getModel(response, TableModel.class);
        if (model != null) {
            model.setColumns(getColumns(model.getName()));
        }
        return model;
    }

    private List<ColumnModel> getColumns(String tableName) {
        Response response = handleExecute(DdlStatementType.GET_COLUMN_STATEMENT, tableName);
        return convertColumns(getModels(response, ColumnModel.class));
    }

    private List<ColumnModel> convertColumns(List<ColumnModel> models) {
        if (!models.isEmpty()) {
            models.forEach(model -> {
                if (model.getPrecision() == null) {
                    model.setPrecision(model.getMaximumLength());
                }
            });
        }
        return models;
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
