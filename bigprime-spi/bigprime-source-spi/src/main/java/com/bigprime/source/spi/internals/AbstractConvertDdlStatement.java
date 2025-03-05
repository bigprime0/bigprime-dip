package com.bigprime.source.spi.internals;

import cn.hutool.core.util.StrUtil;
import com.bigprime.source.spi.constant.StatementTemplateType;
import com.bigprime.source.spi.model.ConvertDdlConfig;
import com.bigprime.source.spi.model.SafeAppendable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lyw
 * @version 1.0
 */
public abstract class AbstractConvertDdlStatement extends AbstractConvertStatement {

    protected final ConvertDdlConfig config;

    protected AbstractConvertDdlStatement(ConvertDdlConfig config) {
        super(config);
        this.config = config;
    }

    public String execute() {
        StringBuilder sb = new StringBuilder();
        SafeAppendable builder = new SafeAppendable(sb);
        switch (config.getType()) {
            case GET_DB_STATEMENT:
                getDbStatementBuild(builder);
                break;
            case GET_TABLE_STATEMENT:
                getTableStatementBuild(builder);
                break;
            case GET_COLUMN_STATEMENT:
                getColumnStatementBuild(builder);
                break;
            case GET_INDEX_STATEMENT:
                getIndexStatementBuild(builder);
                break;
            case GET_VIEW_STATEMENT:
                getViewStatementBuild(builder);
                break;
            case GET_FUNCTION_STATEMENT:
                getFunctionStatementBuild(builder);
                break;
            case CREATE_TABLE_STATEMENT:
                createTableStatementBuild(builder);
                break;
            case CREATE_COLUMN_STATEMENT:
                createColumnStatementBuild(builder);
                break;
            case CREATE_VIEW_STATEMENT:
                createViewStatementBuild(builder);
                break;
            case CREATE_FUNCTION_STATEMENT:
                createFunctionStatementBuild(builder);
                break;
            case ALTER_TABLE_STATEMENT:
                alterTableStatementBuild(builder);
                break;
            case ALTER_COLUMN_STATEMENT:
                alterColumnStatementBuild(builder);
                break;
            case ALTER_VIEW_STATEMENT:
                alterViewStatementBuild(builder);
                break;
            case ALTER_FUNCTION_STATEMENT:
                alterFunctionStatementBuild(builder);
                break;
            case DROP_COLUMN_STATEMENT:
                dropColumnStatementBuild(builder);
                break;
            case DROP_TABLE_STATEMENT:
                dropTableStatementBuild(builder);
                break;
            case DROP_VIEW_STATEMENT:
                dropViewStatementBuild(builder);
                break;
            case DROP_FUNCTION_STATEMENT:
                dropFunctionStatementBuild(builder);
                break;
            case SHOW_CREATE_TABLE_STATEMENT:
                showCreateTableStatementBuild(builder);
                break;
            default:
                break;
        }
        return sb.toString();
    }

    protected void getDbStatementBuild(SafeAppendable builder) {
        builder.append(StatementTemplateType.DB_STATEMENT.getDefault());
    }

    protected void getTableStatementBuild(SafeAppendable builder) {
        builder.append(StatementTemplateType.TABLE_STATEMENT.getDefault());
        getStatementWhere(builder, " TABLE_TYPE = 'BASE TABLE'");

    }

    protected void getColumnStatementBuild(SafeAppendable builder) {
        builder.append(StatementTemplateType.COLUMN_STATEMENT.getDefault());
        getStatementWhere(builder, "");
    }

    protected void getIndexStatementBuild(SafeAppendable builder) {
        builder.append(StatementTemplateType.INDEX_STATEMENT.getDefault());
        getStatementWhere(builder, "");
    }

    protected void getViewStatementBuild(SafeAppendable builder) {
        builder.append(StatementTemplateType.VIEW_STATEMENT.getDefault());
        getStatementWhere(builder, "");
    }

    protected void getFunctionStatementBuild(SafeAppendable builder) {
        builder.append(StatementTemplateType.FUNCTION_STATEMENT.getDefault());
        if (StrUtil.isNotBlank(config.getDatabase())) {
            builder.append(String.format(" AND ROUTINE_SCHEMA = '%s'", config.getDatabase()));
        }
        if (StrUtil.isNotBlank(config.getTableName())) {
            builder.append(String.format(" AND ROUTINE_NAME = '%s'", config.getTableName()));
        }
        builder.append(";");
    }

    protected void createTableStatementBuild(SafeAppendable builder) {
        sqlClause(builder, "CREATE TABLE", Arrays.asList(applyDatabaseAndTable()), "", "", "");
        sqlClause(builder, "", applyColumns(), "(\n", "\n)", ",\n");
        if (StrUtil.isNotBlank(config.getTableComment())) {
            sqlClause(builder, "COMMENT", Arrays.asList(config.getTableComment()), " = '", "'", ",");
        }
        builder.append(";");
    }

    protected void createColumnStatementBuild(SafeAppendable builder) {
        sqlClause(builder, "ALTER TABLE", Arrays.asList(applyDatabaseAndTable()), "", "", "");
        sqlClause(builder, "ADD COLUMN", applyColumns(), "(", ")", ",\n");
        builder.append(";");
    }

    protected void createViewStatementBuild(SafeAppendable builder) {
        builder.append(String.format("CREATE VIEW %s as \n %s;", config.getViewModel().getViewName(), config.getViewModel().getViewDefinition()));
    }

    protected void createFunctionStatementBuild(SafeAppendable builder) {

    }

    protected void alterTableStatementBuild(SafeAppendable builder) {
        sqlClause(builder, "ALTER TABLE", Arrays.asList(applyDatabaseAndTable()), "", "", "");
        sqlClause(builder, "COMMENT", Arrays.asList(config.getTableComment()), " = '", "'", ",");
        builder.append(";");
    }

    protected void alterColumnStatementBuild(SafeAppendable builder) {
        sqlClause(builder, "ALTER TABLE", Arrays.asList(applyDatabaseAndTable()), "", "", "");
        sqlClause(builder, "MODIFY", applyColumns(), "", "", ",\n");
        builder.append(";");
    }

    protected void alterViewStatementBuild(SafeAppendable builder) {
        builder.append(String.format("DROP VIEW IF EXISTS %s;\n", config.getViewModel().getViewName()));
        builder.append(String.format("CREATE VIEW %s as \n %s;", config.getViewModel().getViewName(), config.getViewModel().getViewDefinition()));
    }

    protected void alterFunctionStatementBuild(SafeAppendable builder) {
    }

    protected void dropTableStatementBuild(SafeAppendable builder) {
        sqlClause(builder, "DROP TABLE", Arrays.asList(applyDatabaseAndTable()), "", "", "");
        builder.append(";");
    }

    protected void dropColumnStatementBuild(SafeAppendable builder) {
        sqlClause(builder, "ALTER TABLE", Arrays.asList(applyDatabaseAndTable()), "", "", "");
        sqlClause(builder, "DROP COLUMN", applyColumns(), "", "", ",\n");
        builder.append(";");
    }

    protected void dropViewStatementBuild(SafeAppendable builder) {
        builder.append(String.format("DROP VIEW IF EXISTS %s;\n", config.getViewModel().getViewName()));
    }

    protected void dropFunctionStatementBuild(SafeAppendable builder) {
    }

    protected void showCreateTableStatementBuild(SafeAppendable builder) {
        sqlClause(builder, "SHOW CREATE TABLE", Arrays.asList(applyDatabaseAndTable()), "", "", "");
        builder.append(";");
    }

    protected List<String> applyColumns() {
        return config.getColumns().stream()
                .map(item -> {
                    String value = String.format("\t`%s`", item.getColumn());

                    if(StrUtil.isNotBlank(item.getDataType())){
                        value = String.format("%s %s", value, item.getDataType());
                    }

                    if (item.getPrecision() != null && item.getPrecision() > 0) {
                        if (item.getScale() != null && item.getScale() > 0) {
                            value = String.format("%s(%s,%s)", value, item.getPrecision(), item.getScale());
                        } else {
                            value = String.format("%s(%s)", value, item.getPrecision());
                        }
                    }

                    if (item.getIsNullable() != null && !item.getIsNullable()) {
                        value = String.format("%s NOT NULL", value);
                    }

                    if (item.getIsKey() != null && item.getIsKey()) {
                        value = String.format("%s PRIMARY KEY", value);
                    }

                    if (StrUtil.isNotBlank(item.getDefaultValue())) {
                        value = String.format("%s DEFAULT '%s'", value, item.getDefaultValue());
                    }

                    if (StrUtil.isNotBlank(item.getComment())) {
                        value = String.format("%s COMMENT '%s'", value, item.getComment());
                    }
                    return value.replace("`", getKeywordSymbol());
                }).collect(Collectors.toList());
    }

    private void getStatementWhere(SafeAppendable builder, String append) {
        if (StrUtil.isNotBlank(config.getDatabase())) {
            builder.append(String.format(" WHERE TABLE_SCHEMA = '%s'", config.getDatabase()));
            if (StrUtil.isNotBlank(config.getTableName())) {
                builder.append(String.format(" AND TABLE_NAME = '%s'", config.getTableName()));
            }
            if(StrUtil.isNotBlank(append)){
                builder.append(String.format(" AND %s", append));
            }
        } else if (StrUtil.isNotBlank(config.getTableName())) {
            builder.append(String.format(" WHERE TABLE_NAME = '%s'", config.getTableName()));
            if(StrUtil.isNotBlank(append)){
                builder.append(String.format(" AND %s", append));
            }
        } else if(StrUtil.isNotBlank(append)){
            builder.append(String.format(" WHERE %s", append));
        }
    }
}
