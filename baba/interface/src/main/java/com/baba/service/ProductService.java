package com.baba.service;

import com.baba.pojo.Color;
import com.baba.pojo.Product;
import com.baba.pojo.SuperPojo;
import com.baba.utils.PageHelper;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author Jamayette
 *         Created on  2017/8/16
 */
@Service
public interface ProductService {

    PageHelper.Page<Product> findByExample(Product product, Integer pageNum, Integer pageSize);

    List<Color> findEnableColors();

    void addProduct(Product product);

    void updateProduct(Product product, String ids) throws IOException, SolrServerException;

    SuperPojo findProductById(Long productId);
}
