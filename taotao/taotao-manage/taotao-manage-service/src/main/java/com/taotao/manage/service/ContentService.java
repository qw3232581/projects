package com.taotao.manage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.mapper.ContentMapper;
import com.taotao.manage.pojo.Content;

/**
 * 内容的处理逻辑
 */
@Service
public class ContentService extends BaseService<Content> {

    @Autowired
    private ContentMapper contentMapper;

    public EasyUIResult queryContentList(Long categoryId, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        Example example = new Example(Content.class);
        example.setOrderByClause("updated DESC");
        example.createCriteria().andEqualTo("categoryId", categoryId);
        List<Content> list = this.contentMapper.selectByExample(example);
        PageInfo<Content> pageInfo = new PageInfo<Content>(list);
        return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
    }

}
