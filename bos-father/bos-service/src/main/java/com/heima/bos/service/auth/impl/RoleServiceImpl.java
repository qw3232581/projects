package com.heima.bos.service.auth.impl;

import com.heima.bos.dao.auth.FunctionDao;
import com.heima.bos.dao.auth.RoleDao;
import com.heima.bos.domain.auth.Function;
import com.heima.bos.domain.auth.Menu;
import com.heima.bos.domain.auth.Role;
import com.heima.bos.service.auth.FunctionService;
import com.heima.bos.service.auth.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private FunctionDao functionDao;

    @Override
    public void saveRole(Role model, String menuIds, String[] functionIds) {
        if (model.getId()==null || model.getId() == ""){
            roleDao.saveAndFlush(model);
        }else {
            String name = model.getName();
            String code = model.getCode();
            String description = model.getDescription();
            model = roleDao.getOne(model.getId());
            model.setCode(code);
            model.setName(name);
            model.setDescription(description);
        }

        if (functionIds != null && functionIds.length != 0){
            Set<Function> functions = model.getFunctions();
            functions.clear();
            for (String fid : functionIds) {
                //该方法会多次查询数据库
                //Function function = functionDao.findOne(fid);
                Function f = new Function();
                f.setId(fid);//转化为托管态

                functions.add(f);
            }
        }

        if (StringUtils.isNotBlank(menuIds)){
            Set<Menu> menus = model.getMenus();
            menus.clear();
            String[] mids = menuIds.split(",");
            for (String mid : mids) {
                Menu menu = new Menu();
                menu.setId(mid);
                menus.add(menu);
            }
        }
    }

    @Override
    public Page<Role> pageQuery(Pageable pageRequest) {
        return roleDao.findAll(pageRequest);
    }

    @Override
    public List<Role> ajaxList() {
        return roleDao.findAll();
    }

    @Override
    public void delRole(Role model, String roleIds) {

        if (StringUtils.isNotBlank(roleIds)){
            String[] roleids = roleIds.split(",");
            for (String rid : roleids) {
                Role role = roleDao.getOne(rid);
                role.getFunctions().clear();
                role.getMenus().clear();
                roleDao.delete(rid);
            }
        }

    }

}
