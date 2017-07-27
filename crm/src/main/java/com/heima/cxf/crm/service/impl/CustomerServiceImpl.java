package com.heima.cxf.crm.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.heima.cxf.crm.dao.ICustomerDAO;
import com.heima.cxf.crm.domain.Customer;
import com.heima.cxf.crm.service.ICustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements ICustomerService {

	@Autowired
	private ICustomerDAO customerDao;

	@Override
	public List<Customer> getNoAssociations() {
		return customerDao.getNoAssociations();
	}

	@Override
	public List<Customer> getInUseAssociations(String decidezone_id) {
		return customerDao.getInUseAssociations(decidezone_id);
	}

	@Override
	public void assignCustomerToDecidedZone(String customerids, String decidedZone_id) {
		customerDao.cancelDecidedZoneCustomers(decidedZone_id);
		if (StringUtils.isNoneBlank(customerids)) {
			if ("null".equals(customerids)) {
				return;
			}
			String customerIds[] = customerids.split(",");
			for (String id : customerIds) {
				customerDao.assignCustomerToDecidedZone(id, decidedZone_id);
			}
		}

	}

	@Override
	public Customer findCustomerByTelephone(String telephone) {
		return customerDao.findCustomerByTelephone(telephone);
	}

    @Override
    public void updateAddressById(Integer customerId, String pickAddress) {
        customerDao.updateAddressById(customerId,pickAddress);
    }

    @Override
    public Customer saveBillNotice(Customer customer) {
        return customerDao.save(customer);
    }

    @Override
    public Customer findCustomerByAddress(String pickAddress) {
        return customerDao.findCustomerByAddress(pickAddress);
    }


}
