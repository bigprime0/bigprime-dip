package com.bigprime.handler.sys.model;

import com.bigprime.common.base.BaseModel;
import com.bigprime.handler.sys.model.proxy.SysRoleMenuModelProxy;
import com.easy.query.core.annotation.EntityProxy;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;

/**
 * 角色菜单关系
 * @author lyw
 * @version 1.0
 */
@Data
@Table("sys_role_menu")
@EntityProxy
public class SysRoleMenuModel extends BaseModel implements ProxyEntityAvailable<SysRoleMenuModel , SysRoleMenuModelProxy> {

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;

}
