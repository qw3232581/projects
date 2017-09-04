package com.heima.bos.dao.qp;

import com.heima.bos.domain.qp.NoticeBill;
import com.heima.bos.domain.qp.WorkBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WorkBillDao extends JpaRepository<WorkBill, String>, JpaSpecificationExecutor<WorkBill> {

}
