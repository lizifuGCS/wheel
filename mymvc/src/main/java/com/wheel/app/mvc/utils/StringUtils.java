package com.wheel.app.mvc.utils;

/**
 * @Description
 * @Author zifu.li@hand-china.com
 * @Date 2020/8/9 13:00
 * @Version 1.0
 */
public class StringUtils {

    /**
     * 把字符串的首字母小写
     *
     * @param name
     * @return
     */
    public static String toLowerFirstWord(String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }
}
