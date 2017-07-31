package com.heima.bos.service.auth;

import com.heima.bos.domain.auth.Function;
import com.heima.bos.domain.auth.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {

    void saveRole(Role model, String menuIds, String[] functionIds);

    Page<Role> pageQuery(Pageable pageRequest);

    List<Role> ajaxList();

    void delRole(Role model, String roleIds);
}
