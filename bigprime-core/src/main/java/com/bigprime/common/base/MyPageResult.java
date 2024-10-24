package com.bigprime.common.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@Schema(description = "分页数据")
public class MyPageResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "总记录数")
    private long total;

    @Schema(description = "列表数据")
    private List<T> list;

    /**
     * 分页
     * @param list   列表数据
     * @param total  总记录数
     */
    public MyPageResult(List<T> list, long total) {
        this.list = list;
        this.total = total;
    }
}
