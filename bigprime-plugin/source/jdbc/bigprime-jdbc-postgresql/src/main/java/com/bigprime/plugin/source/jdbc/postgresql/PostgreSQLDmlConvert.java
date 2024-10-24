package com.bigprime.plugin.source.jdbc.postgresql;

import com.bigprime.source.spi.internals.AbstractConvertDmlStatement;
import com.bigprime.source.spi.model.ConvertDmlConfig;

/**
 * @author lyw
 * @version 1.0
 */
public class PostgreSQLDmlConvert extends AbstractConvertDmlStatement {
    public PostgreSQLDmlConvert(ConvertDmlConfig config) {
        super(config);
    }


    @Override
    protected String getKeywordSymbol() {
        return "\"";
    }
}
