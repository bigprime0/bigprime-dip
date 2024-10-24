package com.bigprime.handler.sys.model;

import com.bigprime.common.base.BaseModel;
import com.bigprime.handler.sys.model.proxy.SysUserModelProxy;
import com.easy.query.core.annotation.EntityProxy;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;

/**
 * 用户管理
 * @author lyw
 * @version 1.0
 */
@Data
@Table("sys_user")
@EntityProxy
public class SysUserModel extends BaseModel implements ProxyEntityAvailable<SysUserModel , SysUserModelProxy> {

    /**
     * 性别   0：男   1：女   2：未知
     */
    private Integer gender;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 姓名
     */
    private String realName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 密码
     */
    private String password;

    /**
     * 组织ID
     */
    private Long orgId;

    /**
     * 超级管理员   0：否   1：是
     */
    private Integer superAdmin;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户名
     */
    private String username;

    /**
     * 状态  0：停用   1：正常
     */
    private Integer status;
}
