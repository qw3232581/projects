package com.heima.cxf.crm.dao.impl;

import java.io.Serializable;
import java.util.List;

import com.sun.jndi.cosnaming.IiopUrl;
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
        return getHibernateTemplate().find("from Customer where decidedzoneId  is null");
	}

	@Override
	public List<Customer> getInUseAssociations(String decidezone_id) {
        return getHibernateTemplate().find("from Customer where decidedzoneId = ?", decidezone_id);
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

	@Override
	public Customer findCustomerByTelephone(String telephone) {
        List<Customer> list = getHibernateTemplate().find("from Customer where telephone = ?", telephone);
        return list.isEmpty()? null : list.get(0);
	}

	@Override
	public Customer save(Customer customer) {
		Serializable id = getHibernateTemplate().save(customer);
		customer.setId((Integer) id);
		return customer;
	}

	@Override
	public void updateAddressById(Integer customerId, String pickAddress) {
        getSession().createQuery("update Customer set address = ? where  id = ? ")
                .setParameter(0,pickAddress ).setParameter(1,customerId).executeUpdate();
	}

    @Override
    public Customer findCustomerByAddress(String pickAddress) {
        List list = getHibernateTemplate().find("from Customer where address = ?", pickAddress);
        return list.isEmpty() ? null : (Customer) list.get(0);
    }

	@Override
	public void setDecidedzoneNull(Integer customerId) {
		getSession().createQuery("update Customer set decidedzoneId =null where id = ?")
				.setParameter(0, customerId).executeUpdate();
	}
}
