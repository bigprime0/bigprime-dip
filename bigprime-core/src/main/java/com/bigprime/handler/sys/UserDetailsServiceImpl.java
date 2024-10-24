package com.bigprime.handler.sys;

import com.bigprime.common.base.UserDetail;
import com.bigprime.common.constant.UserStatusEnum;
import com.bigprime.handler.sys.model.SysUserModel;
import com.bigprime.common.utils.DozerUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author lyw
 * @version 1.0
 */
@Component
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final SysUserHandler sysUserHandler;
    private final SysMenuHandler sysMenuHandler;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserModel user = sysUserHandler.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        return getUserDetails(user);
    }

    public UserDetails getUserDetails(SysUserModel user){
        UserDetail userDetail = DozerUtils.map(user, UserDetail.class);

        // 账号不可用
        if (user.getStatus() == UserStatusEnum.DISABLE.getValue()) {
            userDetail.setEnabled(false);
        }
        return userDetail;
    }
}
