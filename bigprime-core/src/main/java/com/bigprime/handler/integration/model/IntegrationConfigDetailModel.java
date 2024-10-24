package com.bigprime.handler.integration.model;

import com.bigprime.common.base.BaseModel;
import com.bigprime.handler.integration.model.proxy.IntegrationConfigDetailModelProxy;
import com.easy.query.core.annotation.EntityProxy;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;

/**
 * 数据集成明细配置
 *
 * @version 1.0
 * @Auther Phoenix
 */
@Data
@Table("di_config_detail")
@EntityProxy
public class IntegrationConfigDetailModel extends BaseModel implements ProxyEntityAvailable<IntegrationConfigDetailModel, IntegrationConfigDetailModelProxy> {

    /**
     * 产品
     */
    private String product;

    /**
     * 配置项
     */
    private String category;

    /**
     * 配置子项
     */
    private String subCategory;

    /**
     * 配置项分组
     */
    private String configGroup;

    /**
     * 配置项(表单)，可自动生成
     */
    private String configCode;

    /**
     * 配置项
     */
    private String configKey;

    /**
     * 配置项标题
     */
    private String configTitle;

    /**
     * 适用版本（universal）
     */
    private String version;

    /**
     * 排序字段
     */
    private int seq;

    /**
     * 中文描述
     */
    private String cnDescription;

    /**
     * 英文描述
     */
    private String enDescription;

    /**
     * 配置项是否可见
     */
    private int isVisible;

    /**
     * 是否任务配置项
     */
    private int isProperty;

    /**
     * 是否开启配置（注意无值配置最终不参与任务构建）
     */
    private int isEnable;

    /**
     * 控件类型(textInput、textarea、switch、select、number、grid)
     */
    private String compType;

    /**
     * 控件配置，如下拉数据源
     */
    private String compConfig;

    /**
     * 默认值
     */
    private String defaultValue;

}
