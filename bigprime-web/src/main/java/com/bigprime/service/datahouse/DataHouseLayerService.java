package com.bigprime.service.datahouse;

import com.bigprime.common.constant.DataBaseEnum;
import com.bigprime.common.constant.DataStructEnum;
import com.bigprime.common.constant.MetaEnum;
import com.bigprime.common.utils.DozerUtils;
import com.bigprime.db.DBBuilder;
import com.bigprime.db.ddl.model.Struct;
import com.bigprime.db.ddl.model.TableBase;
import com.bigprime.handler.database.DataBaseHandler;
import com.bigprime.handler.database.model.DataDatabaseModel;
import com.bigprime.handler.datahouse.DataHouseLayerDetailHandler;
import com.bigprime.handler.datahouse.DataHouseLayerHandler;
import com.bigprime.handler.datahouse.model.DataHouseLayerDetailModel;
import com.bigprime.handler.datahouse.model.DataHouseLayerModel;
import com.bigprime.plugin.manager.Plugin;
import com.bigprime.plugin.manager.constant.PluginType;
import com.bigprime.plugin.manager.internals.impl.SourcePlugin;
import com.bigprime.query.datahouse.DataHouseBindLayerSourceQuery;
import com.bigprime.source.spi.model.TableModel;
import com.bigprime.vo.datahouse.DataHouseGetLayerTreeVO;
import com.bigprime.vo.datahouse.DataHouseGetLayerVO;
import com.bigprime.vo.datahouse.DataHouseGetSourcesVO;
import com.bigprime.vo.datahouse.DataHouseStructVO;
import com.bigprime.vo.spi.SourceTreeVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lyw
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class DataHouseLayerService {
    private final DataHouseLayerHandler layerHandler;
    private final DataHouseLayerDetailHandler layerDetailHandler;
    private final DataBaseHandler baseHandler;
    private final DBBuilder dbBuilder;

    public List<SourceTreeVO> getHouseTree() {
        List<SourceTreeVO> baseModels = new ArrayList<>();
        //获取层级信息
        List<DataHouseLayerModel> layers = layerHandler.getAll();
        //获取绑定信息
        List<Long> ids = new ArrayList<>();
        List<DataHouseLayerDetailModel> layerDetails = layerDetailHandler.getAll();
        for (DataHouseLayerDetailModel entity : layerDetails) {
            if (!ids.contains(entity.getDatabaseId())) {
                ids.add(entity.getDatabaseId());
            }
        }
        //获取绑定的数据库信息
        Map<Long, DataDatabaseModel> dtoMap = new HashMap<>();
        if (!ids.isEmpty()) {
            List<DataDatabaseModel> dos = baseHandler.getByIds(ids);
            if (dos != null && !dos.isEmpty()) {
                for (DataDatabaseModel entity : dos) {
                    dtoMap.put(entity.getId(), entity);
                }
            }
        }
        //组装信息
        for (DataHouseLayerModel layer : layers) {
            //第一层级
            SourceTreeVO model = new SourceTreeVO();
            model.setId(layer.getId());
            model.setType(MetaEnum.OTHER.toString());
            model.setName(layer.getName());
            model.setChildren(new ArrayList<>());
            //获取数据库层级信息
            for (DataHouseLayerDetailModel detailEntity : layerDetails) {
                //判断是否绑定数据源
                if (layer.getId().equals(detailEntity.getParentId()) && dtoMap.containsKey(detailEntity.getDatabaseId())) {
                    //第二层级
                    DataDatabaseModel databaseModel = dtoMap.get(detailEntity.getDatabaseId());
                    //获取绑定的数据库表集合信息
                    List<TableModel> tableModels = new ArrayList<>();

                    SourcePlugin plugin = Plugin.getInstance(PluginType.SOURCE, databaseModel.getId());
                    if (databaseModel.getActive() == 1 && plugin.supportDataWarehouse()) {
                        tableModels = plugin.getTables(false);
                    }
                    model.getChildren().add(SourceTreeVO.builder()
                            .id(detailEntity.getId())
                            .type(MetaEnum.DATABASE.toString())
                            .name(databaseModel.getName())
                            .databaseId(detailEntity.getDatabaseId())
                            .children(tableModels.stream().collect(Collectors.toList()))
                            .build());
                }
            }
            baseModels.add(model);
        }

        return baseModels;
    }

    /**
     * 获取数仓层级信息
     *
     * @return
     */
    public DataHouseGetLayerTreeVO getLayerTree() {
        DataHouseGetLayerTreeVO layerTreeVO = new DataHouseGetLayerTreeVO();
        //获取层级信息
        List<DataHouseLayerModel> layers = layerHandler.getAll();

        //获取数据库层级对应的表结构信息
        for (DataBaseEnum key : dbBuilder.getDataBaseEnums()) {
            Map<DataStructEnum, List<Struct>> struct = dbBuilder.getDataBaseStruct(key);
            DataHouseStructVO structVO = new DataHouseStructVO();
            structVO.setId(key.getProduct());
            structVO.setTableStruct(struct.get(DataStructEnum.TABLE));
            structVO.setColumnsStruct(struct.get(DataStructEnum.COLUMN));
            structVO.setIndexesStruct(struct.get(DataStructEnum.INDEX));
            layerTreeVO.getStructData().add(structVO);
        }

        //获取绑定信息
        List<Long> ids = new ArrayList<>();
        List<DataHouseLayerDetailModel> layerDetails = layerDetailHandler.getAll();
        for (DataHouseLayerDetailModel entity : layerDetails) {
            if (!ids.contains(entity.getDatabaseId())) {
                ids.add(entity.getDatabaseId());
            }
        }

        //获取绑定的数据库信息
        Map<Long, DataDatabaseModel> dtoMap = new HashMap<>();
        if (!ids.isEmpty()) {
            List<DataDatabaseModel> dos = baseHandler.getByIds(ids);
            if (dos != null && !dos.isEmpty()) {
                for (DataDatabaseModel entity : dos) {
                    dtoMap.put(entity.getId(), entity);
                }
            }
        }

        //组装信息
        for (DataHouseLayerModel layer : layers) {
            //第一层级
            DataHouseGetLayerVO layerVO = new DataHouseGetLayerVO();
            layerVO.setLabel(layer.getName());
            layerVO.setCtg("layer");
            layerVO.setNodeData(layer);
            //获取数据库层级信息
            for (DataHouseLayerDetailModel detailEntity : layerDetails) {
                //判断是否绑定数据源
                if (layer.getId().equals(detailEntity.getParentId()) && dtoMap.containsKey(detailEntity.getDatabaseId())) {
                    //第二层级
                    DataHouseGetLayerVO layerVO1 = new DataHouseGetLayerVO();
                    layerVO1.setCtg("db");
                    layerVO1.setProduct(detailEntity.getProduct());
                    layerVO1.setDatabaseId(detailEntity.getDatabaseId());
                    layerVO1.setNodeData(detailEntity);
                    //获取绑定的数据库表集合信息
                    List<TableBase> tables = dbBuilder.builderDdl(detailEntity.getDatabaseId()).getTable().getTables();
                    layerVO1.setLabel(String.format("%s[%s]", dtoMap.get(detailEntity.getDatabaseId()).getName(), detailEntity.getProduct()));

                    if (tables != null && !tables.isEmpty()) {
                        //第三层级
                        for (TableBase table : tables) {
                            DataHouseGetLayerVO layerVO2 = new DataHouseGetLayerVO();
                            layerVO2.setLabel(table.getTableName());
                            layerVO2.setCtg("table");
                            layerVO2.setProduct(detailEntity.getProduct());
                            layerVO2.setDatabaseId(detailEntity.getDatabaseId());
                            layerVO2.setNodeData(table);
                            layerVO1.getChildren().add(layerVO2);
                        }

                    }
                    layerVO.getChildren().add(layerVO1);
                }
            }
            layerTreeVO.getTreeData().add(layerVO);
        }
        return layerTreeVO;
    }

    /**
     * 获取可用的数据源信息
     *
     * @return
     */
    public List<DataHouseGetSourcesVO> getSourcesByType() {
        List<DataHouseGetSourcesVO> sourcesVOS = new ArrayList<>();

        List<DataDatabaseModel> databaseDto = baseHandler.getList("");
        if (databaseDto != null && !databaseDto.isEmpty()) {
            //获取绑定关系
            List<DataHouseLayerDetailModel> layerDetails = layerDetailHandler.getAll();
            List<Long> ids = new ArrayList<>();
            if (layerDetails != null && !layerDetails.isEmpty()) {
                ids = layerDetails.stream().map(DataHouseLayerDetailModel::getDatabaseId).collect(Collectors.toList());
            }

            for (DataDatabaseModel dto : databaseDto) {
                if (dto.getActive() == 1
                        && !ids.contains(dto.getId())
                        && Plugin.<SourcePlugin>getInstance(PluginType.SOURCE, dto.getId()).supportDataWarehouse()) {
                    //未绑定
                    sourcesVOS.add(DozerUtils.map(dto, DataHouseGetSourcesVO.class));
                }
            }
        }
        return sourcesVOS;
    }


    /**
     * 绑定数据源
     *
     * @param query
     * @return
     */
    public boolean saveLayerDetail(DataHouseBindLayerSourceQuery query) {
        return layerDetailHandler.saveLayerDetail(DozerUtils.map(query, DataHouseLayerDetailModel.class));
    }

    /**
     * 删除绑定关系
     *
     * @param id
     * @return
     */
    public boolean deleteLayerDetail(Long id) {
        return layerDetailHandler.deleteLayerDetail(id);
    }
}
