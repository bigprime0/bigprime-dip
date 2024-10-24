package com.bigprime.service.integration;

import com.bigprime.common.base.MyPageResult;
import com.bigprime.common.utils.DozerUtils;
import com.bigprime.common.utils.TreeUtils;
import com.bigprime.handler.integration.IntegrationConfigCategoryHandler;
import com.bigprime.handler.integration.IntegrationConfigDetailHandler;
import com.bigprime.handler.integration.model.IntegrationConfigCategoryModel;
import com.bigprime.handler.integration.model.IntegrationConfigDetailModel;
import com.bigprime.vo.integration.IntegrationConfigCategoryVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据集成配置管理服务
 *
 * @Auther Phoenix
 * @Version 1.0
 */
@Slf4j
@Service
@AllArgsConstructor
public class IntegrationConfigService {

    private IntegrationConfigCategoryHandler configCategoryHandler;
    private IntegrationConfigDetailHandler detailHandler;

    /**
     * 获取分类项树
     *
     * @return
     */
    public List<IntegrationConfigCategoryVO> getCategoryTreeList() {
        return TreeUtils.build(DozerUtils.mapList(configCategoryHandler.getAll(), IntegrationConfigCategoryVO.class));
    }

    /**
     * 保存分类
     *
     * @param vo
     * @return
     */
    public long saveCategory(IntegrationConfigCategoryVO vo) {
        return configCategoryHandler.save(DozerUtils.map(vo, IntegrationConfigCategoryModel.class));
    }

    /**
     * 删除分类
     *
     * @param id
     * @return
     */
    public long deleteCategory(long id) {
        return configCategoryHandler.delete(id);
    }

    /**
     * 根据配置分类获取配置项分页列表
     *
     * @param parent
     * @param category
     * @param search
     * @param page
     * @param limit
     * @return
     */
    public MyPageResult<IntegrationConfigDetailModel> getDetailPageList(String parent, String category, String search, Integer page, Integer limit) {
        return detailHandler.getDetailsByConfig(parent, category, search, page, limit);
    }

    public List<IntegrationConfigDetailModel> getAllDetail() {
        return detailHandler.getAll();
    }

    /**
     * 保存明细配置项
     *
     * @param model
     * @return
     */
    public long saveConfigDetail(IntegrationConfigDetailModel model) {
        return detailHandler.save(model);
    }

    /**
     * 删除明细配置项
     *
     * @param id
     * @return
     */
    public long deleteConfigDetail(long id) {
        return detailHandler.delete(id);
    }

}
