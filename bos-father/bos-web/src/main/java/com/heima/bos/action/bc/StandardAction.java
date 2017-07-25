package com.heima.bos.action.bc;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import com.heima.bos.base.BaseAction;
import com.heima.bos.domain.bc.Standard;
import com.heima.bos.domain.user.User;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("bos")
public class StandardAction extends BaseAction<Standard> {
    //添加Standard
    @Action(value = "standardAction_saveStandard", results = {
            @Result(name = "saveStandard", location = "/WEB-INF/pages/base/standard.jsp")})
    public String saveStandard() {
        User loginUser = (User) getSessionAttribute("loginUser");
        model.setOperator(loginUser.getEmail());
        model.setOperatordept(loginUser.getStation());
        model.setOperationtime(new Timestamp(System.currentTimeMillis()));
        facadeService.getStandardService().save(model);
        return "saveStandard";
    }

    @Action(value = "standardAction_pageQuery")
    public String pageQuery() {
        Page<Standard> pageDatas = facadeService.getStandardService().pageQuery(getPageRequest());
        setPageDatas(pageDatas);

        return "pageQuery";
    }

    @Action(value = "standardAction_delBatch", results = {
            @Result(name = "delBatch", type = "fastJson")})
    public String delBatch() {
        try {
            String idsString = this.getParameter("ids");
            if (StringUtils.isNotBlank(idsString)) {
                String[] ids = idsString.split(",");
                for (String standardId : ids) {
                    facadeService.getStandardService().delStandard(Long.parseLong(standardId));
                }
                push(true);
            } else {
                push(false);
            }
        } catch (Exception e) {
            push(false);
            e.printStackTrace();
        }
        return "delBatch";
    }


    @Action(value = "standardAction_restoreBatch", results = {
            @Result(name = "restoreBatch", type = "fastJson")})
    public String restoreBatch() {
        try {
            String idsString = this.getParameter("ids");
            if (StringUtils.isNotBlank(idsString)) {
                String[] ids = idsString.split(",");
                for (String standardId : ids) {
                    facadeService.getStandardService().restoreStandard(Long.parseLong(standardId));
                }
                push(true);
            } else {
                push(false);
            }
        } catch (Exception e) {
            push(false);
            e.printStackTrace();
        }
        return "restoreBatch";
    }


    @Action(value = "standardAction_ajaxList", results = {
            @Result(name = "ajaxList", type = "fastJson", params = {"includeProperties", "name"})})
    public String ajaxList() {
        List<Standard> standards = facadeService.getStandardService().findAllInUse();
        push(standards);
        return "ajaxList";
    }
}
