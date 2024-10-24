package com.bigprime.service.sys;

import com.bigprime.common.base.UserDetail;
import com.bigprime.common.cache.TokenStoreCache;
import com.bigprime.common.exception.ServerException;
import com.bigprime.common.utils.DozerUtils;
import com.bigprime.common.utils.TokenUtils;
import com.bigprime.vo.sys.SysAccountLoginVO;
import com.bigprime.vo.sys.SysTokenVO;
import com.bigprime.vo.sys.SysUserVO;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * @author lyw
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class SysAuthService {
    private final SysCaptchaService sysCaptchaService;
    private final AuthenticationManager authenticationManager;
    private final TokenStoreCache tokenStoreCache;

    public SysTokenVO loginByAccount(SysAccountLoginVO login) {
        // 验证码效验
        if(login.isCheckCaptcha()){
            boolean flag = sysCaptchaService.validate(login.getKey(), login.getCaptcha());
            if (!flag) {
                throw new ServerException("验证码错误");
            }
        }

        Authentication authentication;
        try {
            // 用户认证
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
        } catch (BadCredentialsException e) {
            throw new ServerException("用户名或密码错误");
        }

        // 用户信息
        UserDetail user = (UserDetail) authentication.getPrincipal();

        // 生成 accessToken
        String accessToken = TokenUtils.generator();

        // 保存用户信息到缓存
        tokenStoreCache.saveUser(accessToken, user);

        return new SysTokenVO(accessToken, DozerUtils.map(user, SysUserVO.class));
    }

    public void logout(String accessToken) {
        // 用户信息
        UserDetail user = tokenStoreCache.getUser(accessToken);
        // 删除用户信息
        tokenStoreCache.deleteUser(accessToken, user.getId());
    }
}
