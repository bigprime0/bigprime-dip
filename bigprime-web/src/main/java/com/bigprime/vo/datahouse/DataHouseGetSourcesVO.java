package com.bigprime.vo.datahouse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@Schema(description = "数据仓库-数据库可用列表")
public class DataHouseGetSourcesVO implements Serializable {
    @Schema(description = "数据库ID")
    private Long id;

    @Schema(description = "数据库名称")
    private String name;

    @Schema(description = "数据库类型")
    private String product;
}
