package com.heima.bos.service.city;

import java.util.List;

import com.heima.bos.domain.city.City;

public interface CityService {

	List<City> findAll(Integer pid);

	String findCityByPidFromRedis(Integer pid);

}
