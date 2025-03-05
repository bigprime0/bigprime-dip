package com.bigprime.db.ddl.interfaces;

import com.bigprime.db.ddl.model.Struct;
import com.bigprime.db.ddl.model.TableBase;

import java.util.List;

/**
 * @author lyw
 * @version 1.0
 */
public interface ITable<T extends TableBase> {

    /**
     * 获取表结构信息
     * @return
     */
    List<Struct> tableStructs();

    /**
     * 转换表对象
     * @param json json信息
     * @return
     */
    T convertTableObject(String json);


    /**
     * 获取表信息
     * @param tableName
     * @return
     */
    T getTable(String tableName);

    /**
     * 获取表列表信息
     * @return
     */
    List<T> getTables();


    /**
     * 创建表
     * @param table
     * @return
     */
    boolean createTable(T table);

    /**
     * 保存表
     * @param table
     * @return
     */
    boolean saveTable(T table);

}
