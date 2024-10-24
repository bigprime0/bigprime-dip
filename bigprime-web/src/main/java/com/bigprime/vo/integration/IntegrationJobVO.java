package com.bigprime.vo.integration;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "同步任务提交返回参数")
public class IntegrationJobVO {
    @Schema(description = "返回状态")
    private String status;

    @Schema(description = "返回消息")
    private String message;
}
