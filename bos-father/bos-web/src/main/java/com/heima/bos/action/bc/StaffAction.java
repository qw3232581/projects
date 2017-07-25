package com.heima.bos.action.bc;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.heima.bos.base.BaseAction;
import com.heima.bos.domain.bc.Staff;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("bos")
public class StaffAction extends BaseAction<Staff> {

    @Action(value = "staffAction_validPhone", results = {
            @Result(name = "validPhone", type = "fastJson")})
    public String validPhone() {
        try {
            String telephone = this.getParameter("telephone");
            Staff staff = facadeService.getStaffService().validTelephone(telephone);
            if (staff == null) {
                push(true);
            } else {
                push(false);
            }
        } catch (Exception e) {
            push(false);
            e.printStackTrace();
        }
        return "validPhone";
    }

    //添加Staff
    @Action(value = "staffAction_saveStaff", results = {
            @Result(name = "saveStaff", location = "/WEB-INF/pages/base/staff.jsp")})
    public String saveStaff() {
        try {
            facadeService.getStaffService().saveStaff(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "saveStaff";
    }

    @Action(value = "staffAction_pageQuery")
    public String pageQuery() {
        Specification<Staff> spec = new Specification<Staff>() {
            @Override
            public Predicate toPredicate(Root<Staff> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (StringUtils.isNotBlank(model.getName())) {
                    Predicate p1 = cb.like(root.get("name").as(String.class), "%" + model.getName() + "%");
                    predicates.add(p1);
                }
                if (StringUtils.isNotBlank(model.getTelephone())) {
                    Predicate p2 = cb.like(root.get("telephone").as(String.class), "%" + model.getTelephone() + "%");
                    predicates.add(p2);
                }
                if (StringUtils.isNotBlank(model.getStation())) {
                    Predicate p3 = cb.like(root.get("station").as(String.class), "%" + model.getStation() + "%");
                    predicates.add(p3);
                }
                if (StringUtils.isNotBlank(model.getStandard())) {
                    Predicate p4 = cb.like(root.get("standard").as(String.class), "%" + model.getStandard() + "%");
                    predicates.add(p4);
                }
                Predicate ps[] = new Predicate[predicates.size()];
                return cb.and(predicates.toArray(ps));
            }
        };

        Page<Staff> pageData = facadeService.getStaffService().pageQuery(getPageRequest(), spec);
        setPageDatas(pageData);
        return "pageQuery";
    }

    @Action(value = "staffAction_delStaff", results = {
            @Result(name = "delStaff", type = "fastJson")})
    public String delStaff() {
        try {
            String idsString = this.getParameter("ids");
            if (StringUtils.isNotBlank(idsString)) {
                String[] ids = idsString.split(",");
                for (String standardId : ids) {
                    facadeService.getStaffService().delStaff(standardId);
                }
                push(true);
            } else {
                push(false);
            }
        } catch (Exception e) {
            push(false);
            e.printStackTrace();
        }
        return "delStaff";
    }

    @Action(value = "staffAction_restoreBatch", results = {
            @Result(name = "restoreBatch", type = "fastJson")})
    public String restoreBatch() {
        try {
            String idsString = this.getParameter("ids");
            if (StringUtils.isNotBlank(idsString)) {
                String[] ids = idsString.split(",");
                for (String standardId : ids) {
                    facadeService.getStaffService().restoreStaff(standardId);
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

    @Action(value = "staffAction_ajaxList", results = {
            @Result(name = "ajaxList", type = "fastJson")})
    public String ajaxList() {
        List<Staff> staffs = facadeService.getStaffService().findAllAvailable();
        push(staffs);
        return "ajaxList";
    }

}
