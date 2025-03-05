package com.bigprime.vo.datahouse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@Schema(description = "数据源管理-明细")
public class DataSourceOptionsVO implements Serializable {
    @Schema(description = "label")
    String label;

    @Schema(description = "value")
    String value;

    @Schema(description = "类型")
    String ctg;

    @Schema(description = "url")
    String url;
}
