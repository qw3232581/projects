package com.heima.bos.service.qp;

import com.heima.bos.domain.city.City;
import com.heima.bos.domain.customer.Customer;
import com.heima.bos.domain.qp.NoticeBill;

import java.util.List;

public interface NoticeBillService {
    Customer findCustomerByTelephone(String telephone);
    void saveNoticeBill(NoticeBill model, String province, String city, String district);
}
