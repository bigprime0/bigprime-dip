package com.bigprime.handler.integration;

import com.bigprime.common.base.MyPageResult;
import com.bigprime.common.base.MyPager;
import com.bigprime.common.utils.IdUtils;
import com.bigprime.handler.integration.model.IntegrationConfigDetailModel;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class IntegrationConfigDetailHandler {
    private final EasyEntityQuery query;

    /**
     * 根据分类项查询配置明细(分页)
     *
     * @param category
     * @param page
     * @param limit
     * @return
     */
    public MyPageResult<IntegrationConfigDetailModel> getDetailsByConfig(String parent, String category, String search, Integer page, Integer limit) {
        return query.queryable(IntegrationConfigDetailModel.class)
                .where(i -> {
                    i.or(() -> {
                        i.category().eq(category);
                        i.subCategory().eq(category);
                    });
                })
                .where(i -> {
                    i.or(() -> {
                        i.product().eq(parent);
                        i.category().eq(parent);
                    });
                })
                .where(i -> {
                    i.or(() -> {
                        i.configTitle().like(search);
                        i.configKey().like(search);
                        i.compType().like(search);
                        i.defaultValue().like(search);
                    });
                })
                .orderBy(i -> i.seq().asc())
                .toPageResult(new MyPager<>(page, limit));
    }

    /**
     * 查询某项下的配置明细
     *
     * @param category
     * @return
     */
    public List<IntegrationConfigDetailModel> getDetailsByConfig(String category) {
        return query.queryable(IntegrationConfigDetailModel.class)
                .where(i -> {
                    i.or(() -> {
                        i.category().eq(category);
                        i.subCategory().eq(category);
                    });
                })
                .orderBy(i -> i.seq().asc())
                .toList();
    }

    public List<IntegrationConfigDetailModel> getAll() {
        return query.queryable(IntegrationConfigDetailModel.class).orderBy(i -> i.seq().asc()).toList();
    }

    /**
     * 根据标识取单一配置项
     *
     * @param id
     * @return
     */
    public IntegrationConfigDetailModel getById(Long id) {
        return query.queryable(IntegrationConfigDetailModel.class).whereById(id).firstOrNull();
    }

    /**
     * 保存配置项
     *
     * @param model
     * @return
     */
    public long save(IntegrationConfigDetailModel model) {
        model.setConfigCode(model.getConfigKey().replace('.', '_'));
        if (model.id <= 0) {
            model.setSeq(IdUtils.getSeqId() * 10);
            return query.insertable(model).executeRows();
        } else {
            return query.updatable(model).executeRows();
        }
    }

    /**
     * 删除配置项
     *
     * @param id
     * @return
     */
    public long delete(Long id) {
        IntegrationConfigDetailModel model = getById(id);
        return model == null ? 0 : query.deletable(model).executeRows();
    }
}
