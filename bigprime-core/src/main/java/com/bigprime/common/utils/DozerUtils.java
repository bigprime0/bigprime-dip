package com.bigprime.common.utils;


import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lyw
 * @version 1.0
 */
public class DozerUtils {

    private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    public static <T> T map(Object o,Class<T> tClass){
        return mapper.map(o, tClass);
    }

    public static <T> List<T> mapList(List<?> list, Class<T> tClass){
        return list.stream().map(o -> map(o, tClass)).collect(Collectors.toList());
    }

}
