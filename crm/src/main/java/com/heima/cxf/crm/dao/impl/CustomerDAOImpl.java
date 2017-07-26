package com.heima.cxf.crm.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.heima.cxf.crm.dao.ICustomerDAO;
import com.heima.cxf.crm.domain.Customer;

@SuppressWarnings("all")
@Repository
public class CustomerDAOImpl extends HibernateDaoSupport implements ICustomerDAO {

	@Autowired
	public void setFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public List<Customer> getNoAssociations() {
		List<Customer> list = getHibernateTemplate().find("from Customer where decidedzoneId  is null");
		return list;
	}

	@Override
	public List<Customer> getInUseAssociations(String decidezone_id) {
		List<Customer> list = getHibernateTemplate().find("from Customer where decidedzoneId = ?", decidezone_id);
		return list;
	}

	@Override
	public void assignCustomerToDecidedZone(String customer_id, String decidedZone_id) {
		// 修改 update xxx set decidezoneid = ? where id = ? hql Query session
		getSession().createQuery("update Customer set decidedzoneId = ? where id = ?").setParameter(0, decidedZone_id).setParameter(1, Integer.parseInt(customer_id)).executeUpdate();
	}

	@Override
	// 取消 定区 关联所有用户
	public void cancelDecidedZoneCustomers(String decidedZone_id) {
		getSession().createQuery("update Customer set decidedzoneId = null where decidedzoneId = ?").setParameter(0, decidedZone_id).executeUpdate();

	}

}
