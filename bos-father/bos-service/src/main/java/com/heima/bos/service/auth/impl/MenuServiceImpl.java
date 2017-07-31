package com.heima.bos.service.auth.impl;

import com.heima.bos.dao.auth.FunctionDao;
import com.heima.bos.dao.auth.MenuDao;
import com.heima.bos.dao.auth.RoleDao;
import com.heima.bos.domain.auth.Function;
import com.heima.bos.domain.auth.Menu;
import com.heima.bos.domain.auth.Role;
import com.heima.bos.service.auth.FunctionService;
import com.heima.bos.service.auth.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private RoleDao roleDao;

    @Override
    public void saveMenu(Menu model) {
        menuDao.save(model);
    }

    @Override
    public Page<Menu> pageQuery(Pageable pageRequest) {
        return menuDao.findAll(pageRequest);
    }

    @Override
    public List<Menu> ajaxListHasSonMenus() {
        return menuDao.ajaxListHasSonMenus();
    }

    @Override
    public List<Menu> ajaxList() {
        return menuDao.ajaxList();
    }

    @Override
    public List<Menu> findMenuByRoleId(String roleId) {
        Role role = roleDao.findOne(roleId);
        Set<Menu> menus = role.getMenus();
        List<Menu> menuList = new ArrayList<>();
        for (Menu menu : menus) {
            menuList.add(menu);
        }
        return menuList;
    }
}
