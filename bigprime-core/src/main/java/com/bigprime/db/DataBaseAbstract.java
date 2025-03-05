package com.bigprime.db;

import com.bigprime.handler.database.model.DataDatabaseModel;
import lombok.Data;

/**
 * @author lyw
 * @version 1.0
 */
@Data
public abstract class DataBaseAbstract {
    /**
     * 连接的数据库信息
     */
    private DataDatabaseModel dataBase;
}
