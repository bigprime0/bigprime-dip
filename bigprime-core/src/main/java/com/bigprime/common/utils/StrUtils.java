package com.bigprime.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.id.NanoId;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author lyw
 * @version 1.0
 */
public final class StrUtils {

    /**
     * 字符串分割
     *
     * @param str
     * @return
     */
    public static List<String> strToList(String str) {
        if (!str.isEmpty()) {
            String[] stirs = str.split(",");
            if (stirs.length > 0) {
                return new ArrayList<>(Arrays.asList(stirs));
            }
        }
        return new ArrayList<>();
    }

    /**
     * 二进制转换字符串
     *
     * @param bytes
     * @return
     */
    public static String byteToString(byte[] bytes) {
        if (null == bytes || bytes.length == 0) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            int v = b & 0xFF;
            String s = Integer.toHexString(v);
            if (s.length() < 2) {
                sb.append(0);
            }
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * 判断为空或null
     *
     * @param input
     * @return
     */
    public static boolean IsNullOrEmpty(String input) {
        return StrUtil.isEmpty(input) || StrUtil.isBlank(input) || ObjectUtil.isNull(input);
    }

    public static int getStrCount(String str, String subStr) {
        int count = 0;
        int fromIndex = 0;

        while ((fromIndex = str.indexOf(subStr, fromIndex)) != -1) {
            count++;
            fromIndex += subStr.length(); // 移动到子字符串的下一个字符开始的位置
        }
        return count;
    }

    /**
     * 获取纳米ID
     * @param len 长度
     * @return
     */
    public static String GenNanoId(int len) {
        String genBaseChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return NanoId.randomNanoId(new Random(), RandomUtil.BASE_NUMBER.toCharArray(), len);
    }

    /**
     * 获取默认长度(20)的纳米ID
     * @return
     */
    public static String GenNanoId() {
        return GenNanoId(20);
    }

    public static String listConvertStr(List<Long> params){
        String str = null;
        if (params != null && params.size() > 0) {
            str = params.stream().map(Object::toString).collect(Collectors.joining(","));
        }
        return str;
    }

    public static List<Long> strConvertList(String str){
        List<Long> list = new ArrayList<>();
        if (!StrUtil.isBlank(str)) {
            list = CollUtil.newArrayList(str.split(",")).stream().map(Long::parseLong).collect(Collectors.toList());
        }
        return list;
    }
}
