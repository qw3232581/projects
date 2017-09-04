package com.heima.bos.dao.auth;

import com.heima.bos.domain.auth.Function;
import com.heima.bos.domain.bc.Decidedzone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FunctionDao extends JpaRepository<Function, String>, JpaSpecificationExecutor<Function> {


}
