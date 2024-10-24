package com.bigprime.handler.sys.model;

import com.bigprime.common.base.BaseModel;
import com.bigprime.handler.sys.model.proxy.SysMenuModelProxy;
import com.easy.query.core.annotation.EntityProxy;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;

/**
 * 菜单管理
 * @author lyw
 * @version 1.0
 */
@Data
@Table("sys_menu")
@EntityProxy
public class SysMenuModel extends BaseModel implements ProxyEntityAvailable<SysMenuModel , SysMenuModelProxy> {

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 上级ID，一级菜单为0
     */
    private Long pid;

    /**
     * 排序
     */
    private Integer sort;


    /**
     * 菜单URL
     */
    private String url;

    /**
     * 授权标识(多个用逗号分隔，如：sys:menu:list,sys:menu:save)
     */
    private String authority;

    /**
     * 菜单名称
     */
    private String name;

}
