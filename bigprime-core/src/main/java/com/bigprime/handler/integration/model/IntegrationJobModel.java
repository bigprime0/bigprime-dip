package com.bigprime.handler.integration.model;

import com.bigprime.common.base.BaseModel;
import com.bigprime.handler.integration.model.proxy.IntegrationJobModelProxy;
import com.easy.query.core.annotation.EntityProxy;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;

@Data
@EntityProxy
@Table("di_job")
public class IntegrationJobModel extends BaseModel implements ProxyEntityAvailable<IntegrationJobModel , IntegrationJobModelProxy> {

    /**
     * 使用引擎产品
     */
    private String engine;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 提交的api地址
     */
    private String url;

    /**
     * 各配置项提交内容，在重新打开配置页后可以返值数据，结构示例（seatunnel）：
     * {
     * "env":{},
     * "source":{},
     * "transform":{}
     * }
     */
    private String formDatas;

    /**
     * 任务提交内容
     */
    private String content;

    /**
     * 业务类型
     */
    private Integer type;

    private String writeType;
    private Long writeSourceId;
    private String writeSourceDatabase;
    private String writeSourceTable;

    private String readerType;
    private Long readerSourceId;
    private String readerSourceDatabase;
    private String readerSourceTable;

    private String analyzeSql;
}
