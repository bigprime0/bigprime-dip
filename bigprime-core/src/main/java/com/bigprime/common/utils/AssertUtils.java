package com.bigprime.common.utils;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.bigprime.common.exception.ServerException;

/**
 * @author lyw
 * @version 1.0
 */
public class AssertUtils {
    public static void isBlank(String str, String variable) {
        if (StrUtil.isBlank(str)) {
            throw new ServerException(variable + "不能为空");
        }
    }

    public static void isNull(Object object, String variable) {
        if (object == null) {
            throw new ServerException(variable + "不能为空");
        }
    }

    public static void isArrayEmpty(Object[] array, String variable) {
        if(ArrayUtil.isEmpty(array)){
            throw new ServerException(variable + "不能为空");
        }
    }
}
