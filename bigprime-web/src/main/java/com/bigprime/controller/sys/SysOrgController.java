package com.bigprime.controller.sys;

import com.bigprime.handler.sys.model.SysOrgModel;
import com.bigprime.service.sys.SysOrgService;
import com.bigprime.vo.sys.SysOrgVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import com.bigprime.common.base.Result;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

/**
 * @author lyw
 * @version 1.0
 */
@RestController
@RequestMapping("org")
@Tag(name="组织管理")
@AllArgsConstructor
public class SysOrgController {
    private final SysOrgService dataService;

    @GetMapping("get-list")
    @Operation(summary = "列表")
    public Result<List<SysOrgVO>> getList() {
        return Result.ok(dataService.getList());
    }

    @GetMapping("get-all")
    @Operation(summary = "所有数据")
    public Result<List<SysOrgModel>> getAll() {
        return Result.ok(dataService.getAll());
    }


    @PostMapping
    @Operation(summary = "保存数据")
    public Result<Boolean> save(@RequestBody @Valid SysOrgVO vo){
        return Result.ok(dataService.save(vo) > 0);
    }

    @PutMapping
    @Operation(summary = "更新数据")
    public Result<Boolean> update(@RequestBody @Valid SysOrgVO vo){
        return Result.ok(dataService.update(vo) > 0);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "根据ID删除数据")
    public Result<Boolean> delete(@PathVariable("id") Long id){
        return Result.ok(dataService.delete(id) > 0);
    }
}
