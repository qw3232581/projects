package com.baba.utils;

import java.io.UnsupportedEncodingException;

/**
 * 编码工具类
 * @author Jamayette
 */
public class Encoding {

    /**
     * 进行get方式编码处理
     * @param str
     * @return
     */
    public static String encodeGetRequest(String str) {
        try {
            if (str != null) {
                return new String(
                        str.getBytes("ISO8859-1"), "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }
}
