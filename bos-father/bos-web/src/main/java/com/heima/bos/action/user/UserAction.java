package com.heima.bos.action.user;

import com.heima.bos.base.BaseAction;
import com.heima.bos.domain.user.User;
import com.heima.bos.utils.RandStringUtils;
import com.opensymphony.xwork2.interceptor.annotations.InputConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.common.security.UsernameToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;

import java.util.concurrent.TimeUnit;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("bos")
public class UserAction extends BaseAction<User> {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    @Qualifier("jmsQueueTemplate")
    private JmsTemplate jmsTemplate;

    //校验验证码
    @Action(value = "userAction_validateCheckcode", results = {
            @Result(name = "validateCheckcode", type = "json")})
    public String validateCheckcode() {
        String userCheckCode = getParameter("checkcode");
        String sysCheckCode = (String) getSessionAttribute("key");
        if (userCheckCode.equalsIgnoreCase(sysCheckCode)) {
            push(true);
        } else {
            push(false);
        }
        return "validateCheckcode";
    }

    //用户登录
    @Action(value = "userAction_login",
            results = {@Result(name = "login_success", type = "redirect", location = "/index.jsp"),
                    @Result(name = "login_error", location = "/login.jsp"),
                    @Result(name = "login_exception", location = "/login.jsp")})
    @InputConfig(resultName = "login_exception")
    public String login() {
        removeSessionAttribute("key");

//        User loginUser = facadeService.getUserService().login(model.getEmail(), model.getPassword());
//
//        if (loginUser == null) {
//            this.addActionError(getText("login.error.WrongEmailOrPassword"));
//            return "login_error";
//        } else {
//            setSessionAttribute("loginUser", loginUser);
//        }
//        return "login_success";

        Subject subject = SecurityUtils.getSubject();

        try {
            subject.login(new UsernamePasswordToken(model.getEmail(),model.getPassword()));
            return "login_success";
        } catch (AuthenticationException e) {
            e.printStackTrace();
            this.addActionError(getText("login.error.WrongEmailOrPassword"));
            return "login_error";
        }

    }

    //用户退出
    @Action(value = "userAction_logout",
            results = {@Result(name = "logout", type = "redirect", location = "/login.jsp")})
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "logout";
    }

    //发送手机验证码
    @Action(value = "userAction_sendValidationCode",
            results = {@Result(name = "sendValidationCode", type = "json")})
    public String sendValidationCode() {
        try {
            final String code = RandStringUtils.getCode();
            redisTemplate.opsForValue().set(model.getTelephone(), code, 120, TimeUnit.SECONDS);
            System.out.println("找回密码:" + code);
//			jmsTemplate.send("sms_validationCode",new MessageCreator() {
//				@Override
//				public Message createMessage(Session session) throws JMSException {
//					MapMessage mapMessage = session.createMapMessage();
//					mapMessage.setString("telephone", model.getTelephone());
//					mapMessage.setString("msg", code);
//					return mapMessage;
//				}
//			});
            push(true);
        } catch (JmsException e) {
            push(false);
            e.printStackTrace();
        }
        return "sendValidationCode";
    }

    //确认手机验证码 0验证码失效 1验证码错误 2手机号错误 3正确
    @Action(value = "userAction_smsPassword",
            results = {@Result(name = "smsPassword", type = "json")})
    public String smsPassword() {
        User existUser = facadeService.getUserService().findUserByTelephone(model.getTelephone());
        if (existUser == null) {
            push("2");
        } else {
            String checkcode = getParameter("checkcode");
            String rediscode = redisTemplate.opsForValue().get(model.getTelephone());
            if (StringUtils.isNotBlank(rediscode)) {
                if (rediscode.equals(checkcode)) {
                    push("3");
                } else {
                    push("1");
                }
            } else {
                push("0");
            }
        }
        return "smsPassword";
    }

    //修改密码
    @Action(value = "userAction_newPassword",
            results = {@Result(name = "newPassword", type = "json")})
    public String newPassword() {
        String newPassword = this.getParameter("newPassword");
        try {
            facadeService.getUserService().updatePasswordByTelephone(newPassword, model.getTelephone());
            push(true);
        } catch (Exception e) {
            push(false);
            e.printStackTrace();
        }
        return "newPassword";
    }

    @Action(value = "userAction_changePasswordWhenLoggedIn",
            results = {@Result(name = "changePasswordWhenLoggedIn", location = "/WEB-INF/pages/common/index.jsp")})
    public String changePasswordWhenLoggedIn() {
        String newPassword = this.getParameter("newPassword");
//        try {
//            User loginUser = (User) getSessionAttribute("loginUser");
//            facadeService.getUserService().changePasswordWhenLoggedIn(newPassword, loginUser.getEmail());
//            push(true);
//        } catch (Exception e) {
//            push(false);
//            e.printStackTrace();
//        }

        return "changePasswordWhenLoggedIn";
    }

}
