package com.bigprime.db.ddl.internals;

import com.bigprime.db.DataBaseAbstract;
import com.bigprime.db.ddl.interfaces.ITable;
import com.bigprime.db.ddl.model.TableBase;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author lyw
 * @version 1.0
 */
@Component
public abstract class TableAbstract<T extends TableBase> extends DataBaseAbstract implements ITable<T> {

    /**
     * 获取表全名
     * @param schemaName
     * @param tableName
     * @return
     */
    protected String getFullTableName(String schemaName, String tableName){
        if (Objects.isNull(schemaName) || schemaName.trim().isEmpty()) {
            return String.format("`%s`", tableName);
        }
        return String.format("`%s`.`%s`", schemaName, tableName);
    }
}
