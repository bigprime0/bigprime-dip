package com.bigprime.handler.sys;

import com.bigprime.handler.sys.model.SysOrgModel;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 机构管理操作帮助类
 * @author lyw
 * @version 1.0
 */
@Component
@AllArgsConstructor
public class SysOrgHandler {
    private final EasyEntityQuery query;

    /**
     * 保存数据
     * @param model 保存的实体
     */
    public long save(SysOrgModel model){
        return query.insertable(model).executeRows(true);
    }

    /**
     * 更新数据
     * @param model 更新的实体
     */
    public long update(SysOrgModel model){
        return query.updatable(model).executeRows();
    }

    /**
     * 根据ID删除
     * @param id ID
     */
    public long delete(Long id){
        return query.deletable(SysOrgModel.class).where(s -> s.id().eq(id)).executeRows();
    }

    /**
     * 获取全部数据
     * @return
     */
    public List<SysOrgModel> getAll(){
        return query.queryable(SysOrgModel.class).toList();
    }


    /**
     * 根据ID集合获取数据
     * @param ids Id集合
     * @return
     */
    public List<SysOrgModel> getByIds(List<Long> ids){
        return query.queryable(SysOrgModel.class).whereByIds(ids).toList();
    }


    /**
     * 本部门及子部门数据
     * @param orgId
     * @return
     */
    public List<Long> getDeptAndSubDeptList(Long orgId){
        List<SysOrgModel> orgList = getAll();
        List<Long> subIdList = new ArrayList<>();
        getTree(orgId, orgList, subIdList);
        subIdList.add(orgId);
        return subIdList;
    }
    private void getTree(Long id, List<SysOrgModel> orgList, List<Long> subIdList) {
        for (SysOrgModel org : orgList) {
            if (org.getPid().equals(id)) {
                getTree(org.getId(), orgList, subIdList);
                subIdList.add(org.getId());
            }
        }
    }

    /**
     * 获取子部门数量
     * @param parentId
     * @return
     */
    public long getSubCount(Long parentId){
        return query.queryable(SysOrgModel.class).where(s -> s.pid().eq(parentId)).count();
    }

    /**
     * 根据ID集合获取名称
     * @param ids
     * @return
     */
    public Map<Long,String> getOrgNameByIds(List<Long> ids){
        List<SysOrgModel> list = query.queryable(SysOrgModel.class).where(s -> s.id().in(ids)).toList();
        return list.stream().collect(Collectors.toMap(SysOrgModel::getId,SysOrgModel::getName));
    }

    public Map<Long,String> getOrgName(){
        List<SysOrgModel> list = query.queryable(SysOrgModel.class).toList();
        return list.stream().collect(Collectors.toMap(SysOrgModel::getId,SysOrgModel::getName));
    }
}
