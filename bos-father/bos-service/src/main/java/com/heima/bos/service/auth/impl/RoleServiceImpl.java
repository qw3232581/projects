package com.heima.bos.service.auth.impl;

import com.heima.bos.dao.auth.FunctionDao;
import com.heima.bos.dao.auth.RoleDao;
import com.heima.bos.domain.auth.Function;
import com.heima.bos.domain.auth.Role;
import com.heima.bos.service.auth.FunctionService;
import com.heima.bos.service.auth.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;

    @Override
    public void saveRole(Role model) {
        roleDao.save(model);
    }
}
