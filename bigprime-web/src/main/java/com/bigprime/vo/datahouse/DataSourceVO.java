package com.bigprime.vo.datahouse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@Schema(description = "数据源管理-类型")
public class DataSourceVO implements Serializable {
    @Schema(description = "数据源类型")
    List<DataSourceTypesVO> dsTypes = new ArrayList<>();

    @Schema(description = "数据源明细")
    List<DataSourceOptionsVO> dsOptions = new ArrayList<>();
}
