package com.heima.bos.dao.bc;

import com.heima.bos.domain.bc.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StaffDao extends JpaRepository<Staff, String>, JpaSpecificationExecutor<Staff> {

    @Query("from Staff where telephone = ?1")
    Staff validTelephone(String telephone);

    @Modifying
    @Query("update Staff set deltag = 1 where id = ?1")
    void delStaff(String standardId);


    @Modifying
    @Query("update Staff set deltag = 0 where id = ?1")
    void restoreStaff(String standardId);

    @Query("from Staff where deltag = 0")
    List<Staff> findAllAvailable();

}
