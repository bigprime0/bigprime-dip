package com.bigprime.vo.datahouse;

import com.bigprime.db.ddl.model.Struct;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author lyw
 * @version 1.0
 */
@Data
public class DataHouseStructVO {
    @Schema(description = "数据库类型Id")
    private String id;

    @Schema(description = "表结构")
    private List<Struct> tableStruct;

    @Schema(description = "列结构")
    private List<Struct> columnsStruct;

    @Schema(description = "索引结构")
    private List<Struct> indexesStruct;
}
