package com.bigprime.db.ddl.interfaces;

import com.bigprime.db.ddl.model.IndexBase;
import com.bigprime.db.ddl.model.Struct;

import java.util.List;

/**
 * @author lyw
 * @version 1.0
 */
public interface IIndex<T extends IndexBase> {
    /**
     * 获取索引结构信息
     * @return
     */
    List<Struct> indexStructs();

    /**
     * 转换索引对象集合
     * @param json
     * @return
     */
    List<T> convertIndexObjects(String json);


    /**
     * 获取索引信息
     * @param tableName
     * @return
     */
    List<T> getIndexes(String tableName);
}
