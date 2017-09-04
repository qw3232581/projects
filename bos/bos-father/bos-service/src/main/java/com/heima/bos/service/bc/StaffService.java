package com.heima.bos.service.bc;

import com.heima.bos.domain.bc.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface StaffService {

    Staff validTelephone(String telephone);

    void saveStaff(Staff model);

    Page<Staff> pageQuery(Pageable pageRequest, Specification<Staff> spec);

    void delStaff(String standardId);

    void restoreStaff(String standardId);

    List<Staff> findAllAvailable();

}
