package com.heima.cxf.crm.dao;

import java.util.List;

import com.heima.cxf.crm.domain.Customer;

public interface ICustomerDAO {
	// 获取定区未关联的客户信息
	List<Customer> getNoAssociations();

	// 获取指定定区绑定客户信息
	List<Customer> getInUseAssociations(String decidezone_id);

	// 定区绑定客户
	void assignCustomerToDecidedZone(String customer_id, String decidedZone_id);

	// 取消定区关联所有客户
	void cancelDecidedZoneCustomers(String decidedZone_id);

    Customer findCustomerByTelephone(String telephone);

    Customer save(Customer customer);

	void updateAddressById(Integer customerId, String pickAddress);

	Customer findCustomerByAddress(String pickAddress);

	void setDecidedzoneNull(Integer customerId);
}

