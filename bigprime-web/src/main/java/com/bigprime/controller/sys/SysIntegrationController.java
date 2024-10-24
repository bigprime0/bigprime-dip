package com.bigprime.controller.sys;

import cn.hutool.core.lang.Pair;
import com.bigprime.common.base.MyPageResult;
import com.bigprime.common.base.Result;
import com.bigprime.handler.sys.model.SysIntegrationModel;
import com.bigprime.service.sys.SysIntegrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("integration/config")
@Tag(name = "集成配置")
@AllArgsConstructor
public class SysIntegrationController {
    private final SysIntegrationService integrationConfigService;

    @PostMapping
    @Operation(summary = "保存配置项")
    public Result<Boolean> save(@RequestBody @Valid SysIntegrationModel model) {
        return Result.ok(integrationConfigService.save(model) > 0);
    }

    @PutMapping
    @Operation(summary = "更新配置项")
    public Result<Boolean> update(@RequestBody @Valid SysIntegrationModel model) {
        return Result.ok(integrationConfigService.update(model) > 0);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "根据id删除配置项")
    public Result<Boolean> delete(@PathVariable("id") Long id) {
        return Result.ok(integrationConfigService.delete(id) > 0);
    }

    @PostMapping("/list")
    @Operation(summary = "查询配置项列表")
    public Result<List<SysIntegrationModel>> list(@RequestBody @Valid Map<String, String> params) {
        return Result.ok(integrationConfigService.list(params.get("product"), params.get("dbType"), params.get("category")));
    }

    @PostMapping("/page")
    @Operation(summary = "查询配置项列表")
    public Result<MyPageResult<SysIntegrationModel>> pageList(@RequestBody @Valid Map<String, String> params) {
        return Result.ok(integrationConfigService.getPageList(params.get("product"), params.get("dbType"), params.get("category"), Integer.parseInt(params.get("page")), Integer.parseInt(params.get("limit"))));
    }

    @PostMapping("/clone")
    @Operation(summary = "查询配置项列表")
    public Result<Pair<Boolean, String>> clone(@RequestBody @Valid Map<String, String> params) {
        return Result.ok(integrationConfigService.clone(params.get("product"), params.get("dbType"), params.get("category"), params.get("newType"),Boolean.parseBoolean(params.get("isOverlay"))));
    }


}
