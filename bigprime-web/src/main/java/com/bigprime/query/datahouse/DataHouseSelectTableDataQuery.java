package com.bigprime.query.datahouse;

import com.bigprime.db.dml.model.SelectPageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "数仓维护-查询数据")
public class DataHouseSelectTableDataQuery extends SelectPageRequest {
    @Schema(description = "数据库ID")
    private Long id;

    @Schema(description = "查询sql")
    private String sql;
}
