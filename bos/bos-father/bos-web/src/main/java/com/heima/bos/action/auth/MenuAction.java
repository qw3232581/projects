package com.heima.bos.action.auth;

import com.heima.bos.base.BaseAction;
import com.heima.bos.domain.auth.Function;
import com.heima.bos.domain.auth.Menu;
import com.heima.bos.domain.user.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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
public class MenuAction extends BaseAction<Menu> {

    @Action(value = "menuAction_saveMenu", results = {
            @Result(name = "saveMenu", location = "/WEB-INF/pages/admin/menu.jsp")})
    public String saveMenu() {
        if (model.getMenu()==null|| StringUtils.isBlank(model.getMenu().getId())){
            model.setMenu(null);
        }
        facadeService.getMenuService().saveMenu(model);
        return "saveMenu";
    }

    @Action(value = "menuAction_ajaxListHasSonMenus", results = {
            @Result(name = "ajaxListHasSonMenus", type = "fastJson",
                    params = {"includProperties","id,name"})})
    public String ajaxListHasSonMenus() {
        List<Menu> list = facadeService.getMenuService().ajaxListHasSonMenus();
        push(list);
        return "ajaxListHasSonMenus";
    }

    @Action(value = "menuAction_ajaxList", results = {
            @Result(name = "ajaxList", type = "fastJson",
                    params = {"includeProperties","id,name,pId,page"})})
    public String ajaxList() {
        try {
            List<Menu> menus = facadeService.getMenuService().ajaxList();
            push(menus);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ajaxList";
    }

    @Action(value = "menuAction_findMenuByRoleId", results = {
            @Result(name = "findMenuByRoleId", type = "fastJson",
                    params = {"includeProperties","id"})})
    public String findMenuByRoleId() {
        try {
            String roleId = getParameter("roleId");
            List<Menu> menus =  facadeService.getMenuService().findMenuByRoleId(roleId);
            push(menus);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "findMenuByRoleId";
    }

    @Action(value = "menuAction_findMenuByUserId", results = {
            @Result(name = "findMenuByUserId", type = "fastJson")})
    public String menuAction_findMenuByUserId() {

        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        List<Menu> menus = facadeService.getMenuService().findMenuByUserId(user.getId());
        push(menus);
        return "findMenuByUserId";
    }

    @Action(value = "menuAction_pageQuery")
    //TODO redis
    public String pageQuery() {
        super.setPage(Integer.parseInt(getParameter("page")));
        Page<Menu> pageData = facadeService.getMenuService().pageQuery(getPageRequest());
        setPageDatas(pageData);
        return "pageQuery";
    }

}
