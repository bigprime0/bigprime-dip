package com.bigprime.handler.sys.model;

import com.bigprime.common.base.BaseModel;
import com.bigprime.handler.sys.model.proxy.SysUserRoleModelProxy;
import com.easy.query.core.annotation.EntityProxy;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;

/**
 * 用户角色关系
 * @author lyw
 * @version 1.0
 */
@Data
@Table("sys_user_role")
@EntityProxy
public class SysUserRoleModel extends BaseModel implements ProxyEntityAvailable<SysUserRoleModel , SysUserRoleModelProxy> {

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 用户ID
     */
    private Long userId;

}
