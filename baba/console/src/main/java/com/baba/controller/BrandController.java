package com.baba.controller;

import com.baba.pojo.Brand;
import com.baba.service.BrandService;
import com.baba.utils.Encoding;
import com.baba.utils.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 品牌管理控制中心
 * @author Jamayette
 * Created on  2017/8/13
 */
@Controller
public class BrandController {

    @Autowired
    private BrandService brandService;

    //品牌 通用映射，当有具体映射时候，会执行具体映射方法
    @RequestMapping(value = "console/brand/{pageName}.do")
    public String consoleBrandShow(
            @PathVariable(value = "pageName") String pageName) {
        return "/brand/" + pageName;
    }

    //条件查询品牌 具体映射
    @RequestMapping(value = "console/brand/list.do")
    public String consoleBrandShow(Model model,
                                   String name,
                                   Integer isDisplay,
                                   Integer pageNum,
                                   Integer pageSize) {

        // 设置查询条件
        Brand brand = new Brand();
        brand.setName(Encoding.encodeGetRequest(name));
        brand.setIsDisplay(isDisplay);

        PageHelper.Page<Brand> pageBrand = brandService.findByExample(brand, pageNum, pageSize);

        // 将查询出来的品牌集合传递给页面
        model.addAttribute("pageBrand",pageBrand);

        // 设置查询数据回显之将查询数据传回给页面
        model.addAttribute("name", Encoding.encodeGetRequest(name));
        model.addAttribute("isDisplay", isDisplay);

        return "/brand/list";
    }


    //修改品牌
    @RequestMapping(value = "console/brand/showEdit.do")
    public String consoleBrandShowEdit(Long brandId, Model model) {

        //获得要修改的品牌
        Brand brand = brandService.findById(brandId);
        // 设置修改的数据回显
        model.addAttribute("brand", brand);
        return "/brand/edit";
    }

    // 执行品牌修改
    @RequestMapping(value = "console/brand/doEdit.do")
    public String consoleBrandDoEdit(Brand brand) {

        // 根据id来执行修改
        brandService.updateById(brand);
        // 重定向到显示品牌列表功能页面
        return "redirect:/console/brand/list.do";

    }

    // 执行品牌删除
    @RequestMapping(value = "console/brand/doDelete.do")
    public String consoleBrandDoDelete(String ids,
    @RequestParam("name")String name,@RequestParam("isDisplay")Integer isDispaly,
                                       @RequestParam("pageNum")Integer pageNum) {

        System.out.println(ids);
        // 根据id来执行删除
        //brandService.deleteByIds(ids);

        // 重定向到显示品牌列表功能页面
        return "redirect:/console/brand/list.do?name="+name+"&isDispaly="+isDispaly
                +"&pageNum="+pageNum+"&pageSize=10";

    }

}
