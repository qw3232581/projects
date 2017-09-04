package com.taotao.web.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.utils.CookieUtils;
import com.taotao.sso.bean.User;
import com.taotao.web.service.UserService;
import com.taotao.web.threadlocal.UserThreadLocal;

public class UserLoginHandlerInterceptor implements HandlerInterceptor {

    public static final String COOKIE_NAME = "TT_TOKEN";

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = CookieUtils.getCookieValue(request, COOKIE_NAME);
        String loginUrl = this.userService.TAOTAO_SSO_URL + "/user/login.html";
        if (StringUtils.isEmpty(token)) {
            // 处于未登录状态
            response.sendRedirect(loginUrl);
            return false;
        }

        User user = this.userService.queryUserByToken(token);
        if (null == user) {
            // 登录超时
            response.sendRedirect(loginUrl);
            return false;
        }

        // 处于登录状态
        UserThreadLocal.set(user);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) throws Exception {
        UserThreadLocal.set(null); // 清空当前线程中的User对象
    }

}
