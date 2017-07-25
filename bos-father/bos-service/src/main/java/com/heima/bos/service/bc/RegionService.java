package com.heima.bos.service.bc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.heima.bos.domain.bc.Region;

public interface RegionService {

    void saveRegion(Region model);

    void importData(ArrayList<Region> regions);

    Region findRegionById(String id);

    Page<Region> pageQuery(Pageable pageRequest, Specification<Region> spec);

    List<Region> findAllRegions(String params);

	String pageQueryByRedis(Pageable pageRequest);

}
