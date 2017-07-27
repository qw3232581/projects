package com.heima.bos.dao.qp;

import com.heima.bos.domain.bc.Decidedzone;
import com.heima.bos.domain.qp.NoticeBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NoticeBillDao extends JpaRepository<NoticeBill, String>, JpaSpecificationExecutor<NoticeBill> {


}
