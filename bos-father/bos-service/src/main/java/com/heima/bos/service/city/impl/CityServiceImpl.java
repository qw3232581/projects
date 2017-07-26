package com.heima.bos.service.city.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.heima.bos.dao.city.CityDao;
import com.heima.bos.domain.city.City;
import com.heima.bos.redis.crud.RedisCRUD;
import com.heima.bos.service.city.CityService;

@Service
@Transactional
public class CityServiceImpl implements CityService {
	@Autowired
	private CityDao cityDao;
	
	@Autowired
	private RedisCRUD redisCRUDImpl;

	@Override
	public List<City> findAll(Integer pid) {
		return cityDao.findAllByPid(pid);
	}

	@Override
	public String findCityByPidFromRedis(Integer pid) {
		String jsonValue = redisCRUDImpl.GetJSONStringFromRedis("City_"+pid );
		if (StringUtils.isBlank(jsonValue)) {
			List<City> cities = cityDao.findAllByPid(pid);
			jsonValue = JSON.toJSONString(cities);
			redisCRUDImpl.writeJSONStringToRedis("City_"+pid, jsonValue);
		}
		return jsonValue;
	}

}

