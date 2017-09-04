package com.heima.bos.dao.qp;

import com.heima.bos.domain.qp.WorkBill;
import com.heima.bos.domain.qp.WorkOrderManage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WorkOrderManageDao extends JpaRepository<WorkOrderManage, String>, JpaSpecificationExecutor<WorkOrderManage> {


}
