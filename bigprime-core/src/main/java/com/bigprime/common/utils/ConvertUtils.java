package com.bigprime.common.utils;

import cn.hutool.core.date.DateUtil;
import com.bigprime.common.exception.ServerException;

import java.util.Date;

/**
 * @author lyw
 * @version 1.0
 */
public class ConvertUtils {

    public static <T> T ConvertType(String str, Class<T> clazz) {
        if (clazz == String.class) {
            return (T) str;
        } else if (clazz == Integer.class || clazz == int.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == Long.class || clazz == long.class) {
            return (T) Long.valueOf(str);
        } else if (clazz == Double.class || clazz == double.class) {
            return (T) Double.valueOf(str);
        } else if (clazz == Float.class || clazz == float.class) {
            return (T) Float.valueOf(str);
        } else if (clazz == Boolean.class || clazz == boolean.class) {
            return (T) Boolean.valueOf(str);
        } else if (clazz == Byte.class || clazz == byte.class) {
            return (T) Byte.valueOf(str);
        } else if (clazz == Date.class) {
            try {
                return (T) DateUtil.date(Long.parseLong(str));
            } catch (NumberFormatException e) {
                try {
                    return (T) DateUtil.parse(str);
                } catch (Exception ex) {
                    return null;
                }
            }
        }
        return null;
    }
}
