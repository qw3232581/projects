package com.heima.bos.dao.auth;

import com.heima.bos.domain.auth.Function;
import com.heima.bos.domain.auth.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuDao extends JpaRepository<Menu, String>, JpaSpecificationExecutor<Menu> {

    @Query("from Menu where generatemenu = 1 order by zindex desc")
    List<Menu> ajaxListHasSonMenus();

    @Query("from Menu order by zindex desc")
    List<Menu> ajaxList();

    @Query("from Menu m inner join fetch m.roles r inner join fetch r.users u where u.id = ?1 order by m.zindex desc")
    List<Menu> findMenuByUserId(Integer id);
}
