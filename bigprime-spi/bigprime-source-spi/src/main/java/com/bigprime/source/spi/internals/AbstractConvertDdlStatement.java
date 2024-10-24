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
