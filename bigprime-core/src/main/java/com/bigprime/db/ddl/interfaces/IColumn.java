package com.bigprime.db.ddl.interfaces;

import com.bigprime.db.ddl.model.ColumnBase;
import com.bigprime.db.ddl.model.Struct;

import java.util.List;

/**
 * @author lyw
 * @version 1.0
 */
public interface IColumn<T extends ColumnBase> {
    /**
     * 获取列结构信息
     * @return
     */
    List<Struct> columnStructs();

    /**
     * 转换索引对象集合
     * @param json
     * @return
     */
    List<T> convertColumnObjects(String json);


    /**
     * 获取列信息
     * @param tableName
     * @return
     */
    List<T> getColumns(String tableName);
}
