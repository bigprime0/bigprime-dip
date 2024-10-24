package com.bigprime.handler.sys;

import cn.hutool.core.collection.CollUtil;
import com.bigprime.handler.sys.model.SysUserRoleModel;
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
 * 用户角色关系操作帮助类
 * @author lyw
 * @version 1.0
 */
@Component
@AllArgsConstructor
public class SysUserRoleHandler {
    private final EasyEntityQuery query;

    /**
     * 批量保存，存在则忽略
     * @param models
     * @return
     */
    public long batchSave(List<SysUserRoleModel> models){
        return  query.insertable(models).onConflictThen(null).executeRows();
    }

    /**
     * 保存或更新
     * @param userId
     * @param roleIdList
     */
    public void saveOrUpdate(Long userId, List<Long> roleIdList) {
        if(roleIdList == null){
            roleIdList = new ArrayList<>();
        }
        //数据库角色ID列表
        List<Long> dbRoleIdList = getRoleIdsByUserId(userId);

        //需要新增的角色ID
        Collection<Long> insertRoleIdList = CollUtil.subtract(roleIdList, dbRoleIdList);
        if (CollUtil.isNotEmpty(insertRoleIdList)) {
            List<SysUserRoleModel> roleList = insertRoleIdList.stream().map(roleId -> {
                SysUserRoleModel model = new SysUserRoleModel();
                model.setUserId(userId);
                model.setRoleId(roleId);
                return model;
            }).collect(Collectors.toList());

            //批量新增
            batchSave(roleList);
        }

        //需要删除的角色ID
        Collection<Long> deleteRoleIdList = CollUtil.subtract(dbRoleIdList, roleIdList);
        if (CollUtil.isNotEmpty(deleteRoleIdList)) {
            query.deletable(SysUserRoleModel.class).where(s -> {
                s.userId().eq(userId);
                s.roleId().in(deleteRoleIdList);
            }).executeRows();
        }
    }

    /**
     * 根据用户ID
     * @param userId
     * @return
     */
    public long deleteByUserIdsAndRoleId(Long userId){
        return query.deletable(SysUserRoleModel.class).where(s -> {
            s.userId().eq(userId);
        }).executeRows();
    }

    /**
     * 根据角色ID删除
     * @param roleId
     * @return
     */
    public long deleteByRoleIds(Long roleId){
        return query.deletable(SysUserRoleModel.class).where(s -> s.roleId().eq(roleId)).executeRows();
    }


    /**
     * 根据用户ID获取角色ID集合
     * @param userId
     * @return
     */
    public List<Long> getRoleIdsByUserId(Long userId){
        return query.queryable(SysUserRoleModel.class).where(s -> s.userId().eq(userId)).select(s -> new LongProxy(s.roleId())).toList();
    }

    /**
     * 根据员工ID集合获取角色ID集合信息
     * @param userIds 员工Id集合
     * @return
     */
    public Map<Long,List<Long>> getByUserIds(List<Long> userIds){
        List<SysUserRoleModel> list = query.queryable(SysUserRoleModel.class).where(s -> s.userId().in(userIds)).toList();
        return list.stream().collect(Collectors.groupingBy(SysUserRoleModel::getUserId,Collectors.mapping(SysUserRoleModel::getRoleId,Collectors.toList())));
    }

}
