package com.heima.bos.service.bc.impl;

import com.heima.bos.dao.bc.StandardDao;
import com.heima.bos.domain.bc.Standard;
import com.heima.bos.service.bc.StandardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StandardServiceImpl implements StandardService {

    @Autowired
    private StandardDao standardDao;

    @Override
    public void save(Standard model) {
        standardDao.save(model);
    }

    @Override
    public Page<Standard> pageQuery(Pageable pageRequest) {
        return standardDao.findAll(pageRequest);
    }

    @Override
    public void delStandard(long standardId) {
        standardDao.delStandard(standardId);
    }

    @Override
    public void restoreStandard(long standardId) {
        standardDao.restoreStandard(standardId);
    }

    @Override
    public List<Standard> findAllInUse() {
        return standardDao.findAllInUse();
    }

}
