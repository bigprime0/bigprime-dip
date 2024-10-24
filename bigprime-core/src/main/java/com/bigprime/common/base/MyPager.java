package com.bigprime.common.base;

import com.easy.query.core.api.pagination.EasyPageResult;
import com.easy.query.core.api.pagination.Pager;
import com.easy.query.core.basic.api.select.executor.PageAble;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyPager<TEntity> implements Pager<TEntity, MyPageResult<TEntity>> {

    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码最小值为 1")
    @Schema(description = "当前页码", required = true)
    Integer page;

    @NotNull(message = "每页条数不能为空")
    @Min(value = 1, message = "每页条数，取值范围 1-1000")
    @Max(value = 1000, message = "每页条数，取值范围 1-1000")
    @Schema(description = "每页条数", required = true)
    Integer limit;

    @Override
    public MyPageResult<TEntity> toResult(PageAble<TEntity> pageAble) {
        EasyPageResult<TEntity> pageResult = pageAble.toPageResult(page, limit);
        return new MyPageResult(pageResult.getData(), pageResult.getTotal());
    }
}
