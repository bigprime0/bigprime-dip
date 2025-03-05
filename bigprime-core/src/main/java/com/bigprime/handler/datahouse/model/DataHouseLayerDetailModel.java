package com.bigprime.handler.datahouse.model;

import com.bigprime.common.base.BaseModel;
import com.bigprime.handler.database.model.DataDatabaseModel;
import com.bigprime.handler.datahouse.model.proxy.DataHouseLayerDetailModelProxy;
import com.easy.query.core.annotation.EntityProxy;
import com.easy.query.core.annotation.Navigate;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.enums.RelationTypeEnum;
import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@Table("data_house_layer_detail")
@EntityProxy
public class DataHouseLayerDetailModel extends BaseModel implements ProxyEntityAvailable<DataHouseLayerDetailModel , DataHouseLayerDetailModelProxy> {
    /**
     * 父级分层ID(ODS层级的ID)
     */
    private Long parentId;

    @Navigate(value = RelationTypeEnum.ManyToOne, selfProperty = "parentId")
    private DataHouseLayerModel parent;

    /**
     * 绑定的产品
     */
    private String product;

    /**
     * 绑定的数据库ID(数据源ID)
     */
    private Long databaseId;
    @Navigate(value = RelationTypeEnum.ManyToOne, selfProperty = "databaseId")
    private DataDatabaseModel database;

}
