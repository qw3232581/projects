package com.baba.product.service;

import com.baba.dao.SKUDAO;
import com.baba.pojo.Sku;
import com.baba.pojo.SuperPojo;
import com.baba.service.SkuService;
import com.github.abel533.entity.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Jamayette
 *         Created on  2017/8/17
 */
@Service("skuService")
@Transactional
public class SkuServiceImpl implements SkuService {

    @Autowired
    SKUDAO skuDao;

    @Override
    public List<Sku> findByProductId(Long productId) {
        return skuDao.selectSkuByProductID(productId);
    }

    @Override
    public void updateSku(Sku sku) {
        skuDao.updateByPrimaryKeySelective(sku);
    }

    @Override
    public SuperPojo findSkuAndProductBySkuId(Long skuId) {

        return skuDao.findSkuAndProductBySkuId(skuId);

    }
}
