package com.heima.bos.dao.bc;

import com.heima.bos.domain.bc.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegionDao extends JpaRepository<Region, String>, JpaSpecificationExecutor<Region> {

    @Query("from Region where province like ?1 or city like ?1 or district like ?1")
    List<Region> findAllRegionsLikeProvinceOrCityOrDistrict(String p);

    @Query("from Region where province = ?1 and city = ?2 and district = ?3")
    Region findRegionsByDetailedAddress(String province, String city, String district);
}
