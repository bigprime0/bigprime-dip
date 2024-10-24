package com.bigprime.controller.sys;

import com.bigprime.query.BaseQuery;
import com.bigprime.service.sys.SysRoleService;
import com.bigprime.vo.sys.SysRoleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import com.bigprime.common.base.MyPageResult;
import com.bigprime.common.base.Result;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author lyw
 * @version 1.0
 */
@RestController
@RequestMapping("role")
@Tag(name="角色管理")
@AllArgsConstructor
public class SysRoleController {
    private final SysRoleService dataService;

    @PostMapping("page")
    @Operation(summary = "根据条件获取分页数据")
    public Result<MyPageResult<SysRoleVO>> page(@RequestBody BaseQuery query){
        return Result.ok(dataService.getPage(query));
    }

    @PostMapping
    @Operation(summary = "保存数据")
    public Result<Boolean> save(@RequestBody @Valid SysRoleVO vo){
        return Result.ok(dataService.save(vo) > 0);
    }

    @PutMapping
    @Operation(summary = "更新数据")
    public Result<Boolean> update(@RequestBody @Valid SysRoleVO vo){
        return Result.ok(dataService.update(vo) > 0);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "根据ID删除数据")
    public Result<Boolean> delete(@PathVariable("id") Long id){
        return Result.ok(dataService.delete(id) > 0);
    }

    @GetMapping("all")
    @Operation(summary = "获取全部数据")
    public Result<List<SysRoleVO>> all(){
        return Result.ok(dataService.getAll());
    }
}
