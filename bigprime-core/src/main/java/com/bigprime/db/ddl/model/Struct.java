package com.bigprime.db.ddl.model;

import com.bigprime.annotation.FieldDescription;
import com.bigprime.common.constant.FieldControlEnum;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author lyw
 * @version 1.0
 */
@Data
public class Struct {
    /**
     * 字段标识 如:Name
     */
    private String key;

    /**
     * 字段名称 如:姓名
     */
    private String label;

    /**
     * 字段控件类型
     */
    private FieldControlEnum fieldControl;

    /**
     * 下拉列表选项值 k-v
     */
    private Map<String,String> listValues = new HashMap<>();

    /**
     * 是否能修改
     */
    private Boolean canUpdate;

    /**
     * 是否补充属性
     */
    private Boolean isProperties;

    /**
     * 是否显示
     */
    private Boolean isShow;

    /**
     * 排序
     */
    private int sort;


    private static Map<String, List<Struct>> STRUCT_MAPPER = new HashMap<>();

    /**
     * 获取类结构信息
     * @param clazz
     * @return
     */
    public static List<Struct> getStruct(Class clazz){
        if(clazz == null){
            return new ArrayList<>();
        }
        String clazzName = clazz.getName();
        if(STRUCT_MAPPER.containsKey(clazzName)){
            return STRUCT_MAPPER.get(clazzName);
        }
        List<Struct> structs = new ArrayList<>();
        Map<String,Map<String,String>> map = new HashMap<>();
        while(clazz != null){
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(FieldDescription.class)) {
                    FieldDescription description = field.getAnnotation(FieldDescription.class);
                    Struct struct = new Struct();
                    struct.setKey(field.getName());
                    struct.setLabel(description.description());
                    struct.setCanUpdate(description.canUpdate());
                    struct.setIsProperties(description.isProperties());
                    struct.setIsShow(description.isShow());
                    struct.setFieldControl(description.fieldControl());
                    if(struct.getFieldControl() == FieldControlEnum.LIST){
                        try {
                            Class c = description.type();
                            if(map.containsKey(c.getName())){
                                struct.setListValues(map.get(c.getName()));
                            }else{
                                Map<String,String> listValues = (Map<String,String>)c.getMethod("GetTypes").invoke(c.getDeclaredConstructor().newInstance());
                                struct.setListValues(listValues);
                                map.put(c.getName(), listValues);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    struct.setSort(description.sort());
                    structs.add(struct);
                }
            }
            clazz = clazz.getSuperclass();
        }
        Collections.sort(structs, Comparator.comparingInt(Struct::getSort));
        STRUCT_MAPPER.put(clazzName, structs);
        return structs;
    }
}
