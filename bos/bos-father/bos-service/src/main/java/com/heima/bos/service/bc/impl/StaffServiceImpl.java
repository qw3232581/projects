package com.heima.bos.service.bc.impl;

import com.heima.bos.dao.bc.StaffDao;
import com.heima.bos.domain.bc.Staff;
import com.heima.bos.service.bc.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffDao staffDao;

    @Override
    public Staff validTelephone(String telephone) {
        return staffDao.validTelephone(telephone);
    }

    @Override
    public void saveStaff(Staff model) {
        staffDao.save(model);
    }

    @Override
    public void delStaff(String standardId) {
        staffDao.delStaff(standardId);
    }

    @Override
    public void restoreStaff(String standardId) {
        staffDao.restoreStaff(standardId);
    }

    @Override
    public Page<Staff> pageQuery(Pageable pageRequest, Specification<Staff> spec) {
        return staffDao.findAll(spec, pageRequest);
    }

    @Override
    public List<Staff> findAllAvailable() {
        return staffDao.findAllAvailable();
    }


}
