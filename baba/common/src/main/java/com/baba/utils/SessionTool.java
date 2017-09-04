package com.baba.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 自定义session工具类 主要是分配babasessionid
 * @author Jamayette
 *         Created on  2017/8/20
 */
public class SessionTool {

    /**
     * 获得自定义sessionID，并在初次访问时，分配babasessionid，
     * 并将babasessionid写入到浏览器的cookie中
     */
    public static String getSessionID(HttpServletRequest request, HttpServletResponse response){
        // 1、从cookie中取出babasessionid
        Cookie[] cookies = request.getCookies();
        if (cookies!=null && cookies.length>0){
            // 判断cookie中是否有babasessionid
            for (Cookie cookie : cookies) {
                if ("babasessionid".equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }

        // 如果没有maosessionid，则重新为该浏览器创建一个
        String babasessionid = UUID.randomUUID().toString().replaceAll("-", "");
        Cookie cookie = new Cookie("babasessionid", babasessionid);
        // 设置cookie存活时间
        cookie.setMaxAge(-1);
        // 设置路径 如果不设置，端口后面的目录名称（xxx:8080/login）会对cookie存储照成影响
        cookie.setPath("/");

        // 设置二级跨域，由于本次课程中都是localhost，所有无需设置，但是项目正式上限后需要设置该项
        // cookie.setDomain(".babasport.com");

        // 把cookie写回浏览器客户端
        response.addCookie(cookie);

        return babasessionid;
    }


}
