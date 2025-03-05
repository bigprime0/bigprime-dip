package com.bigprime.handler.datahouse.model;

import com.bigprime.common.base.BaseModel;
import com.bigprime.handler.datahouse.model.proxy.DataHouseLayerModelProxy;
import com.easy.query.core.annotation.EntityProxy;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@Table("data_house_layer")
@EntityProxy
public class DataHouseLayerModel extends BaseModel implements ProxyEntityAvailable<DataHouseLayerModel, DataHouseLayerModelProxy> {
    /**
     * 分层名称
     */
    private String name;

    /**
     * 分层中文名称
     */
    private String cnName;

    /**
     * 分层描述
     */
    private String note;

    /**
     * 表名前缀
     */
    private String tablePrefix;
}
