package com.baba.portal.interceptor;

import com.baba.service.SessionService;
import com.baba.utils.SessionTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jamayette
 *         Created on  2017/8/23
 */
public class MyInterceptor implements HandlerInterceptor{

    @Autowired
    SessionService sessionService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //判断登陆
        String username = sessionService.getUsernameFromRedis(SessionTool.getSessionID(request, response));


        if (username == null){
            response.sendRedirect("http://localhost:8081/login.aspx?returnUrl=http://localhost:8082/cart");
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
