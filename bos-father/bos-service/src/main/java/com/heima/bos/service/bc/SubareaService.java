package com.heima.bos.service.bc;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.heima.bos.domain.bc.Subarea;

public interface SubareaService {

    Page<Subarea> pageQuery(PageRequest pageRequest);

    void saveSubarea(Subarea model);

    Page<Subarea> pageQuery(Pageable pageRequest, Specification<Subarea> spec);

    List<Subarea> findAll(Specification specification);

	List<Subarea> noAssociationList();

	List<Subarea> findAllAjax();


}
