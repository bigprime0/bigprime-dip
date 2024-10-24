package com.bigprime.handler.integration;

import com.bigprime.common.utils.StrUtils;
import com.bigprime.handler.integration.model.IntegrationConfigCategoryModel;
import com.bigprime.handler.integration.model.IntegrationConfigDetailModel;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import com.easy.query.core.basic.jdbc.tx.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class IntegrationConfigCategoryHandler {
    private final EasyEntityQuery query;
    private final IntegrationConfigDetailHandler detailHandler;

    /**
     * 查询所有配置分类项
     *
     * @return
     */
    public List<IntegrationConfigCategoryModel> getAll() {
        return query.queryable(IntegrationConfigCategoryModel.class).toList();
    }

    public IntegrationConfigCategoryModel getById(long id) {
        return query.queryable(IntegrationConfigCategoryModel.class).whereById(id).firstOrNull();
    }

    /**
     * 保存配置分类项
     *
     * @param model
     * @return
     */
    public long save(IntegrationConfigCategoryModel model) {
        //赋值category字段
        if (StrUtils.IsNullOrEmpty(model.getCategory())) {
            IntegrationConfigCategoryModel parentModel = this.getById(model.getPid());
            if (parentModel != null) {
                if (parentModel.getLabel().equalsIgnoreCase("transform")) {
                    model.setCategory("transform");
                } else if (parentModel.getLabel().equalsIgnoreCase("source")
                        || parentModel.getLabel().equalsIgnoreCase("sink")
                        || parentModel.getLabel().equalsIgnoreCase("reader")
                        || parentModel.getLabel().equalsIgnoreCase("writer")
                ) {
                    model.setCategory("connector");
                } else {

                }
            }
        }
        if (model.getId() <= 0) {
            query.insertable(model).executeRows(true);
        } else {
            IntegrationConfigCategoryModel oldModel = this.getById(model.getId());
            //修改了label需要级联修改Detail与Route
            if (!oldModel.getLabel().equals(model.getLabel())) {
                try (Transaction transaction = query.beginTransaction()) {
                    try {
                        model.setRoute(model.getRoute().replace(oldModel.getLabel(), model.getLabel()));
                        query.updatable(IntegrationConfigDetailModel.class)
                                .setColumns(i -> i.category().set(model.getLabel()))
                                .where(i -> i.category().eq(oldModel.getLabel()))
                                .executeRows();
                        query.updatable(IntegrationConfigDetailModel.class)
                                .setColumns(i -> i.subCategory().set(model.getLabel()))
                                .where(i -> i.subCategory().eq(oldModel.getLabel()))
                                .executeRows();
                        query.updatable(model).executeRows();
                        transaction.commit();
                    } catch (Exception e) {
                        transaction.rollback();
                        throw e;
                    }
                }
            } else {
                query.updatable(model).executeRows();
            }
        }
        return model.getId();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public long delete(long id) {
        IntegrationConfigCategoryModel model = getById(id);
        //判断分类下面是否存在配置项
        List<IntegrationConfigDetailModel> details = detailHandler.getDetailsByConfig(model.getLabel());
        if (details.size() > 0) {
            return 500001;
        }
        return query.deletable(model).executeRows();
    }



}
