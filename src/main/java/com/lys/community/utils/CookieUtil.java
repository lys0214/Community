package com.lys.community.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;

/**
 * cookie工具类
 */
public class CookieUtil {

    /**
     * 返回请求中指定名字的cookie值
     * @param request   请求
     * @param name  cookie的key
     * @return cookie的值
     */
    public static String getValue(HttpServletRequest request, String name) {
        if (request == null || name == null) {
            throw  new IllegalArgumentException("参数为空");
        }
    //    从request中取出所有的cookie值
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
