package com.heima.bos.dao.city;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.heima.bos.domain.city.City;

public interface CityDao extends JpaRepository<City, String> {

	@Query("from City where pid = ?1")
	List<City> findAllByPid(Integer pid);
}
