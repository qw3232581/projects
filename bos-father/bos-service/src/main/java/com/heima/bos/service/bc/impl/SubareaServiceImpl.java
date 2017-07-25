package com.heima.bos.service.bc.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.heima.bos.dao.bc.SubareaDao;
import com.heima.bos.domain.bc.Subarea;
import com.heima.bos.service.bc.SubareaService;

@Service
@Transactional
public class SubareaServiceImpl implements SubareaService {

    @Autowired
    private SubareaDao subareaDao;


    @Override
    public Page<Subarea> pageQuery(PageRequest pageRequest) {

        Page<Subarea> all = subareaDao.findAll(pageRequest);
        List<Subarea> list = all.getContent();
        for (Subarea subarea : list) {
            Hibernate.initialize(subarea.getRegion());
        }

        return all;
    }

    @Override
    public void saveSubarea(Subarea model) {
        subareaDao.save(model);
    }

    @Override
    public Page<Subarea> pageQuery(Pageable pageRequest, Specification<Subarea> spec) {
        return subareaDao.findAll(spec, pageRequest);
    }

    @Override
    public List<Subarea> findAll(Specification specification) {
        return subareaDao.findAll(specification);
    }

    @Override
    public List<Subarea> findAllAjax() {
        return subareaDao.findAllAjax();
    }

	@Override
	public List<Subarea> noAssociationList() {
		return subareaDao.noAssociationList();
	}

}
