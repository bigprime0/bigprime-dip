package com.bigprime.service.spi;

import cn.hutool.core.util.StrUtil;
import com.bigprime.common.constant.MetaEnum;
import com.bigprime.common.utils.DozerUtils;
import com.bigprime.handler.database.DataBaseHandler;
import com.bigprime.handler.database.model.DataDatabaseModel;
import com.bigprime.plugin.manager.Plugin;
import com.bigprime.plugin.manager.constant.PluginType;
import com.bigprime.plugin.manager.internals.impl.SourcePlugin;
import com.bigprime.query.datahouse.DataSourceQuery;
import com.bigprime.query.spi.SourceQuery;
import com.bigprime.service.datahouse.DataSourceService;
import com.bigprime.source.spi.model.Response;
import com.bigprime.source.spi.model.TableModel;
import com.bigprime.vo.datahouse.DataDatabaseVO;
import com.bigprime.vo.spi.SourceTreeVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lyw
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SourceService {
    private final DataBaseHandler baseHandler;
    private final DataSourceService dataSourceService;

    /**
     * 获取支持的插件
     *
     * @return
     */
    public Object getPlugin() {
        return Plugin.<SourcePlugin>getPlugin(PluginType.SOURCE).getPluginSupport();
    }

    /**
     * 测试连接
     *
     * @param vo
     * @return
     */
    public Boolean testConnection(DataDatabaseVO vo) {
        boolean active = Plugin.<SourcePlugin>getPlugin(PluginType.SOURCE).testConnection(vo.getSource());

        if (vo.getId() > 0 && vo.getActive() != active) {
            vo.setActive(active);
            try {
                dataSourceService.getResult(vo);
            } catch (Exception e) {
                active = false;
            }
        }
        return active;
    }

    /**
     * 获取列表
     *
     * @param query
     * @return
     */
    public List<DataDatabaseVO> getList(DataSourceQuery query) {
        List<DataDatabaseVO> list = DozerUtils.mapList(baseHandler.getList(query.getSearch()), DataDatabaseVO.class);
        List<DataDatabaseVO> result = new ArrayList<>();
        for (DataDatabaseVO vo : list) {
            boolean add = true;
            if (vo.getActive() != query.getActive()) {
                add = false;
            }
            if (!add && StrUtil.isNotBlank(query.getProtocol()) && query.getProtocol() != vo.getSource().getProtocol()) {
                add = false;
            }
            if (!query.getConfig()) {
                vo.setConfig("");
                vo.setSource(null);
            }
            if (add) {
                result.add(vo);
            }
        }
        return result;
    }

    /**
     * 获取源树结构
     *
     * @param id
     * @return
     */
    public List<SourceTreeVO> getSourceTree(Long id) {
        List<SourceTreeVO> baseModels = new ArrayList<>();
        DataDatabaseModel model = baseHandler.getById(id);
        List<TableModel> tableModels = Plugin.<SourcePlugin>getInstance(PluginType.SOURCE, id).getTables(false);
        baseModels.add(SourceTreeVO.builder()
                .type(MetaEnum.DATABASE.toString())
                .name(model.getName())
                .children(tableModels.stream().collect(Collectors.toList()))
                .build());
        return baseModels;
    }

    /**
     * 根据数据库ID获取数据库表列表信息
     *
     * @param id
     * @return
     */
    public List<TableModel> getTables(Long id) {
        return Plugin.<SourcePlugin>getInstance(PluginType.SOURCE, id).getTables(false);
    }

    /**
     * 根据数据库ID及表名称获取表详情
     *
     * @param id
     * @param tableName
     * @return
     */
    public TableModel getTable(Long id, String tableName) {
        return Plugin.<SourcePlugin>getInstance(PluginType.SOURCE, id).getTable(tableName);
    }

    /**
     * 获取数据
     *
     * @param query
     * @return
     */
    public Response execute(SourceQuery query) {
        SourcePlugin plugin = Plugin.getInstance(PluginType.SOURCE, query.getDatabaseId());
        if (StrUtil.isNotBlank(query.getSql())) {
            return plugin.execute(query.getSql());
        }
        return plugin.execute(query.getDmlConfig(), query.getCryptoId(), query.getCryptoColumns());
    }

    public void detectingSources() {
        baseHandler.detectingSources();
    }
}
