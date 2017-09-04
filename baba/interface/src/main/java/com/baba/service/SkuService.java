package com.baba.service;

import com.baba.pojo.Sku;
import com.baba.pojo.SuperPojo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jamayette
 *         Created on  2017/8/17
 */
@Service
public interface SkuService {

    List<Sku> findByProductId(@Param("product_id") Long productId);

    void updateSku(Sku sku);

    SuperPojo findSkuAndProductBySkuId(Long skuId);
}

