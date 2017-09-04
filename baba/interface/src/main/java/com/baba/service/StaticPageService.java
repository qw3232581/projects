package com.baba.service;

import freemarker.template.TemplateException;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.Map;

/**
 * 静态化页面服务接口
 * @author Jamayette
 *         Created on  2017/8/20
 */
@Service
public interface StaticPageService {


    /**
     * 静态化商品详情页面
     * @throws IOException
     */
    void staticProductPage(Map<String, Object> map, String id) throws IOException, TemplateException;

}
