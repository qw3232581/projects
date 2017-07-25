package com.heima.bos.service.bc;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.heima.bos.domain.bc.Decidedzone;
import com.heima.bos.domain.customer.Customer;

public interface DecidedzoneService {

    void saveDecidedzone(Decidedzone model, String[] ids);

	List<Decidedzone> findAll();

	Page<Decidedzone> pageQuery(Pageable pageRequest, Specification<Decidedzone> specification);

	List<Customer> findNoAssociationCustomers();

	List<Customer> findAssociatedCustomers(String id);

	void assignCustomersToDecidedzone(String id, String[] customerIds);


}
