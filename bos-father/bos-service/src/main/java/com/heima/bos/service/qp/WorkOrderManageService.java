package com.heima.bos.service.qp;

import com.heima.bos.domain.customer.Customer;
import com.heima.bos.domain.qp.NoticeBill;
import com.heima.bos.domain.qp.WorkOrderManage;

import java.util.List;

public interface WorkOrderManageService {

    List<WorkOrderManage> findAll();
}
