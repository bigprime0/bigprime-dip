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
@Schema(description = "数仓维护-创建表")
public class DataHouseCreateTableQuery {

    @Schema(description = "绑定的数据库ID")
    private Long databaseId;

    @Schema(description = "表信息(json格式)")
    private String tableInfo;

    @Schema(description = "列集合信息(json格式)")
    private String columnsInfo;

    @Schema(description = "索引集合信息(json格式)")
    private String indexesInfo;
}
