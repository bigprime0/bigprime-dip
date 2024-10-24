package com.bigprime.controller.integration;

import com.bigprime.common.base.MyPageResult;
import com.bigprime.common.base.Result;
import com.bigprime.handler.integration.model.IntegrationConfigDetailModel;
import com.bigprime.service.integration.IntegrationConfigService;
import com.bigprime.vo.integration.IntegrationConfigCategoryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("integration")
@Tag(name = "数据集成配置")
@AllArgsConstructor
public class IntegrationConfigController {
    private final IntegrationConfigService integrationConfigService;

    @PostMapping("/config/category/tree")
    @Operation(summary = "获取分类数据")
    public Result<List<IntegrationConfigCategoryVO>> getCategoryTreeList() {
        return Result.ok(integrationConfigService.getCategoryTreeList());
    }

    @PostMapping("/config/category/save")
    @Operation(summary = "保存分类数据")
    public Result<Long> saveCategory(@RequestBody @Valid IntegrationConfigCategoryVO vo) {
        return Result.ok(integrationConfigService.saveCategory(vo));
    }

    @DeleteMapping("/config/category/{id}")
    @Operation(summary = "根据id删除分类项")
    public Result<Long> deleteCategory(@PathVariable("id") Long id) {
        return Result.ok(integrationConfigService.deleteCategory(id));
    }

    @PostMapping("/config/detail/page")
    @Operation(summary = "查询配置项列表(分页)")
    public Result<MyPageResult<IntegrationConfigDetailModel>> getDetailPageList(@RequestBody @Valid Map<String, String> params) {
        return Result.ok(integrationConfigService.getDetailPageList(params.get("parent"), params.get("category"), params.get("search"), Integer.parseInt(params.get("page")), Integer.parseInt(params.get("limit"))));
    }

    @GetMapping("/config/detail/list")
    @Operation(summary = "获取所有配置")
    public Result<List<IntegrationConfigDetailModel>> getAllDetail() {
        return Result.ok(integrationConfigService.getAllDetail());
    }

    @PostMapping("/config/detail/save")
    @Operation(summary = "保存配置项")
    public Result<Boolean> saveDetail(@RequestBody @Valid IntegrationConfigDetailModel model) {
        return Result.ok(integrationConfigService.saveConfigDetail(model) > 0);
    }

    @PutMapping("/config/detail/update")
    @Operation(summary = "更新分类配置数据")
    public Result<Boolean> updateDetail(@RequestBody @Valid IntegrationConfigDetailModel model) {
        return Result.ok(integrationConfigService.saveConfigDetail(model) > 0);
    }

    @DeleteMapping("/config/detail/{id}")
    @Operation(summary = "根据id删除配置项")
    public Result<Boolean> deleteDetail(@PathVariable("id") Long id) {
        return Result.ok(integrationConfigService.deleteConfigDetail(id) > 0);
    }
}
