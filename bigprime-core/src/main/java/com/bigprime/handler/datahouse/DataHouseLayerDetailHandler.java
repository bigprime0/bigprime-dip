package com.bigprime.handler.datahouse;

import com.bigprime.handler.datahouse.model.DataHouseLayerDetailModel;
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
public class DataHouseLayerDetailHandler {
    private final EasyEntityQuery query;

    /**
     * 获取绑定关系
     * @return
     */
    public List<DataHouseLayerDetailModel> getAll(){
        return query.queryable(DataHouseLayerDetailModel.class).toList();
    }

    /**
     * 保存或更新绑定关系
     * @param entity
     * @return
     */
    public boolean saveLayerDetail(DataHouseLayerDetailModel entity){
        if(entity != null){
            if(entity.getId() > 0){
                //更新
                return query.updatable(entity).executeRows() > 0;
            }else{
                //新增
                return query.insertable(entity).executeRows() > 0;
            }
        }
        return false;
    }

    /**
     * 删除绑定关系
     * @param id
     * @return
     */
    public boolean deleteLayerDetail(Long id){
        DataHouseLayerDetailModel entity = query.queryable(DataHouseLayerDetailModel.class).where(d -> d.databaseId().eq(id)).firstOrNull();
        if(entity != null){
            return query.deletable(entity).executeRows() > 0;
        }
        return false;
    }

    public List<DataHouseLayerDetailModel> getList(){
        return query.queryable(DataHouseLayerDetailModel.class)
                .include(d -> d.parent())
                .include(d -> d.database())
                .toList();
    }
}
