package com.bigprime.query;

import com.bigprime.common.base.BaseModel;
import com.bigprime.common.base.MyPager;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "通用查询数据")
public class BaseQuery<T> extends MyPager {

    @Schema(description = "Id")
    private Long id;

    @Schema(description = "查询关键字")
    private String search;

    @Schema(description = "类型相关")
    private List<Integer> types;

    @Schema(description = "实体")
    private T model;
}
