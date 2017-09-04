package com.baba.product.service;

import com.baba.dao.BrandDAO;
import com.baba.pojo.Brand;
import com.baba.service.BrandService;
import com.baba.utils.PageHelper;
import com.github.abel533.entity.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 品牌服务类
 * @author Jamayette
 * Created on  2017/8/13
 */
@Service("brandService")
@Transactional
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandDAO brandDAO;
    @Autowired
    private Jedis jedis;


    @Override
    /**
    * @author Jamayette 2017/8/14
    * @method findByExample
    * @param [brand, pageNum, pageSize]
    * @return com.baba.utils.PageHelper.Page
    * @description 分页条件查询品牌
    */
    public PageHelper.Page<Brand> findByExample(Brand brand, Integer pageNum, Integer pageSize) {
        System.out.println("------------------------");
        System.out.println(brand);

        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        if (brand.getName()!=null){
            criteria.andLike("name","%"+brand.getName()+"%");
        }
        if (brand.getIsDisplay()!=null){
            criteria.andEqualTo("isDisplay",brand.getIsDisplay());
        }

        PageHelper.startPage(pageNum,pageSize);
  //      brandDAO.findByExample(brand);
        brandDAO.selectByExample(example);
        PageHelper.Page<Brand> endPage = PageHelper.endPage();
        return endPage;

    }

    @Override
    /** 
    * @author Jamayette 2017/8/14 
    * @method findById 
    * @param [id]
    * @return com.baba.pojo.Brand 
    * @description 根据ID查找品牌 
    */
    public Brand findById(Long id) {
        return brandDAO.selectByPrimaryKey(id);
//        return brandDAO.findById(id);
    }

    @Override
    /**
    * @author Jamayette 2017/8/14
    * @method updateById
    * @param [brand]
    * @return void
    * @description 根据ID修改品牌
    */
    public void updateById(Brand brand) {
        // 将品牌信息同步修改redis
        jedis.hset("brand", String.valueOf(brand.getId()), brand.getName());

        brandDAO.updateByPrimaryKey(brand);
//        brandDAO.updateById(brand);
    }

    @Override
    /** 
    * @author Jamayette 2017/8/17 
    * @method selectAllBrands 
    * @param []
    * @return java.util.List<com.baba.pojo.Brand> 
    * @description 选择全部品牌 
    */
    public List<Brand> selectAllBrands() {
        List<Brand> brands = brandDAO.select(null);
        return brands;
    }

    @Override
    /** 
    * @author Jamayette 2017/8/17 
    * @method deleteByIds 
    * @param [ids]
    * @return void 
    * @description 根据id批量删除 
    */
    public void deleteByIds(String ids) {
        brandDAO.deleteByIds(ids);
    }

    @Override
    /** 
    * @author Jamayette 2017/8/18 
    * @method findAllFromRedis 
    * @param []
    * @return java.util.List<com.baba.pojo.Brand> 
    * @description 从redis中查询所有品牌
    */
    public List<Brand> findAllFromRedis() {
        Map<String, String> hgetAll = jedis.hgetAll("brand");
        // 将查询的结果放入到品牌对象集合中
        List<Brand> brands = new ArrayList<>();

        for (Map.Entry<String, String> entry : hgetAll.entrySet()) {
            Brand brand = new Brand();
            brand.setId(Long.parseLong(entry.getKey()));
            brand.setName(entry.getValue());
            brands.add(brand);
        }
        return brands;
    }
}

