package com.baba.cms.service.impl;

import com.baba.service.StaticPageService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.Map;

/**
 * 静态化服务器实现类
 * @author Jamayette
 *         Created on  2017/8/20
 */
@Service("staticPageService")
public class StaticPageServiceImpl implements StaticPageService{

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    private final ServletContext servletContext;

    @Autowired
    public StaticPageServiceImpl(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void staticProductPage(Map<String, Object> map, String id) throws IOException, TemplateException {
        //使用spring的Freemarker配置获得标准的Freemarker配置器
        Configuration configuration = freeMarkerConfigurer.getConfiguration();

        //生成的文件的位置
        String hPath = this.servletContext.getRealPath("/html/product/"+id+".html");

        // 获得最终文件的父文件（目录）
        File file = new File(hPath);
        File parentFile = file.getParentFile();

        // 如果父目录不存在，则进行创建
        if(!parentFile.exists()) {
            parentFile.mkdir();
        }

        //加载模版文件
        Template template = configuration.getTemplate("product.html");

        // 设置输出文件的位置
        //FileWriter fileWriter = new FileWriter(new File(hPath));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(hPath)), "UTF-8"));

        // 开始输出
        template.process(map, bufferedWriter);
    }


}
