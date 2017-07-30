package com.heima.bos.service.auth;

import com.heima.bos.domain.auth.Function;
import com.heima.bos.domain.auth.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MenuService {

    void saveMenu(Menu model);

    Page<Menu> pageQuery(Pageable pageRequest);

    List<Menu> ajaxListHasSonMenus();

    List<Menu> ajaxList();
}
