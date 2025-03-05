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
@Schema(description = "数据仓库-分层树")
public class DataHouseGetLayerTreeVO  implements Serializable {

    @Schema(description = "数据分层")
    List<DataHouseGetLayerVO> treeData = new ArrayList<>();

    @Schema(description = "数据库层级对应的表结构描述, key对应数据库层级ID,value是对应的表、列等结构描述")
    List<DataHouseStructVO> structData = new ArrayList<>();
}
