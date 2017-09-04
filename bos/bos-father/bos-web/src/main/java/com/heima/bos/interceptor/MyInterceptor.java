package com.heima.bos.interceptor;

import com.heima.bos.domain.user.User;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

@Component("myInterceptor")
public class MyInterceptor extends MethodFilterInterceptor {

    //拦截器跳转到登录页面
    @Override
    protected String doIntercept(ActionInvocation invocation) throws Exception {
        User loginUser = (User) ServletActionContext.getRequest().getSession().getAttribute("loginUser");
        if (loginUser == null) {
            return "no_login";
        } else {
            return invocation.invoke();
        }
    }
}