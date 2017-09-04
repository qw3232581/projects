package com.heima.bos.dao.bc;

import com.heima.bos.domain.bc.Standard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StandardDao extends JpaRepository<Standard, Integer> {

    @Modifying
    @Query("update Standard set isDel = 1 where id = ?1")
    void delStandard(long standardId);

    @Modifying
    @Query("update Standard set isDel = 0 where id = ?1")
    void restoreStandard(long standardId);

    @Query("from Standard where isDel = 0")
    List<Standard> findAllInUse();

}
