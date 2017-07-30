package com.heima.bos.service.auth;

import com.heima.bos.domain.auth.Function;
import com.heima.bos.domain.auth.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {

    void saveRole(Role model);
}
