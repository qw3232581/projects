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
        try {
            String[] functionIds = getRequest().getParameterValues("functionIds");
            String menuIds = getParameter("menuIds");

            facadeService.getRoleService().saveRole(model,menuIds,functionIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "saveRole";
    }

    @Action(value = "roleAction_delRole", results = {
            @Result(name = "delRole", location = "/WEB-INF/pages/admin/role.jsp")})
    public String delRole() {
        try {
            String roleIds = getParameter("roleIds");

            facadeService.getRoleService().delRole(model,roleIds);
            push(true);
        } catch (Exception e) {
            push(false);
            e.printStackTrace();
        }
        return "delRole";
    }


    @Action(value = "roleAction_ajaxList", results = {
            @Result(name = "ajaxList", type = "fastJson")})
    public String ajaxList() {
        List<Role> roles = facadeService.getRoleService().ajaxList();
        push(roles);
        return "ajaxList";
    }

    @Action(value = "roleAction_pageQuery")
    //TODO redis
    public String pageQuery() {
        Page<Role> pageData = facadeService.getRoleService().pageQuery(getPageRequest());
        setPageDatas(pageData);
        return "pageQuery";
    }

}
