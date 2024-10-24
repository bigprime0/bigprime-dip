package com.bigprime.controller.sys;

import com.bigprime.service.sys.SysAuthService;
import com.bigprime.service.sys.SysCaptchaService;
import com.bigprime.vo.sys.SysAccountLoginVO;
import com.bigprime.vo.sys.SysCaptchaVO;
import com.bigprime.vo.sys.SysTokenVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import com.bigprime.common.base.Result;
import com.bigprime.common.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lyw
 * @version 1.0
 */
@RestController
@RequestMapping("auth")
@Tag(name = "认证管理")
@AllArgsConstructor
public class SysAuthController {
    private final SysCaptchaService sysCaptchaService;
    private final SysAuthService sysAuthService;

    @GetMapping("captcha")
    @Operation(summary = "验证码")
    public Result<SysCaptchaVO> captcha() {
        SysCaptchaVO captchaVO = sysCaptchaService.generate();
        return Result.ok(captchaVO);
    }

    @PostMapping("login")
    @Operation(summary = "账号密码登录")
    public Result<SysTokenVO> login(@RequestBody SysAccountLoginVO login) {
        SysTokenVO token = sysAuthService.loginByAccount(login);

        return Result.ok(token);
    }

    @PostMapping("logout")
    @Operation(summary = "退出")
    public Result<String> logout(HttpServletRequest request) {
        sysAuthService.logout(TokenUtils.getAccessToken(request));
        return Result.ok();
    }
}
