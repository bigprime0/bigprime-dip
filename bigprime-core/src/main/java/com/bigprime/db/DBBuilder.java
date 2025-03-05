package com.bigprime.db;


import com.bigprime.common.config.NacosConfig;
import com.bigprime.common.constant.DataBaseEnum;
import com.bigprime.common.constant.DataStructEnum;
import com.bigprime.db.ddl.internals.DdlBuilderAbstract;
import com.bigprime.db.ddl.model.Struct;
import com.bigprime.db.dml.internals.DmlBuilderAbstract;
import com.bigprime.handler.database.DataBaseHandler;
import com.bigprime.handler.database.model.DataDatabaseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lyw
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class DBBuilder implements ApplicationContextAware {
    private final DataBaseHandler baseHandler;
    private final NacosConfig config;

    private static Map<DataBaseEnum,Map<DataStructEnum,List<Struct>>> STRUCT_MAPPER = new HashMap<>();

    /**
     * 获取数据库结构信息
     * @param type
     * @return
     */
    public Map<DataStructEnum,List<Struct>> getDataBaseStruct(DataBaseEnum type){
        if(STRUCT_MAPPER.containsKey(type)){
            return STRUCT_MAPPER.get(type);
        }
        DdlBuilderAbstract ddl = getDdlInstance(null, type);
        Map<DataStructEnum,List<Struct>> map = new HashMap<>();
        map.put(DataStructEnum.TABLE, ddl.getTable().tableStructs());
        map.put(DataStructEnum.COLUMN, ddl.getColumn().columnStructs());
        map.put(DataStructEnum.INDEX, ddl.getIndex().indexStructs());
        STRUCT_MAPPER.put(type, map);
        return map;
    }

    /**
     * 获取支持的数据库类型索引
     * @return
     */
    public List<String> getDataBaseIndexes(){
        List<String> list = DdlBuilderAbstract.getRelationMapper().keySet().stream().map(DataBaseEnum::getProduct).collect(Collectors.toList());
        return list;
    }

    /**
     * 获取支持的数据库类型
     * @return
     */
    public List<DataBaseEnum> getDataBaseEnums(){
        List<DataBaseEnum> list = new ArrayList<>();
        list.addAll(DdlBuilderAbstract.getRelationMapper().keySet());
        return list;
    }


    /**
     * 构建数据库操作
     * @param id
     * @return
     */
    public DdlBuilderAbstract builderDdl(Long id){
        DataDatabaseModel dataBase = getDataBase(id);
        DataBaseEnum type = DataBaseEnum.getByProduct(dataBase.getProduct());
        return getDdlInstance(dataBase, type);
    }

    /**
     * 获取ddl实例
     * @param dataBase
     * @param type
     * @return
     */
    private DdlBuilderAbstract getDdlInstance(DataDatabaseModel dataBase,DataBaseEnum type) {
        if (DdlBuilderAbstract.getRelationMapper().containsKey(type)) {
            try {
                DdlBuilderAbstract ddl = applicationContext.getBean(DdlBuilderAbstract.getRelationMapper().get(type));
                ddl.getTable().setDataBase(dataBase);
                ddl.getColumn().setDataBase(dataBase);
                ddl.getIndex().setDataBase(dataBase);
                return ddl;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException(String.format("unknown database type (%s)", type.name()));
    }

    /**
     * 获取dml实例
     * @param id
     * @return
     */
    public DmlBuilderAbstract builderDml(Long id) {
        DataDatabaseModel dataBase = getDataBase(id);
        DataBaseEnum type = DataBaseEnum.getByProduct(dataBase.getProduct());
        if (DmlBuilderAbstract.getRelationMapper().containsKey(type)) {
            try {
                DmlBuilderAbstract dml = applicationContext.getBean(DmlBuilderAbstract.getRelationMapper().get(type));
                dml.getSelect().setDataBase(dataBase);
                return dml;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException(String.format("unknown database type (%s)", type.name()));
    }

    /**
     * 获取数据库信息
     * @param id
     * @return
     */
    private DataDatabaseModel getDataBase(Long id){
        DataDatabaseModel dataBase = null;
        if(id == -1){
            dataBase = config.getDataBaseModel();
        }else{
            dataBase = baseHandler.getById(id);
        }
        if(dataBase == null){
            throw new RuntimeException(String.format("unknown database id (%s)", id));
        }
        return dataBase;
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
