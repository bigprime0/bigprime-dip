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
@Schema(description = "数仓维护-绑定数据源")
public class DataHouseBindLayerSourceQuery {
    @Schema(description = "id(新增时为0)")
    private Long id;

    @Schema(description = "父级分层ID(ODS层级的ID)")
    private Long parentId;

    @Schema(description = "绑定的数据库ID(数据源ID)")
    private Long databaseId;

    @Schema(description = "数据库类型")
    private String product;
}
