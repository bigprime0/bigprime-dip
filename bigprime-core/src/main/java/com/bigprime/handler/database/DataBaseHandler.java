package com.bigprime.handler.database;

import cn.hutool.json.JSONUtil;
import com.bigprime.common.config.NacosConfig;
import com.bigprime.common.constant.DataBaseEnum;
import com.bigprime.common.exception.ServerException;
import com.bigprime.handler.database.model.DataDatabaseModel;
import com.bigprime.plugin.manager.Plugin;
import com.bigprime.plugin.manager.constant.PluginType;
import com.bigprime.plugin.manager.internals.impl.SourcePlugin;
import com.bigprime.plugin.manager.model.PluginModel;
import com.bigprime.source.spi.model.SourceConfig;
import com.easy.query.api.proxy.base.StringProxy;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author lyw
 * @version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class DataBaseHandler {
    private final EasyEntityQuery query;
    private final NacosConfig config;

    /**
     * 根据Id获取数据库配置信息
     *
     * @param id
     * @return
     */
    public DataDatabaseModel getById(Long id) {
        if (id == -1) {
            return config.getDataBaseModel();
        }
        return query.queryable(DataDatabaseModel.class).whereById(id).firstOrNull();
    }

    /**
     * 更新状态
     *
     * @param id
     * @param active
     */
    public void updateActive(Long id, Integer active) {
        query.updatable(DataDatabaseModel.class).setColumns(d -> {
            d.active().set(active);
        }).where(d -> d.id().eq(id)).executeRows();
    }

    /**
     * 根据Id集合获取数据库配置信息
     *
     * @param ids
     * @return
     */
    public List<DataDatabaseModel> getByIds(List<Long> ids) {
        return query.queryable(DataDatabaseModel.class).whereByIds(ids).toList();
    }

    /**
     * 根据数据库类型集合获取配置信息
     *
     * @param types
     * @return
     */
    public List<DataDatabaseModel> getByTypes(List<String> types) {
        return query.queryable(DataDatabaseModel.class).where(o -> o.product().in(types)).toList();
    }

    /**
     * 根据数据库ID获取数据库类型
     *
     * @param id
     * @return
     */
    public DataBaseEnum getDataBaseEnumById(Long id) {
        String product = query.queryable(DataDatabaseModel.class).where(d -> d.id().eq(id)).select(d -> new StringProxy(d.product())).firstOrNull();
        if (product != null) {
            return DataBaseEnum.getByProduct(product);
        }
        return DataBaseEnum.UNKNOWN;
    }

    /**
     * 查询数据源
     *
     * @param search
     * @return
     */
    public List<DataDatabaseModel> getList(String search) {
        return query.queryable(DataDatabaseModel.class).where(w -> {
            if (!search.isEmpty()) {
                w.or(() -> {
                    w.name().like(search);
                    w.summary().like(search);
                });
            }
        }).toList();
    }

    /**
     * 保存数据源
     *
     * @param model
     * @return
     */
    public long save(DataDatabaseModel model) {
        long result = 0L;
        if (model.id <= 0) {
            result = query.insertable(model).executeRows(true);
        } else {
            result = query.updatable(model).executeRows();
        }
        registerSource(model);
        return result;
    }

    /**
     * 删除数据源
     *
     * @param id
     * @return
     */
    public long delete(Long id) {
        long result = 0L;
        DataDatabaseModel model = getById(id);
        if (model != null) {
            result = query.deletable(model).executeRows();
            unregisterSource(id);
        }
        return result;
    }

    public void registerSource(DataDatabaseModel model) {
        Plugin.register(PluginModel.builder()
                .id(model.getId())
                .type(PluginType.SOURCE)
                .instance(SourcePlugin.builder()
                        .sourceConfig(JSONUtil.toBean(model.getConfig(), SourceConfig.class))
                        .build())
                .build());
    }

    public void unregisterSource(Long id) {
        Plugin.unregister(PluginType.SOURCE, id);
    }

    public void detectingSources() {
        List<DataDatabaseModel> list = getList("");
        list.forEach(model -> {
            try {
                saveSource(model);
            } catch (Exception e) {
                log.warn("Fail detectingSources", e.getMessage());
            }
        });
    }

    public long saveSource(DataDatabaseModel model) {
        boolean active = Plugin.<SourcePlugin>getPlugin(PluginType.SOURCE).testConnection(JSONUtil.toBean(model.getConfig(), SourceConfig.class));
        if (!active) {
            if (model.getId() > 0 && model.getActive() == 1) {
                model.setActive(0);
                save(model);
            }
            throw new ServerException("Connection not successful");
        } else {
            model.setActive(1);
            return save(model);
        }
    }

    @PostConstruct
    public void initSourceInjector() {
        detectingSources();
        try {
            registerSource(config.getDataBaseModel());
        } catch (Exception e) {
            log.warn("Fail initSourceInjector", e.getMessage());
        }
    }

}

