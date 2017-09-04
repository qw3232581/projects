package com.baba.controller;

import com.baba.pojo.Brand;
import com.baba.pojo.Color;
import com.baba.pojo.Product;
import com.baba.service.BrandService;
import com.baba.service.ProductService;
import com.baba.utils.Encoding;
import com.baba.utils.PageHelper;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

/**
 * 商品管理控制中心
 * @author Jamayette
 *         Created on  2017/8/15
 */
@Controller
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;

    //商品 通用映射
    @RequestMapping(value = "console/product/{pageName}.do")
    public String consoleProductJump(
            @PathVariable(value = "pageName") String pageName) {
        return "/product/" + pageName;
    }

    //商品 具体映射
    @RequestMapping(value = "console/product/list.do")
    public String consoleProductShow(Model model,String name, Long brandId,
                                     Integer isShow, Integer pageNum, Integer pageSize) {
        Product product = new Product();

        String encodeName = Encoding.encodeGetRequest(name);
        product.setName(encodeName);

        PageHelper.Page<Product> pageProduct = productService.findByExample(product,pageNum,pageSize);
        List<Brand> brands = brandService.selectAllBrands();

        model.addAttribute("pageProduct",pageProduct);
        model.addAttribute("brands",brands);
        model.addAttribute("name",Encoding.encodeGetRequest(name));
        model.addAttribute("isShow",isShow);

        return "/product/list";
    }

    //商品添加
    @RequestMapping(value = "console/product/showAdd.do")
    public String consoleShowProductAdd(Model model) {
        //加载所有可用的颜色
        List<Color> colors = productService.findEnableColors();
        List<Brand> brands = brandService.selectAllBrands();
        model.addAttribute("brands",brands);
        model.addAttribute("colors",colors);
        return "/product/add";

    }
    @RequestMapping(value = "console/product/doAdd.do")
    public String consoleDoProductAdd(Model model,Product product) {

        productService.addProduct(product);
        return "redirect:/console/product/list.do";
    }

    @RequestMapping(value = "console/product/onShelf.do")
    public String consoleDoOnShelf(Model model,String ids) {

        Product product = new Product();
        product.setIsShow(1);

        try {
            productService.updateProduct(product,ids);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }

        return "redirect:/console/product/list.do";
    }




}
