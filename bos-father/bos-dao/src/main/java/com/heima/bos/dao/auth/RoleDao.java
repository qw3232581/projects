package com.heima.bos.dao.auth;

import com.heima.bos.domain.auth.Function;
import com.heima.bos.domain.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleDao extends JpaRepository<Role, String>, JpaSpecificationExecutor<Role> {


}
