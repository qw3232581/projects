package com.heima.bos.service.auth;

import com.heima.bos.domain.auth.Function;
import com.heima.bos.domain.bc.Decidedzone;
import com.heima.bos.domain.customer.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface FunctionService {

    void saveFunction(Function model);

    Page<Function> pageQuery(Pageable pageRequest);

    List<Function> ajaxList();
}
