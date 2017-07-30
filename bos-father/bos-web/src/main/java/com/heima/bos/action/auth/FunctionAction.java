package com.heima.bos.action.auth;

import com.heima.bos.base.BaseAction;
import com.heima.bos.domain.auth.Function;
import com.heima.bos.domain.bc.Region;
import com.heima.bos.utils.PinYin4jUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("bos")
public class FunctionAction extends BaseAction<Function> {

    @Action(value = "functionAction_saveFunction", results = {
            @Result(name = "saveFunction", location = "/WEB-INF/pages/admin/function.jsp")})
    public String saveRegion() {
        try {
            facadeService.getFunctionService().saveFunction(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "saveFunction";
    }

    @Action(value = "functionAction_ajaxList", results = {
            @Result(name = "ajaxList", type = "fastJson")})
    public String ajaxList() {
        try {
            List<Function> functions =  facadeService.getFunctionService().ajaxList();
            push(functions);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ajaxList";
    }

    @Action(value = "functionAction_pageQuery")
    //TODO redis
    public String pageQuery() {
        Page<Function> pageData = facadeService.getFunctionService().pageQuery(getPageRequest());
        setPageDatas(pageData);
        return "pageQuery";
    }

}
