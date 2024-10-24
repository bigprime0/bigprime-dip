package com.bigprime.handler.database.model;

import com.bigprime.common.base.BaseModel;
import com.bigprime.handler.database.model.proxy.DataDatabaseModelProxy;
import com.easy.query.core.annotation.ColumnIgnore;
import com.easy.query.core.annotation.EntityProxy;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@Table("data_database")
@EntityProxy
public class DataDatabaseModel extends BaseModel implements ProxyEntityAvailable<DataDatabaseModel, DataDatabaseModelProxy> {
    /**
     * 产品
     */
    private String product;

    /**
     * 产品
     */
    @ColumnIgnore
    private String productType;

    /**
     * 源名称
     */
    private String name;

    /**
     * 描述
     */
    private String summary;

    /**
     * 产品源配置
     */
    private String config;

    /**
     * 是否有效
     */
    private Integer active;

}
