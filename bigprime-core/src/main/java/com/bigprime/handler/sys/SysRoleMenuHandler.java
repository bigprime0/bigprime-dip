package com.bigprime.handler.sys;

import cn.hutool.core.collection.CollUtil;
import com.bigprime.handler.sys.model.SysRoleMenuModel;
import com.easy.query.api.proxy.base.LongProxy;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色菜单关系操作帮助类
 * @author lyw
 * @version 1.0
 */
@Component
@AllArgsConstructor
public class SysRoleMenuHandler {
    private final EasyEntityQuery query;


    /**
     * 根据菜单ID删除角色菜单关系
     * @param menuId
     * @return
     */
    public long deleteByMenuId(Long menuId){
        return query.deletable(SysRoleMenuModel.class).where(s -> s.menuId().eq(menuId)).executeRows();
    }

    /**
     * 根据角色ID删除
     * @param roleId
     * @return
     */
    public long deleteByRoleIds(Long roleId){
        return query.deletable(SysRoleMenuModel.class).where(s -> s.roleId().eq(roleId)).executeRows();
    }


    /**
     * 根据角色ID获取菜单集合
     * @param roleId
     * @return
     */
    public List<Long> getMenuIdByRoleId(Long roleId){
        return query.queryable(SysRoleMenuModel.class).where(s -> s.roleId().eq(roleId)).select(s -> new LongProxy(s.menuId())).toList();
    }

    /**
     * 根据角色ID集合获取菜单对应菜单
     * @param roleIds 角色Id集合
     * @return
     */
    public Map<Long,List<Long>> getByRoleIds(List<Long> roleIds){
        List<SysRoleMenuModel> list = query.queryable(SysRoleMenuModel.class).where(s -> s.roleId().in(roleIds)).toList();
        return list.stream().collect(Collectors.groupingBy(SysRoleMenuModel::getRoleId,Collectors.mapping(SysRoleMenuModel::getMenuId,Collectors.toList())));
    }


    /**
     * 新增或者删除角色菜单关联信息
     * @param roleId
     * @param menuIdList
     */
    public void saveOrUpdate(Long roleId, List<Long> menuIdList){
        if(menuIdList == null){
            menuIdList = new ArrayList<>();
        }
        //据库菜单ID列表
        List<Long> dbMenuIdList = getMenuIdByRoleId(roleId);

        // 需要新增的菜单ID
        Collection<Long> insertMenuIdList = CollUtil.subtract(menuIdList, dbMenuIdList);
        if (CollUtil.isNotEmpty(insertMenuIdList)){
            List<SysRoleMenuModel> menuList = insertMenuIdList.stream().map(menuId -> {
                SysRoleMenuModel model = new SysRoleMenuModel();
                model.setMenuId(menuId);
                model.setRoleId(roleId);
                return model;
            }).collect(Collectors.toList());

            //批量新增
            query.insertable(menuList).executeRows();
        }

        //需要删除的菜单ID
        Collection<Long> deleteMenuIdList = CollUtil.subtract(dbMenuIdList, menuIdList);
        if (CollUtil.isNotEmpty(deleteMenuIdList)){
            query.deletable(SysRoleMenuModel.class).where(s -> {
                s.roleId().eq(roleId);
                s.menuId().in(deleteMenuIdList);
            }).executeRows();
        }
    }
}
