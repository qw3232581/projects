package com.baba.utils;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具类
 * @author Jamayette
 *         Created on  2017/8/20
 */
public class Encryption {
    /**
     * 对字符串进行加密
     */
    public static String encrypt(String str) {
        String pwd = null;
        try {

            // 如有需要还可以对密码进行加盐
            // str = "yfy67v4a7vghfe" + str + "eruweyr786f";

            // 使用md5加密方式
            MessageDigest instance = MessageDigest.getInstance("md5");
            // 开始加密，参数需要将字符串转成字节数组，返回的也是字节数组
            byte[] digest = instance.digest(str.getBytes());

            // 将字节数组转成十六进行字符串
            char[] encodeHex = Hex.encodeHex(digest);
            pwd = new String(encodeHex);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return pwd;
    }

}
