package com.bigprime.vo.datahouse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@Schema(description = "数据源管理-类型")
public class DataSourceTypesVO implements Serializable {
    @Schema(description = "value")
    String value;

    @Schema(description = "描述")
    String describe;
}
