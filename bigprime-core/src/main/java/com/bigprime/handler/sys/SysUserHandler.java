package com.bigprime.handler.sys;

import cn.hutool.core.util.StrUtil;
import com.bigprime.common.base.MyPageResult;
import com.bigprime.common.base.MyPager;
import com.bigprime.handler.sys.model.SysUserModel;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 用户管理操作帮助类
 * @author lyw
 * @version 1.0
 */
@Component
@AllArgsConstructor
public class SysUserHandler {
    private final EasyEntityQuery query;

    /**
     * 根据条件获取分页数据
     * @param search 请求参数
     * @param page 请求页数
     * @param limit 请求条数
     * @return
     */
    public MyPageResult<SysUserModel> getPage(String search, Integer page, Integer limit){
        return query.queryable(SysUserModel.class).where(w -> {
            if(!StrUtil.isBlank(search)){
                w.or(() -> {
                    w.username().like(search);
                    w.realName().like(search);
                });
            }
        }).toPageResult(new MyPager<>(page, limit));
    }

    public List<SysUserModel> getList(){
        return query.queryable(SysUserModel.class).toList();
    }


    /**
     * 保存数据
     * @param model 保存的实体
     */
    public long save(SysUserModel model){
        return query.insertable(model).executeRows(true);
    }

    /**
     * 更新密码
     * @param model
     * @return
     */
    public long updatePassword(SysUserModel model){
        return query.updatable(SysUserModel.class).setColumns(o -> {
            o.password().set(model.getPassword());
        }).where(w -> w.id().eq(model.getId())).executeRows();
    }


    /**
     * 更新数据
     * @param model 更新的实体
     */
    public long update(SysUserModel model){
        return query.updatable(SysUserModel.class).setColumns(o -> {
            o.realName().set(model.getRealName());
            o.avatar().set(model.getAvatar());
            o.mobile().set(model.getMobile());
            o.email().set(model.getEmail());
            o.status().set(model.getStatus());
            o.gender().set(model.getGender());
            o.orgId().set(model.getOrgId());
            o.superAdmin().set(model.getSuperAdmin());
        }).where(w -> w.id().eq(model.getId())).executeRows();
    }

    /**
     * 更新头像
     * @param model
     * @return
     */
    public long updateAvatar(SysUserModel model){
        return query.updatable(SysUserModel.class).setColumns(o -> {
            o.avatar().set(model.getAvatar());
        }).where(w -> w.id().eq(model.getId())).executeRows();
    }

    /**
     * 根据ID删除
     * @param id
     */
    public long delete(Long id){
        return query.deletable(SysUserModel.class).where(s -> s.id().eq(id)).executeRows();
    }

    /**
     * 根据ID获取数据
     * @param id Id
     * @return
     */
    public SysUserModel getById(Long id){
        return query.queryable(SysUserModel.class).whereById(id).firstOrNull();
    }

    /**
     * 根据ID集合获取数据
     * @param ids
     * @return
     */
    public List<SysUserModel> getByIds(List<Long> ids){
        return query.queryable(SysUserModel.class).where(s -> s.id().in(ids)).toList();
    }

    /**
     * 根据ID集合获取用户姓名
     * @param ids
     * @return
     */
    public Map<Long,String> getNameByIds(List<Long> ids){
        Map<Long,String> maps = new HashMap<>();
        List<SysUserModel> list = getByIds(ids);
        if(list.size() > 0){
            maps = list.stream().collect(Collectors.toMap(SysUserModel::getId,SysUserModel::getRealName));
        }
        return maps;
    }


    /**
     * 根据username获取信息
     * @param username
     * @return
     */
    public SysUserModel getByUsername(String username){
        return query.queryable(SysUserModel.class).where(s -> s.username().eq(username)).firstOrNull();
    }

    /**
     * 根据手机号获取信息
     * @param mobile
     * @return
     */
    public SysUserModel getByMobile(String mobile){
        return query.queryable(SysUserModel.class).where(s -> s.mobile().eq(mobile)).firstOrNull();
    }


    /**
     * 根据组织ID获取员工数量
     * @param orgId
     * @return
     */
    public long getCountByOrgId(Long orgId){
        return query.queryable(SysUserModel.class).where(s -> s.orgId().eq(orgId)).count();
    }
}
