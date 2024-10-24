package com.bigprime.query.datahouse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数据源请求参数")
public class DataSourceQuery {
    @Schema(description = "搜索")
    private String search;

    @Schema(description = "返回配置")
    private Boolean config = false;

    @Schema(description = "协议类型")
    private String protocol;

    @Schema(description = "有效")
    private Boolean active = true;
}
