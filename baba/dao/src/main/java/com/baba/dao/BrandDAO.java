package com.baba.dao;

import com.baba.pojo.Brand;
import com.github.abel533.mapper.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 品牌管理DAO
 * @author Jamayette
 * Created on 2017/8/13
 */
@Repository
public interface BrandDAO extends Mapper<Brand>{
    void deleteByIds(String ids);

//    List<Brand> findByExample(Brand brand);
//
//    Brand findById(Long id);
//
//    void updateById(Brand brand);

}

