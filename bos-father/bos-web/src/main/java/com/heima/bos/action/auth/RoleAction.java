package com.heima.bos.action.auth;

import com.heima.bos.base.BaseAction;
import com.heima.bos.domain.auth.Menu;
import com.heima.bos.domain.auth.Role;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("bos")
public class RoleAction extends BaseAction<Role> {

    @Action(value = "roleAction_saveRole", results = {
            @Result(name = "saveRole", location = "/WEB-INF/pages/admin/role.jsp")})
    public String saveRole() {
        facadeService.getRoleService().saveRole(model);
        return "saveRole";
    }
//
//    @Action(value = "menuAction_ajaxListHasSonMenus", results = {
//            @Result(name = "ajaxListHasSonMenus", type = "fastJson",
//                    params = {"includProperties","id,name"})})
//    public String ajaxListHasSonMenus() {
//        List<Menu> list = facadeService.getMenuService().ajaxListHasSonMenus();
//        push(list);
//        return "ajaxListHasSonMenus";
//    }
//
//    @Action(value = "menuAction_pageQuery")
//    //TODO redis
//    public String pageQuery() {
//        super.setPage(Integer.parseInt(getParameter("page")));
//        Page<Menu> pageData = facadeService.getMenuService().pageQuery(getPageRequest());
//        setPageDatas(pageData);
//        return "pageQuery";
//    }

}
