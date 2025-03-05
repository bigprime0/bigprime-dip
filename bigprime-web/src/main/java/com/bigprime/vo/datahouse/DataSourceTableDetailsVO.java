package com.bigprime.vo.datahouse;

import com.bigprime.db.ddl.model.TableBase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * @author lyw
 * @version 1.0
 */
@Data
@Schema(description = "表对应的详情")
@AllArgsConstructor
public class DataSourceTableDetailsVO {

    @Schema(description = "表信息")
    private TableBase tableInfo;
}
