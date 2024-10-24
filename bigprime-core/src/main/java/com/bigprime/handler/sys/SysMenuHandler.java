package com.bigprime.handler.sys;

import com.bigprime.handler.sys.model.SysMenuModel;
import com.bigprime.handler.sys.model.SysRoleMenuModel;
import com.bigprime.handler.sys.model.SysUserRoleModel;
import com.bigprime.handler.sys.model.proxy.SysMenuModelProxy;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 菜单管理操作帮助类
 * @author lyw
 * @version 1.0
 */
@Component
@AllArgsConstructor
public class SysMenuHandler {
    private final EasyEntityQuery query;

    /**
     * 保存数据
     * @param model 保存的实体
     */
    public long save(SysMenuModel model){
        return query.insertable(model).executeRows(true);
    }

    /**
     * 更新数据
     * @param model 更新的实体
     */
    public long update(SysMenuModel model){
        return query.updatable(model).executeRows();
    }

    /**
     * 根据ID删除
     * @param id ID
     */
    public long delete(Long id){
        return query.deletable(SysMenuModel.class).where(s -> s.id().eq(id)).executeRows();
    }

    /**
     * 获取全部数据
     * @return
     */
    public List<SysMenuModel> getAll(){
        return query.queryable(SysMenuModel.class).orderBy(s -> s.sort().asc()).toList();
    }

    /**
     * 获取父级菜单id获取子菜单个数
     * @param pid
     * @return
     */
    public Long getCountByPid(Long pid){
        return query.queryable(SysMenuModel.class).where(s -> s.pid().eq(pid)).count();
    }

    /**
     * 根据用户ID及类型获取数据
     * @param userId
     * @return
     */
    public List<SysMenuModel> getList(Long userId){
        return query.queryable(SysUserRoleModel.class)
                .leftJoin(SysRoleMenuModel.class,(s1, s2) -> s1.roleId().eq(s2.roleId()))
                .leftJoin(SysMenuModel.class,(s1,s2,s3) -> s2.menuId().eq(s3.id()))
                .where((s1, s2, s3) -> {
                    s1.userId().eq(userId);
                }).orderBy((s1, s2, s3) -> s3.sort().asc())
                .select((s1, s2, s3) -> new SysMenuModelProxy().selectAll(s3)).toList();
    }
}
