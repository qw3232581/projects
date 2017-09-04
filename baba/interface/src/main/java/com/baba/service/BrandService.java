package com.baba.service;

import com.baba.pojo.Brand;
import com.baba.utils.PageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : Jamayette
 * Created on 2017/8/13
 */
@Service
public interface BrandService {

    PageHelper.Page<Brand> findByExample(Brand brand, Integer pageNum, Integer pageSize);

    Brand findById(Long id);

    void updateById(Brand brand);

    List<Brand> selectAllBrands();

    void deleteByIds(String ids);

    List<Brand> findAllFromRedis();
}
