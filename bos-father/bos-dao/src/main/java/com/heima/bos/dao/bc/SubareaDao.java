package com.heima.bos.dao.bc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.heima.bos.domain.bc.Subarea;

public interface SubareaDao extends JpaRepository<Subarea, String>, JpaSpecificationExecutor<Subarea> {

    @Query("from Subarea where decidedzone is null")
    List<Subarea> findAllAjax();

    @Modifying
    @Query("update Subarea set decidedzone.id = ?2 where id =?1")
    void assignSubareasToDecidedZone(String id, String decidedzoneid);

    @Query("from Subarea where decidedzone is null")
	List<Subarea> noAssociationList();

}
