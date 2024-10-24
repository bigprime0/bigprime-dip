package com.bigprime.service.datahouse;

import cn.hutool.core.util.StrUtil;
import com.bigprime.common.exception.ServerException;
import com.bigprime.common.utils.DozerUtils;
import com.bigprime.handler.database.DataBaseHandler;
import com.bigprime.handler.database.model.DataDatabaseModel;
import com.bigprime.plugin.manager.Plugin;
import com.bigprime.plugin.manager.constant.PluginType;
import com.bigprime.plugin.manager.internals.impl.SourcePlugin;
import com.bigprime.vo.datahouse.DataDatabaseVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author lyw
 * @version 1.0
 */
@Slf4j
@Service
@AllArgsConstructor
public class DataSourceService {
    private final DataBaseHandler baseHandler;

    /**
     * 查询数据源
     *
     * @param search
     * @return
     */
    public List<DataDatabaseVO> getList(String search) {
        return DozerUtils.mapList(baseHandler.getList(search), DataDatabaseVO.class);
    }

    /**
     * 保存数据源
     *
     * @param vo
     * @return
     */
    public long save(DataDatabaseVO vo) {
        boolean active = Plugin.<SourcePlugin>getPlugin(PluginType.SOURCE).testConnection(vo.getSource());
        if (!active) {
            if (vo.getId() > 0 && vo.getActive()) {
                vo.setActive(false);
                getResult(vo);
            }
            throw new ServerException("Connection not successful");
        } else {
            vo.setActive(true);
            return getResult(vo);
        }
    }

    @Transactional
    public long getResult(DataDatabaseVO vo) {
        vo.setSource(vo.getSource());
        if (StrUtil.isBlank(vo.getProduct())) {
            vo.setProduct(vo.getSource().getType());
        }
        DataDatabaseModel model = DozerUtils.map(vo, DataDatabaseModel.class);
        return baseHandler.save(DozerUtils.map(model, DataDatabaseModel.class));
    }

    /**
     * 删除数据源
     *
     * @param id
     * @return
     */
    @Transactional
    public long delete(Long id) {
        return baseHandler.delete(id);
    }
}
