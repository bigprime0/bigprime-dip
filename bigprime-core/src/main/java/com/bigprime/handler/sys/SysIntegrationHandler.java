package com.bigprime.handler.sys;

import com.bigprime.common.base.MyPageResult;
import com.bigprime.common.base.MyPager;
import com.bigprime.handler.sys.model.SysIntegrationModel;
import com.bigprime.common.utils.StrUtils;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import com.easy.query.core.basic.jdbc.tx.Transaction;
import lombok.AllArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@AllArgsConstructor
public class SysIntegrationHandler {
    private final EasyEntityQuery query;

    /**
     * 新增配置项
     *
     * @param model
     * @return
     */
    public long save(SysIntegrationModel model) {
        return query.insertable(model).executeRows(true);
    }

    /**
     * 批量新增配置项
     *
     * @param models
     * @return
     */
    public long save(Collection<SysIntegrationModel> models) {
        return query.insertable(models).executeRows(true);
    }

    /**
     * 克隆批次配置项
     *
     * @param product
     * @param dbType
     * @param category
     * @return
     */
    public boolean clone(String product, String dbType, String category, String newType, boolean isOverlay) {
        var list = list(product, dbType, category);
        if (list.isEmpty()) return false;
        List<SysIntegrationModel> newList = new ArrayList<>();
        list.forEach(model -> {
            model.setDbType(newType);
            model.setId(0L);
            newList.add(model);
        });
        long _counts = 0;
        try (Transaction transaction = query.beginTransaction()) {
            if (isOverlay) {
                //执行删除操作
                query.deletable(SysIntegrationModel.class)
                        .where(w -> {
                            w.product().eq(product);
                            w.dbType().eq(newType);
                            w.category().eq(category);
                        })
                        .executeRows();
            }
            _counts = save(newList);
            transaction.commit();
        }
        return _counts > 0;
    }

    /**
     * 更新配置项
     *
     * @param model
     * @return
     */
    public long update(SysIntegrationModel model) {
        return query.updatable(model).executeRows();
    }

    /**
     * 删除配置项
     *
     * @param id
     * @return
     */
    public long delete(long id) {
        return query.deletable(SysIntegrationModel.class)
                .where(w -> w.id().eq(id))
                .executeRows();
    }

    /**
     * 分页查询
     *
     * @param product
     * @param dbType
     * @param page
     * @param limit
     * @return
     */
    public MyPageResult<SysIntegrationModel> getPageList(String product, String dbType, String category, Integer page, Integer limit) {
        return query.queryable(SysIntegrationModel.class).where(w -> {
            w.product().eq(!StrUtils.IsNullOrEmpty(product), product);
            w.dbType().eq(!StrUtils.IsNullOrEmpty(dbType), dbType);
            w.category().eq(!StrUtils.IsNullOrEmpty(category), category);
        }).orderBy(o -> {
            o.product();
            o.dbType();
            o.category();
            o.seq();
        }).toPageResult(new MyPager<>(page, limit));
    }

    /**
     * 根据Id查询单个配置
     *
     * @param id
     * @return
     */
    public SysIntegrationModel getById(Long id) {
        return query.queryable(SysIntegrationModel.class).whereById(id).firstOrNull();
    }

    /**
     * 查询配置项集合
     *
     * @param product
     * @param dbType
     * @param category
     * @return
     */
    public List<SysIntegrationModel> list(String product, String dbType, String category) {
        return query.queryable(SysIntegrationModel.class).where(w -> {
            w.product().eq(!StrUtils.IsNullOrEmpty(product), product);
            w.dbType().eq(!StrUtils.IsNullOrEmpty(dbType), dbType);
            w.category().eq(!StrUtils.IsNullOrEmpty(category), category);
        }).orderBy(o -> o.seq()).toList();
    }

}
