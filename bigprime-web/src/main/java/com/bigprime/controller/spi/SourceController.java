package com.bigprime.controller.spi;

import com.bigprime.common.base.Result;
import com.bigprime.query.datahouse.DataSourceQuery;
import com.bigprime.query.spi.SourceDdlQuery;
import com.bigprime.query.spi.SourceQuery;
import com.bigprime.service.spi.SourceService;
import com.bigprime.source.spi.model.Response;
import com.bigprime.source.spi.model.TableModel;
import com.bigprime.vo.datahouse.DataDatabaseVO;
import com.bigprime.vo.spi.SourceTreeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 源管理
 * @author lyw
 * @version 1.0
 */
@RestController
@RequestMapping("/source")
@Tag(name = "源管理")
@AllArgsConstructor
public class SourceController {
    private final SourceService sourceService;

    /**
     * 获取支持的插件
     * @return
     */
    @GetMapping("/get-plugin")
    @Operation(summary = "获取支持的插件")
    public Result<Object> getPlugin() {
        return Result.ok(sourceService.getPlugin());
    }


    @PostMapping("/get-list")
    @Operation(summary = "获取列表")
    public Result<List<DataDatabaseVO>> getList(@RequestBody DataSourceQuery query) {
        return Result.ok(sourceService.getList(query));
    }

    @GetMapping("/get-source-tree/{id}")
    @Operation(summary = "根据数据源ID获取数据源树信息")
    public Result<List<SourceTreeVO>> getSourceTree(@PathVariable Long id) {
        return Result.ok(sourceService.getSourceTree(id));
    }

    /**
     * 测试连接
     * @param model
     * @return
     */
    @PostMapping("/test-connection")
    @Operation(summary = "测试连接")
    public Result<Boolean> testConnection(@RequestBody DataDatabaseVO model) {
        return Result.ok(sourceService.testConnection(model));
    }

    @GetMapping("/get-tables/{id}")
    @Operation(summary = "根据数据源ID获取数据源列表信息")
    public Result<List<TableModel>> getTables(@PathVariable Long id) {
        return Result.ok(sourceService.getTables(id));
    }

    @GetMapping("/get-table/{id}/{tableName}")
    @Operation(summary = "根据数据库ID及表名获取表详情信息")
    public Result<TableModel> getTable(@PathVariable Long id, @PathVariable String tableName) {
        return Result.ok(sourceService.getTable(id, tableName));
    }

    @PostMapping("/execute")
    @Operation(summary = "获取数据")
    public Result<Response> execute(@RequestBody SourceQuery query) {
        return Result.ok(sourceService.execute(query));
    }

    @PostMapping("/execute-ddl")
    @Operation(summary = "执行ddl操作")
    public Result<Object> executeDdl(@RequestBody SourceDdlQuery query){
        return Result.ok(query.getType().execute(query));
    }
}
