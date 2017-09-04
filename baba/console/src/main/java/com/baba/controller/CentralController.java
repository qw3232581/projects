package com.baba.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 后台管理 控制中心
 * @author Jamayette
 *
 */
@Controller
public class CentralController {

    /**
     * 将用户输入的url路径，取出关键部分，直接转发到指定jsp页面
     */

    // 总
    @RequestMapping(value = "console/{pageName}.do")
    public String consoleShow(@PathVariable(value = "pageName") String pageName) {
        return pageName;
    }

    // 框架页面
    @RequestMapping(value = "console/frame/{pageName}.do")
    public String consoleFrameShow(@PathVariable(value = "pageName") String pageName) {
        return "/frame/" + pageName;
    }

}