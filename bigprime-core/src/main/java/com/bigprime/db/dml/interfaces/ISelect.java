package com.bigprime.db.dml.interfaces;

import com.bigprime.db.dml.model.SelectPageRequest;
import com.bigprime.db.dml.model.SelectPageResult;

import java.util.List;
import java.util.Map;

/**
 * @author lyw
 * @version 1.0
 */
public interface ISelect {
    /**
     * 根据表名获取数据
     * @param tableName
     * @param count 查询数据量
     * @return
     */
    List<Map<String, Object>> getDataByTableName(String tableName, int count);

    /**
     * 根据sql获取数据
     * @param sql
     * @param count 查询数据量
     * @return
     */
    List<Map<String, Object>> getDataBySql(String sql, int count);

    /**
     * 获取表分页信息
     * @param request
     * @return
     */
    SelectPageResult<Map<String, Object>> getDataByPage(SelectPageRequest request);
}
