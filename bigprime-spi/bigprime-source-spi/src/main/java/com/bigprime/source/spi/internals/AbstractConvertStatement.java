package com.bigprime.source.spi.internals;

import com.bigprime.source.spi.model.ConvertConfig;
import com.bigprime.source.spi.model.SafeAppendable;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lyw
 * @version 1.0
 */
public abstract class AbstractConvertStatement {
    protected final ConvertConfig config;

    protected AbstractConvertStatement(ConvertConfig config) {
        this.config = config;
    }


    public abstract String execute();

    protected String applyDatabaseAndTable() {
        String table = "";
        if (StringUtils.isNotEmpty(config.getDatabase())) {
            table = String.format("`%s`", config.getDatabase());
        }
        if (StringUtils.isNotEmpty(config.getSchema())) {
            if (StringUtils.isNotEmpty(table)) {
                table = String.format("%s.`%s`", table, config.getSchema());
            } else {
                table = String.format("`%s`", config.getSchema());
            }
        }
        if (StringUtils.isNotEmpty(table)) {
            table = String.format("%s.`%s`", table, config.getTableName());
        } else {
            table = String.format("`%s`", config.getTableName());
        }
        return table.replace("`", getKeywordSymbol());
    }

    protected void sqlClause(SafeAppendable builder, String keyword, List<String> parts, String open, String close, String conjunction) {
        if (parts != null && !parts.isEmpty()) {
            if (!builder.isEmpty()) {
                builder.append("\n");
            }
            builder.append(keyword);
            builder.append(" ");
            builder.append(open);
            String last = "________";
            for (int i = 0, n = parts.size(); i < n; i++) {
                String part = parts.get(i);
                if (part != null) {
                    if (i > 0 && !part.equals(getAnd()) && !part.equals(getOr()) && !last.equals(getAnd()) && !last.equals(getOr())) {
                        builder.append(conjunction);
                    }
                    builder.append(part);
                    last = part;
                } else {
                    if (i > 0) {
                        builder.append(conjunction);
                    }
                    builder.append(null);
                }
            }
            builder.append(close);
        }
    }

    protected void rowsStrategyClause(SafeAppendable builder, Integer offset, Integer limit) {
        if (limit != null && limit > 0) {
            builder.append("\nLIMIT ").append(String.valueOf(limit));
        }
        if (offset != null && offset > 0 && limit != null && limit > 0) {
            offset = (offset - 1) * limit;
            builder.append("\nOFFSET ").append(String.valueOf(offset));
        }
    }

    protected String getKeywordSymbol() {
        return "`";
    }

    protected String getAnd() {
        return ") \nAND (";
    }

    protected String getOr() {
        return ") \nOR (";
    }
}
