package com.baba.portal.controller;

import com.baba.pojo.Brand;
import com.baba.pojo.Product;
import com.baba.pojo.Sku;
import com.baba.pojo.SuperPojo;
import com.baba.service.BrandService;
import com.baba.service.ProductService;
import com.baba.service.SolrService;
import com.baba.utils.Encoding;
import com.baba.utils.PageHelper;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.rmi.runtime.Log;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * 前台首页控制中心
 * @author Jamayette
 *         Created on  2017/8/16
 */
@Controller
public class IndexController {

    @Autowired
    private SolrService solrService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductService productService;

    //显示前台首页
    @RequestMapping(value="/")
    public String showIndex() {
        return "index";
    }

    // 前台首页搜索功能
    @RequestMapping(value = "/searchProduct")
    public String indexSearch(Model model, String keyword,String sort
    ,Integer pageNum,Integer pageSize,Long brandId, Float pa, Float pb) throws SolrServerException {

        keyword = Encoding.encodeGetRequest(keyword);

        PageHelper.Page<SuperPojo> page =
                solrService.findProductByKeyWord(keyword,sort,pageNum,pageSize,brandId, pa, pb);

        //查询出的商品信息
        model.addAttribute("page", page);

        //回显查询的关键字
        model.addAttribute("keyword", keyword);

        // 回传用户选择的品牌id
        model.addAttribute("brandId", brandId);

        // 回传用户选择的价格
        model.addAttribute("pa", pa);
        model.addAttribute("pb", pb);

        // 从redis中查询出品牌，并传递到页面
        List<Brand> brands = brandService.findAllFromRedis();
        model.addAttribute("brands", brands);

        // 构建已选条件的map
        TreeMap<String, String> map = new TreeMap<>();

        if (brandId != null) {
            // 根据品牌id 获得品牌名称
            for (Brand brand : brands) {
                //注意写等号会有问题
                if (brandId.equals(brand.getId())) {
                    map.put("品牌", brand.getName());
                    break;
                }
            }
        }

        // 价格
        if (pa != null && pb != null) {
            if (pb == -1) {
                map.put("价格", pa + "以上");
            } else {
                map.put("价格", pa + "-" + pb);
            }
        }

        // 回显已选条件
        model.addAttribute("map", map);

        //将之前的排序信息传回页面
        model.addAttribute("sort2",sort);

        //反转排序规则
        if (sort != null && sort.equals("price asc")){
            sort = "price desc";
        }else {
            sort = "price asc";
        }

        model.addAttribute("sort",sort);

        return "search";
    }

    // 商品详情展示功能
    @RequestMapping(value = "/product/showProductDetail")
    public String showProductDetail(Long productId,Model model)   {

        SuperPojo superPojo = productService.findProductById(productId);

        List skus = (List) superPojo.get("skus");

        // 去除颜色重复
        HashMap<Long, String> colors = new HashMap<>();
        for (Object object : skus) {
            SuperPojo sku = (SuperPojo) object;
            // 将颜色添加到hm集合中，利用hm集合来去除重复   key是颜色的id  value是颜色名称
            colors.put((Long) sku.get("color_id"), (String) sku.get("name"));
        }

        // 万能实体对象要被传递，将非重复的颜色对象也通过superPojo顺便传递到页面
        superPojo.setProperty("colors", colors);

        model.addAttribute(superPojo);

        return "product";
    }

}
