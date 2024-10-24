package com.bigprime.common.filter;

import com.bigprime.common.base.UserDetail;
import com.bigprime.common.cache.TokenStoreCache;
import com.bigprime.common.utils.TokenUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lyw
 * @version 1.0
 */
@Component
@AllArgsConstructor
public class AuthenticationTokenFilter extends OncePerRequestFilter {
    private final TokenStoreCache tokenStoreCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String accessToken = TokenUtils.getAccessToken(request);
        // accessToken为空，表示未登录
        if (StringUtils.isBlank(accessToken)) {
            chain.doFilter(request, response);
            return;
        }

        // 获取登录用户信息
        UserDetail user = tokenStoreCache.getUser(accessToken);
        if (user == null) {
            chain.doFilter(request, response);
            return;
        }

        // 用户存在
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, null);

        // 新建 SecurityContext
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        chain.doFilter(request, response);
    }
}
