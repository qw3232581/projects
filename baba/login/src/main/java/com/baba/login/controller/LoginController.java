package com.baba.login.controller;

import com.baba.pojo.Buyer;
import com.baba.service.BuyerService;
import com.baba.service.SessionService;
import com.baba.utils.Encoding;
import com.baba.utils.Encryption;
import com.baba.utils.SessionTool;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jamayette
 *         Created on  2017/8/20
 */
@Controller
public class LoginController {

    @Autowired
    BuyerService buyerService;
    @Autowired
    SessionService sessionService;

    //显示登录页面
    @RequestMapping(value = "/login.aspx",method = RequestMethod.GET)
    public String showLogin(Model model,String returnUrl) {
        model.addAttribute("returnUrl",returnUrl);
        return "login";
    }

    ///执行登陆功能
    @RequestMapping(value = "/doLogin.aspx",method = RequestMethod.POST)
    public String showLogin(Model model,String username,String password,String returnUrl,
                            HttpServletResponse response,HttpServletRequest request) {

        // 如果有验证码，还需要判断验证码

        //用户名不为空时
        if (username!=null){
            if (password != null){
                // 开始查数据库检测用户名和密码是否正确
                Buyer buyer = buyerService.findByUsername(username);

                //当用户存在时
                if (buyer!= null){
                    // 开始判断密码
                    // 因为数据库中的密码已经是不可逆加密过后的密文，所以比较密码时先对用户传入的密码进行加密
                    if (buyer.getPassword().equals(Encryption.encrypt(password))){

                        // 将用户名保存到自定义session中(redis)
                        sessionService.addUsernameToRedis(SessionTool.getSessionID(request, response), username);

                        // 如果用户直接打开登录页面，则登录后返回首页
                        if (returnUrl == null || returnUrl.length() < 1) {
                            returnUrl = "http://localhost:8082/";
                        }

                        return "redirect:" + (Encoding.encodeGetRequest(returnUrl));
                    }else {
                        model.addAttribute("error","密码不正确");
                    }
                }else {
                    model.addAttribute("error","用户不存在");
                }
            }else {
                model.addAttribute("error","密码不能为空");
            }
        }else {
            model.addAttribute("error","用户名不能为空");
        }
        return "login";
    }

    // 判断用户是否登录
    @RequestMapping(value = "/isLogin.aspx")
    @ResponseBody
    public MappingJacksonValue isLogin(String callback,
                                       HttpServletRequest request, HttpServletResponse response) {

        // 根据babasessionid去redis上查找相应的用户名
        String username = sessionService.getUsernameFromRedis(SessionTool.getSessionID(request, response));

        // 将要传回给页面的数据，封装到callback的值为命名的js方法中
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(
                username);
        mappingJacksonValue.setJsonpFunction(callback);
        return mappingJacksonValue;
    }







}
