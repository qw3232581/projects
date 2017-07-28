package com.heima.bos.service.qp.impl;

import com.heima.bos.dao.bc.DecidedzoneDao;
import com.heima.bos.dao.bc.RegionDao;
import com.heima.bos.dao.qp.NoticeBillDao;
import com.heima.bos.dao.qp.WorkBillDao;
import com.heima.bos.domain.bc.Decidedzone;
import com.heima.bos.domain.bc.Region;
import com.heima.bos.domain.bc.Staff;
import com.heima.bos.domain.bc.Subarea;
import com.heima.bos.domain.customer.Customer;
import com.heima.bos.domain.qp.NoticeBill;
import com.heima.bos.domain.qp.WorkBill;
import com.heima.bos.service.base.BaseInterface;
import com.heima.bos.service.qp.NoticeBillService;
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
import java.util.Set;

@Service
@Transactional
public class NoticeBillServiceImpl implements NoticeBillService {

    @Autowired
    private NoticeBillDao noticeBillDao;
    @Autowired
    private DecidedzoneDao decidedzoneDao;
    @Autowired
    private WorkBillDao workBillDao;
    @Autowired
    private RegionDao reginDao;
    @Autowired
    @Qualifier("jmsQueueTemplate")
    private JmsTemplate jmsTemplate;


    @Override
    public Customer findCustomerByTelephone(String telephone) {
        String url = BaseInterface.CRM_BASE_URL + "findCustomerByTelephone/" + telephone;
        return WebClient.create(url).accept(MediaType.APPLICATION_JSON).get(Customer.class);
    }

    @Override
    public void saveNoticeBill(final NoticeBill model, String province, String city, String district) {
        noticeBillDao.saveAndFlush(model);
        if (!("null".equalsIgnoreCase(String.valueOf(model.getCustomerId())))){
            String url = BaseInterface.CRM_BASE_URL + "updateAddressById/" + model .getCustomerId() +"/"+model.getPickaddress();
            WebClient.create(url).put(null);
        }else {
            String url = BaseInterface.CRM_BASE_URL + "saveBillNotice";
            Customer c = new Customer();
            c.setName(model.getCustomerName());
            c.setAddress(model.getPickaddress());
            c.setDecidedzoneId(null);
            c.setStation("hangtou");
            c.setTelephone(model.getTelephone());
            Response post = WebClient.create(url).accept(MediaType.APPLICATION_JSON).post(c);
            Customer customer = post.readEntity(Customer.class);
            model.setCustomerId(customer.getId());
        }

        //地址库完全匹配
        String url = BaseInterface.CRM_BASE_URL + "findCustomerByAddress/" + model.getPickaddress();
        Customer c = WebClient.create(url).accept(MediaType.APPLICATION_JSON).get(Customer.class);

        if (c!=null){
            String decidedzoneId = c.getDecidedzoneId();
            if (StringUtils.isNotBlank(decidedzoneId)){
                Decidedzone decidedzone = decidedzoneDao.findOne(c.getDecidedzoneId());
                Staff staff = decidedzone.getStaff();
                model.setStaff(staff);
                model.setOrdertype("自动");
                WorkBill workBill = new WorkBill();
                workBill.setBuildtime(new Date(System.currentTimeMillis()));
                workBill.setAttachbilltimes(0);
                workBill.setStaff(staff);
                workBill.setNoticeBill(model);
                workBill.setPickstate("新单");
                workBill.setType("新-完全匹配");
                workBill.setRemark(model.getRemark());

                workBillDao.save(workBill);

                sendBillMessage(model,staff);
                return;

            }
        }

        

        //匹配分区
        Region region = reginDao.findRegionsByDetailedAddress(province,city,district);
        Set<Subarea> subareas = region.getSubareas();
        if (subareas != null && subareas.size() >0){
            for (Subarea subarea : subareas) {
                if (model.getPickaddress().contains(subarea.getAddresskey())){
                    Decidedzone zone = subarea.getDecidedzone();
                    if (zone != null){
                        Staff staff = zone.getStaff();
                        model.setStaff(staff);
                        model.setOrdertype("自动");
                        WorkBill bill = new WorkBill();
                        bill.setAttachbilltimes(0);
                        bill.setBuildtime(new Date(System.currentTimeMillis()));
                        bill.setNoticeBill(model);
                        bill.setType("新-匹配分区");
                        bill.setStaff(staff);
                        bill.setRemark(model.getRemark());
                        bill.setPickstate("新单");
                        workBillDao.save(bill);

                        sendBillMessage(model,staff);

                        return;


                    }
                }

            }
        }

        model.setOrdertype("手工");
    }

    private void sendBillMessage(final NoticeBill model, final Staff staff){
        jmsTemplate.send("bos_staff", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("telephone", staff.getTelephone());
                mapMessage.setString("msg", "你好,客户名称=" + model.getCustomerName() + "---送达地址=" + model.getArrivecity());
                return mapMessage;
            }
        });
    }


}

