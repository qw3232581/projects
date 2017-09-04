package com.heima.bos.dao.auth;

import com.heima.bos.domain.auth.Function;
import com.heima.bos.domain.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleDao extends JpaRepository<Role, String>, JpaSpecificationExecutor<Role> {
    //sql : select r.* from role r join user_role ur on r.id = ur.role_id where ur.user_id = ?
    @Query("from Role r inner join fetch r.users u where u.id = ?1")
    List<Role> findAllRolesByUserId(Integer id);
}
