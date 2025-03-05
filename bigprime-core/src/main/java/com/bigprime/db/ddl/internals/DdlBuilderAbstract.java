package com.bigprime.db.ddl.internals;

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
public abstract class DdlBuilderAbstract implements CommandLineRunner {

    private static Map<DataBaseEnum,Class<? extends DdlBuilderAbstract>> RELATION_MAPPER = new HashMap<>();

    /**
     * 表实例
     */
    private TableAbstract table;

    /**
     * 列实例
     */
    private ColumnAbstract column;

    /**
     * 索引实例
     */
    private IndexAbstract index;


    public DdlBuilderAbstract(TableAbstract table,ColumnAbstract column,IndexAbstract index){
        this.table = table;
        this.column = column;
        this.index = index;
    }

    protected static void setRelationMapper(DataBaseEnum dataBaseType, Class<? extends DdlBuilderAbstract> clazz){
        RELATION_MAPPER.put(dataBaseType, clazz);
    }


    public static Map<DataBaseEnum,Class<? extends DdlBuilderAbstract>> getRelationMapper(){
        return RELATION_MAPPER;
    }
}
