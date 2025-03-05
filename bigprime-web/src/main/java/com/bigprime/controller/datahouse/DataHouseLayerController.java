package com.bigprime.controller.datahouse;

import com.bigprime.common.base.Result;
import com.bigprime.query.datahouse.DataHouseBindLayerSourceQuery;
import com.bigprime.service.datahouse.DataHouseLayerService;
import com.bigprime.vo.datahouse.DataHouseGetLayerTreeVO;
import com.bigprime.vo.datahouse.DataHouseGetSourcesVO;
import com.bigprime.vo.spi.SourceTreeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数仓管理-数仓维护
 *
 * @author lyw
 * @version 1.0
 */
@RestController
@RequestMapping("/layer")
@Tag(name = "数仓管理-数仓维护")
@AllArgsConstructor
public class DataHouseLayerController {
    private final DataHouseLayerService dataHouseService;

    @GetMapping("/get-house-tree")
    @Operation(summary = "获取仓库结构信息")
    public Result<List<SourceTreeVO>> getHouseTree() {
        return Result.ok(dataHouseService.getHouseTree());
    }

    @GetMapping("/get-layer-tree")
    @Operation(summary = "获取层级信息")
    public Result<DataHouseGetLayerTreeVO> getLayerTree() {
        return Result.ok(dataHouseService.getLayerTree());
    }

    @GetMapping("/get-sources")
    @Operation(summary = "获取可用的数据库列表(过滤已绑定过的)")
    public Result<List<DataHouseGetSourcesVO>> getSources() {
        return Result.ok(dataHouseService.getSourcesByType());
    }

    @PostMapping("/bind-layer-source")
    @Operation(summary = "绑定数据源")
    public Result<Boolean> bindLayerSource(@RequestBody DataHouseBindLayerSourceQuery query) {
        return Result.ok(dataHouseService.saveLayerDetail(query));
    }

    @GetMapping("/delete/{id}")
    @Operation(summary = "根据绑定的数据库ID删除绑定关系")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(dataHouseService.deleteLayerDetail(id));
    }
}
