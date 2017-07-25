package com.heima.cxf.crm.dao;

import java.util.List;

import com.heima.cxf.crm.domain.Customer;

public interface ICustomerDAO {
	// 获取定区未关联的客户信息
	public List<Customer> getNoAssociations();

	// 获取指定定区绑定客户信息
	public List<Customer> getInUseAssociations(String decidezone_id);

	// 定区绑定客户
	public void assignCustomerToDecidedZone(String customer_id, String decidedZone_id);

	// 取消定区关联所有客户
	public void cancelDecidedZoneCustomers(String decidedZone_id);

}

