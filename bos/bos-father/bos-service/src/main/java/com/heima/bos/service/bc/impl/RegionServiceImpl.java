package com.heima.bos.service.bc.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.heima.bos.dao.bc.RegionDao;
import com.heima.bos.domain.bc.Region;
import com.heima.bos.redis.crud.RedisCRUD;
import com.heima.bos.service.bc.RegionService;

@Service
@Transactional
public class RegionServiceImpl implements RegionService {

    @Autowired
    private RegionDao regionDao;
    
    @Autowired
    private RedisCRUD redisCRUDImpl;

    @Override
    public void saveRegion(Region model) {
        regionDao.save(model);
    }

    @Override
    public void importData(ArrayList<Region> regions) {
        regionDao.save(regions);
    }

    @Override
    public Region findRegionById(String id) {
        return regionDao.findOne(id);
    }

    @Override
    public Page<Region> pageQuery(Pageable pageRequest, Specification<Region> spec) {
        return regionDao.findAll(spec, pageRequest);
    }

    @Override
    public List<Region> findAllRegions(String params) {
        if (StringUtils.isNotBlank(params)) {
            return regionDao.findAllRegionsLikeProvinceOrCityOrDistrict("%" + params + "%");
        }
        return findAll();
    }

    private List<Region> findAll() {
        return regionDao.findAll();
    }
    
	@Override
	public String pageQueryByRedis(Pageable pageRequest) {
		// 区域大量数据 第一次查询数据库 存放缓冲服务器 redis 第二次查询 从redis取 
		// 如果 数据库发生更新 及时更新缓存redis
    	
		int pageNumber = pageRequest.getPageNumber();// 获取当前查询的页码
		int pageSize = pageRequest.getPageSize();// 获取当前查询每页记录数
		String keyId = pageNumber + "_" + pageSize;
		
		// 通过key查询对应的分页数据   每页记录数作为唯一标识 存储在redis服务器上
		String jsonvalue = redisCRUDImpl.GetJSONStringFromRedis(keyId);
		if (StringUtils.isBlank(jsonvalue)) {
			// 第一次查询数据库,将数据库数据序列化Json格式的数据存储到redis 服务器上
			Page<Region> pageData = regionDao.findAll(pageRequest);// 查询数据库
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("total", pageData.getTotalElements());
			map.put("rows", pageData.getContent());
			jsonvalue = JSON.toJSONString(map);// 满足easyui需要分页数据的json格式
			redisCRUDImpl.writeJSONStringToRedis(keyId, jsonvalue);// 将分页的json格式数据存储到redis服务器上
		}
		return jsonvalue;
	}

}
