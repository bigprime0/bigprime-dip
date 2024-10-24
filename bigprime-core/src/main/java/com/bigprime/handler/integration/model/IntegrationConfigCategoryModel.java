package com.bigprime.handler.integration.model;

import com.bigprime.handler.integration.model.proxy.IntegrationConfigCategoryModelProxy;
import com.easy.query.core.annotation.Column;
import com.easy.query.core.annotation.EntityProxy;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;

/**
 * 数据集成分类配置
 *
 * @author Phoneix
 * @version 1.0
 */
@Data
@Table("di_config_category")
@EntityProxy
public class IntegrationConfigCategoryModel implements ProxyEntityAvailable<IntegrationConfigCategoryModel, IntegrationConfigCategoryModelProxy> {

    @Column(primaryKey = true, generatedKey = true)
    private long id;

    private long pid;

    private String label;

    private String route;

    private String category;

    private int isAdd;

    private int isLeaf;
}
