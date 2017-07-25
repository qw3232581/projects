package com.heima.bos.dao.bc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.heima.bos.domain.bc.Decidedzone;

public interface DecidedzoneDao extends JpaRepository<Decidedzone, String>, JpaSpecificationExecutor<Decidedzone> {


}
