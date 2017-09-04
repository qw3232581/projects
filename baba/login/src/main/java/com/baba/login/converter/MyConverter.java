package com.baba.login.converter;

import org.springframework.core.convert.converter.Converter;

/**
 * @author Jamayette
 *         Created on  2017/8/20
 * 自定义转换器类
 */
public class MyConverter implements Converter<String,String> {

    @Override
    public String convert(String source) {
        // 去掉字符串前后空格
        try {
            if (source!=null){
                source.trim();
                if (!"".equals(source)){
                    return source;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
}
