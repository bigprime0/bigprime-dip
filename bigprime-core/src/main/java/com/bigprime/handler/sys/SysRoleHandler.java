package com.bigprime.handler.sys;

import cn.hutool.core.util.StrUtil;
import com.bigprime.common.base.MyPageResult;
import com.bigprime.common.base.MyPager;
import com.bigprime.handler.sys.model.SysRoleModel;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 角色管理操作帮助类
 * @author lyw
 * @version 1.0
 */
@Component
@AllArgsConstructor
public class SysRoleHandler {
    private final EasyEntityQuery query;

    /**
     * 根据条件获取分页数据
     * @param search 请求参数
     * @param page 请求页数
     * @param limit 请求条数
     * @return
     */
    public MyPageResult<SysRoleModel> getPage(String search, Integer page, Integer limit){
        return query.queryable(SysRoleModel.class).where(w -> {
            if(!StrUtil.isBlank(search)){
                w.name().like(search);
            }
        }).toPageResult(new MyPager<>(page, limit));
    }

    /**
     * 保存数据
     * @param model 保存的实体
     */
    public long save(SysRoleModel model){
        return query.insertable(model).executeRows(true);
    }

    /**
     * 更新数据
     * @param model 更新的实体
     */
    public long update(SysRoleModel model){
        return query.updatable(model).executeRows();
    }

    /**
     * 根据ID删除
     * @param id
     */
    public long delete(Long id){
        return query.deletable(SysRoleModel.class).where(s -> s.id().eq(id)).executeRows();
    }

    /**
     * 获取全部数据
     * @return
     */
    public List<SysRoleModel> getAll(){
        return query.queryable(SysRoleModel.class).toList();
    }
}
