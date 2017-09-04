package com.baba.service;

import com.baba.pojo.SuperPojo;
import com.baba.utils.PageHelper;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * solr服务类
 * @author Jamayette
 *         Created on  2017/8/18
 */
@Service
public interface SolrService {

    PageHelper.Page<SuperPojo> findProductByKeyWord(
            String keyword, String sort, Integer pageNum, Integer pageSize,Long brandId, Float pa, Float pb)
            throws SolrServerException;

    void addProduct(String ids) throws SolrServerException, IOException;
}
