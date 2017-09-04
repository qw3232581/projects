package com.taotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.manage.pojo.ItemDesc;
import com.taotao.web.bean.Item;
import com.taotao.web.service.ItemService;

@RequestMapping("item")
@Controller
public class ItemController {
    
    @Autowired
    private ItemService itemService;

    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ModelAndView itemDetail(@PathVariable("itemId") Long itemId) {
        ModelAndView mv = new ModelAndView("item");
        // 商品的基本数据
        Item item = this.itemService.queryByItemId(itemId);
        mv.addObject("item", item);
        
        // 商品的描述数据
        ItemDesc itemDesc = this.itemService.queryItemDescByItemId(itemId);
        mv.addObject("itemDesc", itemDesc);
        
        // 商品的规格参数数据
        String itemParam = this.itemService.queryItemParamItemByItemId(itemId);
        mv.addObject("itemParam", itemParam);
        return mv;
    }
}
