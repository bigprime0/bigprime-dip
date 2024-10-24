package com.bigprime.controller.sys;

import cn.hutool.core.util.StrUtil;
import com.bigprime.query.BaseQuery;
import com.bigprime.vo.sys.SysUserPasswordVO;
import com.bigprime.vo.sys.SysUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import com.bigprime.common.base.MyPageResult;
import com.bigprime.common.base.Result;
import com.bigprime.common.base.SecurityUser;
import com.bigprime.common.base.UserDetail;
import com.bigprime.service.sys.SysUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author lyw
 * @version 1.0
 */
@RestController
@RequestMapping("user")
@Tag(name="用户管理")
@AllArgsConstructor
public class SysUserController {
    private final SysUserService dataService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("page")
    @Operation(summary = "根据条件获取分页数据")
    public Result<MyPageResult<SysUserVO>> page(@RequestBody BaseQuery query){
        return Result.ok(dataService.getPage(query));
    }

    @GetMapping("list")
    @Operation(summary = "获取全部")
    public Result<List<SysUserVO>> getList(){
        return Result.ok(dataService.getList());
    }

    @PutMapping("password")
    @Operation(summary = "修改密码")
    public Result<Boolean> password(@RequestBody @Valid SysUserPasswordVO vo) {
        // 原密码不正确
        UserDetail user = SecurityUser.getUser();
        if (!passwordEncoder.matches(vo.getPassword(), user.getPassword())) {
            return Result.ok(false);
        }
        return Result.ok(dataService.updatePassword(user.getId(), passwordEncoder.encode(vo.getNewPassword())) > 0);
    }

    @PostMapping
    @Operation(summary = "保存数据")
    public Result<Boolean> save(@RequestBody @Valid SysUserVO vo){
        if (StrUtil.isBlank(vo.getPassword())) {
            vo.setPassword("123456");
        }
        //密码加密
        vo.setPassword(passwordEncoder.encode(vo.getPassword()));
        return Result.ok(dataService.save(vo) > 0);
    }

    @PutMapping
    @Operation(summary = "更新数据")
    public Result<Boolean> update(@RequestBody @Valid SysUserVO vo){
        return Result.ok(dataService.update(vo) > 0);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "根据ID删除数据")
    public Result<Boolean> delete(@PathVariable("id") Long id)
    {
        return Result.ok(dataService.delete(id) > 0);
    }

    @GetMapping("{id}")
    @Operation(summary = "根据Id获取数据")
    public Result<SysUserVO> getById(@PathVariable("id") Long id){
        return Result.ok(dataService.getById(id));
    }
}
