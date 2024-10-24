package com.bigprime.common.base;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author lyw
 * @version 1.0
 */
public class SecurityUser {
    /**
     * 获取用户信息
     */
    public static UserDetail getUser() {
        UserDetail user;
        try {
            //不在同一个线程执行会获取不到，如远程调用就不行
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();
            user = (UserDetail) principal;
        } catch (Exception e) {
            return new UserDetail();
        }

        return user;
    }

    /**
     * 获取用户ID
     */
    public static Long getUserId() {
        return getUser().getId();
    }
}
