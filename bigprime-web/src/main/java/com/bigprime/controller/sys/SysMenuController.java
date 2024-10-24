package com.bigprime.controller.sys;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import com.bigprime.common.base.Result;
import com.bigprime.common.base.SecurityUser;
import com.bigprime.common.base.UserDetail;
import com.bigprime.service.sys.SysMenuService;
import com.bigprime.vo.sys.SysMenuVO;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

/**
 * @author lyw
 * @version 1.0
 */
@RestController
@RequestMapping("menu")
@Tag(name="菜单管理")
@AllArgsConstructor
public class SysMenuController {
    private final SysMenuService dataService;

    @GetMapping("get-menus")
    @Operation(summary = "菜单")
    public Result<List<SysMenuVO>> getMenus(){
        UserDetail user = SecurityUser.getUser();
        List<SysMenuVO> list = dataService.getUserMenuList(user);

        return Result.ok(list);
    }

    @PostMapping
    @Operation(summary = "保存数据")
    public Result<Boolean> save(@RequestBody @Valid SysMenuVO vo){
        return Result.ok(dataService.save(vo) > 0);
    }

    @PutMapping
    @Operation(summary = "更新数据")
    public Result<Boolean> update(@RequestBody @Valid SysMenuVO vo){
        return Result.ok(dataService.update(vo) > 0);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "删除")
    public Result<Boolean> delete(@PathVariable("id") Long id){
        return Result.ok(dataService.delete(id) > 0);
    }
}
