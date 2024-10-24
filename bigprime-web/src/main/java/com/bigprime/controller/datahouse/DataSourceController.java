package com.bigprime.controller.datahouse;

import com.bigprime.common.base.Result;
import com.bigprime.query.BaseQuery;
import com.bigprime.service.datahouse.DataSourceService;
import com.bigprime.vo.datahouse.DataDatabaseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 数仓管理-数据源管理
 *
 * @author lyw
 * @version 1.0
 */
@RestController
@RequestMapping("/data-source")
@Tag(name = "数仓管理-数据源管理")
@AllArgsConstructor
public class DataSourceController {
    private final DataSourceService dataSourceService;

    /**
     * 获取数据源列表
     *
     * @return
     */
    @PostMapping("/list")
    @Operation(summary = "获取数据源列表")
    public Result<List<DataDatabaseVO>> getList(@RequestBody BaseQuery query) {
        return Result.ok(dataSourceService.getList(query.getSearch()));
    }

    /**
     * 保存数据源
     *
     * @param model
     * @return
     */
    @PostMapping("/save")
    @Operation(summary = "保存数据源")
    public Result<Boolean> save(@RequestBody DataDatabaseVO model) {
        return Result.ok(dataSourceService.save(model) > 0);
    }

    /**
     * 删除数据源
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    @Operation(summary = "删除数据源")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(dataSourceService.delete(id) > 0);
    }
}
