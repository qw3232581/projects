package com.taotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.web.service.IndexService;

@RequestMapping("index")
@Controller
public class IndexController {

    @Autowired
    private IndexService indexService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("index");

        // 首页大广告
        String indexAD1 = this.indexService.queryIndexAD1();
        mv.addObject("indexAD1", indexAD1);
        
        // 楼层商品广告数据
        String indexItemADList = this.indexService.queryIndexItemADList();
        mv.addObject("indexItemADList", indexItemADList);
        return mv;
    }

}
