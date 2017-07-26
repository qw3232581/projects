package com.heima.bos.service.bc.impl;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.heima.bos.dao.bc.DecidedzoneDao;
import com.heima.bos.dao.bc.SubareaDao;
import com.heima.bos.domain.bc.Decidedzone;
import com.heima.bos.domain.customer.Customer;
import com.heima.bos.service.base.BaseInterface;
import com.heima.bos.service.bc.DecidedzoneService;

@Service
@Transactional
@SuppressWarnings("all")
public class DecidedzoneServiceImpl implements DecidedzoneService {

    @Autowired
    private DecidedzoneDao decidedzoneDao;
    @Autowired
    private SubareaDao subareaDao;

    @Override
    public void saveDecidedzone(Decidedzone model, String[] ids) {
        decidedzoneDao.save(model);
        if (ids != null && ids.length != 0) {
            for (String id : ids) {
                subareaDao.assignSubareasToDecidedZone(id, model.getId());
            }
        }
    }

	@Override
	public List<Decidedzone> findAll() {
		return decidedzoneDao.findAll();
	}

	@Override
	public Page<Decidedzone> pageQuery(Pageable pageRequest, Specification<Decidedzone> specification) {
		return decidedzoneDao.findAll(specification,pageRequest);
	}

	@Override
	public List<Customer> findNoAssociationCustomers() {
		String url = BaseInterface.CRM_BASE_URL + "noAssociation";
		List<Customer> list = (List<Customer>) WebClient.create(url)
								.accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		return list;
	}

	@Override
	public List<Customer> findAssociatedCustomers(String id) {
		String url = BaseInterface.CRM_BASE_URL +id;
		
		List<Customer> list = (List<Customer>) WebClient.create(url)
								.accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		return list;
	}

	@Override
	public void assignCustomersToDecidedzone(String id, String[] customerIds) {
		String url = BaseInterface.CRM_BASE_URL + "assignCustomerToDecidedZone/" +id;
		if (customerIds != null && customerIds.length != 0) {
			StringBuilder sb = new StringBuilder();
			for (String sid : customerIds) {
				sb.append(sid).append(",");
			}
			String cids = sb.substring(0,sb.length() - 1);
			url = url + "/" + cids;
		}else{
			url = url + "/null";
		}
		WebClient.create(url).put(null);
	}

}
