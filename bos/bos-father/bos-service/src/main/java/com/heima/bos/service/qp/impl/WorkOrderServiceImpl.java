package com.heima.bos.service.qp.impl;

import com.heima.bos.dao.bc.DecidedzoneDao;
import com.heima.bos.dao.bc.RegionDao;
import com.heima.bos.dao.qp.NoticeBillDao;
import com.heima.bos.dao.qp.WorkBillDao;
import com.heima.bos.dao.qp.WorkOrderManageDao;
import com.heima.bos.domain.bc.Decidedzone;
import com.heima.bos.domain.bc.Region;
import com.heima.bos.domain.bc.Staff;
import com.heima.bos.domain.bc.Subarea;
import com.heima.bos.domain.customer.Customer;
import com.heima.bos.domain.qp.NoticeBill;
import com.heima.bos.domain.qp.WorkBill;
import com.heima.bos.domain.qp.WorkOrderManage;
import com.heima.bos.service.base.BaseInterface;
import com.heima.bos.service.qp.NoticeBillService;
import com.heima.bos.service.qp.WorkOrderManageService;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class WorkOrderServiceImpl implements WorkOrderManageService {

    @Autowired
    private WorkOrderManageDao workOrderServiceDao;


    @Override
    public List<WorkOrderManage> findAll() {
        return workOrderServiceDao.findAll();
    }
}

