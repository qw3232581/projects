package com.heima.bos.action.qp;

import com.heima.bos.base.BaseAction;
import com.heima.bos.domain.bc.Decidedzone;
import com.heima.bos.domain.bc.Staff;
import com.heima.bos.domain.bc.Subarea;
import com.heima.bos.domain.customer.Customer;
import com.heima.bos.domain.qp.NoticeBill;
import com.heima.bos.domain.user.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("bos")
public class NoticeBillAction extends BaseAction<NoticeBill> {
    @Action(value = "noticeBillAction_findCustomerByTelephone", results = {@Result(name = "findCustomerByTelephone", type = "fastJson")})
    public String findCustomerByTelephone() {
        Customer c = facadeService.getNoticeBillService().findCustomerByTelephone(model.getTelephone());
        push(c);
        return "findCustomerByTelephone";
    }


    @Action(value = "noticeBillAction_saveNoticeBill",
            results = {@Result(name = "saveNoticeBill",location = "/WEB-INF/pages/qupai/noticebill_add.jsp") })
    public String saveNoticeBill() {

        User loginUser = (User) getSessionAttribute("loginUser");
        model.setUser(loginUser);
        String province = getParameter("nprovince");
        String city = getParameter("ncity");
        String district = getParameter("ndistrict");
        facadeService.getNoticeBillService().saveNoticeBill(model,province,city,district);

        return "saveNoticeBill";
    }

}
