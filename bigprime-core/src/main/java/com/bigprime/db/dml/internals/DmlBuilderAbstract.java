package com.bigprime.db.dml.internals;

import com.bigprime.common.constant.DataBaseEnum;
import lombok.Getter;
import org.springframework.boot.CommandLineRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lyw
 * @version 1.0
 */
@Getter
public abstract class DmlBuilderAbstract implements CommandLineRunner {
    /**
     * 查询实例
     */
    private SelectAbstract select;

    public DmlBuilderAbstract(SelectAbstract select){
        this.select = select;
    }

    public static Map<DataBaseEnum, Class<? extends DmlBuilderAbstract>> RELATION_MAPPER = new HashMap<>();

    protected static void setRelationMapper(DataBaseEnum dataBaseType, Class<? extends DmlBuilderAbstract> clazz){
        RELATION_MAPPER.put(dataBaseType, clazz);
    }

    public static Map<DataBaseEnum,Class<? extends DmlBuilderAbstract>> getRelationMapper(){
        return RELATION_MAPPER;
    }
}
