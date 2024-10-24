package com.bigprime.source.spi.internals;

import com.bigprime.source.spi.constant.DmlOperatorType;
import com.bigprime.source.spi.model.ConvertDmlConfig;
import com.bigprime.source.spi.model.SafeAppendable;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lyw
 * @version 1.0
 */
public abstract class AbstractConvertDmlStatement extends AbstractConvertStatement {

    protected final ConvertDmlConfig config;

    public AbstractConvertDmlStatement(ConvertDmlConfig config) {
        super(config);
        this.config = config;
    }

    public String execute() {
        StringBuilder sb = new StringBuilder();
        SafeAppendable builder = new SafeAppendable(sb);
        switch (config.getType()) {
            case COUNT_STATEMENT:
                countStatementBuild(builder);
                break;
            case SELECT_STATEMENT:
                selectStatementBuild(builder);
                break;
            default:
                break;
        }
        return sb.toString();
    }

    protected void countStatementBuild(SafeAppendable builder) {
        sqlClause(builder, "SELECT", Arrays.asList("COUNT(1)"), "", "", ", ");
        sqlClause(builder, "FROM", Arrays.asList(applyDatabaseAndTable()), "", "", ", ");
        if (ObjectUtils.isNotEmpty(config.getWhere())) {
            sqlClause(builder, "WHERE", Arrays.asList(applyWhere()), "(", ")", " AND ");
        }
    }

    protected void selectStatementBuild(SafeAppendable builder) {
        sqlClause(builder, "SELECT", applySelectColumns(), "", "", ", ");
        sqlClause(builder, "FROM", Arrays.asList(applyDatabaseAndTable()), "", "", ", ");
        if (ObjectUtils.isNotEmpty(config.getWhere())) {
            sqlClause(builder, "WHERE", Arrays.asList(applyWhere()), "(", ")", " AND ");
        }
        if (ObjectUtils.isNotEmpty(config.getOrders())) {
            sqlClause(builder, "ORDER BY", applyOrderByColumns(), "", "", ", ");
        }
        rowsStrategyClause(builder, config.getOffset(), config.getLimit());
        builder.append(";");
    }

    protected List<String> applySelectColumns() {
        if (config.getColumns() == null || config.getColumns().isEmpty()) {
            return Arrays.asList("*");
        }
        return config.getColumns().stream()
                .map(item -> {
                    String value = String.format("`%s`", item.getColumn());
                    if (item.getExpression() != null) {
                        value = String.format("%s(%s)", item.getExpression(), value);
                    }
                    if (item.getAlias() != null) {
                        value = String.format("%s AS %s", value, item.getAlias());
                    }
                    return value.replace("`", getKeywordSymbol());
                }).collect(Collectors.toList());
    }

    protected List<String> applyOrderByColumns() {
        return config.getOrders().stream()
                .map(v -> {
                    String value = String.format("`%s` %s", v.getColumn(), v.getOrder());
                    if (StringUtils.isNotEmpty(v.getExpression())) {
                        value = String.format("%s(`%s`) %s", v.getExpression(), v.getColumn(), v.getOrder());
                    }
                    return value.replace("`", getKeywordSymbol());
                }).collect(Collectors.toList());
    }

    protected String applyWhere() {
        return config.getWhere()
                .stream()
                .map(column -> {
                    String value = column.getValue();
                    if (column.getOperator().equals(DmlOperatorType.LIKE) || column.getOperator().equals(DmlOperatorType.NLIKE)) {
                        value = String.format("%%%s%%", value);
                    } else if (column.getOperator().equals(DmlOperatorType.NULL) || column.getOperator().equals(DmlOperatorType.NNULL)) {
                        return String.format("`%s` %s", column.getColumn(), column.getOperator().getSymbol()).replace("`", getKeywordSymbol());
                    }
                    return String.format("`%s` %s '%s'", column.getColumn(), column.getOperator().getSymbol(), value).replace("`", getKeywordSymbol());
                }).collect(Collectors.joining());
    }

}