package com.bigprime.handler.datahouse;

import com.bigprime.handler.datahouse.model.DataHouseLayerModel;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lyw
 * @version 1.0
 */
@Component
@AllArgsConstructor
public class DataHouseLayerHandler {

    private final EasyEntityQuery query;

    /**
     * 获取所有层级信息
     * @return
     */
    public List<DataHouseLayerModel> getAll(){
        return query.queryable(DataHouseLayerModel.class).toList();
    }
}
