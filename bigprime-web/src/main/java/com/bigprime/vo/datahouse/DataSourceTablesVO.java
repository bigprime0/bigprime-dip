package com.bigprime.vo.datahouse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@Schema(description = "数据库表列表信息")
public class DataSourceTablesVO implements Serializable {
    @Schema(description = "表名称")
    public String tableName;

    @Schema(description = "中文名称")
    public String tableComment;
}
