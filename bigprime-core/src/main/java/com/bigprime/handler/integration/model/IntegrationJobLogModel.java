package com.bigprime.handler.integration.model;

import com.bigprime.handler.integration.model.proxy.IntegrationJobLogModelProxy;
import com.easy.query.core.annotation.Column;
import com.easy.query.core.annotation.ColumnIgnore;
import com.easy.query.core.annotation.EntityProxy;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@EntityProxy
@Table("di_job_log")
public class IntegrationJobLogModel implements ProxyEntityAvailable<IntegrationJobLogModel , IntegrationJobLogModelProxy> {

    private Long jobDefinedId;
    private String engine;
    /**
     * 后台返回的任务标识，如果没有表示提交任务失败
     */
    @Column(primaryKey = true)
    private String jobId;

    private String jobName;

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
     * 任务状态
     */
    private String status;

    /**
     * 任务返回消息（包含异常）
     */
    private String message;

    private String logs;

    private Date startTime;

    private Date endTime;

    /**
     * 业务类型
     */
    private Integer type;

    private Long writeSourceId;
    private String writeSourceTable;

    @ColumnIgnore
    private List<Long> jobDefinedIds;
}
