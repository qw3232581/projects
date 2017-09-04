package com.baba.dao;


import com.baba.pojo.Sku;
import com.baba.pojo.SuperPojo;
import com.github.abel533.mapper.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Jamayette
 *         Created on  2017/8/17
 */
@Repository
public interface SKUDAO extends Mapper<Sku> {

    List<Sku> selectSkuByProductID(Long productId);

    List<SuperPojo> selectSkuAndColorByProductId(@Param("productId") Long productId);

    SuperPojo findSkuAndProductBySkuId(@Param("skuId") Long skuId);
}
