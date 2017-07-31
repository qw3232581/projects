package com.heima.bos.service.auth.impl;

import com.heima.bos.dao.auth.FunctionDao;
import com.heima.bos.dao.auth.RoleDao;
import com.heima.bos.dao.bc.DecidedzoneDao;
import com.heima.bos.dao.bc.SubareaDao;
import com.heima.bos.domain.auth.Function;
import com.heima.bos.domain.auth.Role;
import com.heima.bos.domain.bc.Decidedzone;
import com.heima.bos.domain.customer.Customer;
import com.heima.bos.service.auth.FunctionService;
import com.heima.bos.service.base.BaseInterface;
import com.heima.bos.service.bc.DecidedzoneService;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class FunctionServiceImpl implements FunctionService {
    @Autowired
    private FunctionDao functionDao;
    @Autowired
    private RoleDao roleDao;

    @Override
    public void saveFunction(Function model) {
        functionDao.save(model);
    }

    @Override
    public Page<Function> pageQuery(Pageable pageRequest) {
        return functionDao.findAll(pageRequest);
    }

    @Override
    public List<Function> ajaxList() {
        return functionDao.findAll();
    }

    @Override
    public List<Function> findFunctionByRoleId(String roleId) {
        Role role = roleDao.findOne(roleId);
        Set<Function> functions = role.getFunctions();
        List<Function> functionList = new ArrayList<>();
        for (Function function : functions) {
            functionList.add(function);
        }
        return functionList;
    }
}
