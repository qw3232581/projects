package com.baba.controller;

import com.baba.pojo.Sku;
import com.baba.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Jamayette
 *         Created on  2017/8/17
 */
@Controller
public class SKUController {

    @Autowired
    private SkuService skuService;

    // 显示某商品的库存列表（具体映射）
    @RequestMapping(value = "console/sku/list.do")
    public String consoleSkuShowList(Model model, Long productId) {

        List<Sku> skus = skuService.findByProductId(productId);

        System.out.println("库存数量："+skus.size());
        model.addAttribute("skus", skus);



        return "/sku/list";
    }

    @RequestMapping(value = "console/sku/skuUpdate.do")
    @ResponseBody
    public String consoleSkuDoUpdate(Model model, Sku sku) {

        try {
            skuService.updateSku(sku);
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }
        return "success";
    }

}
