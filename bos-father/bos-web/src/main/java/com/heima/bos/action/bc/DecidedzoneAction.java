package com.heima.bos.action.bc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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

import com.heima.bos.base.BaseAction;
import com.heima.bos.domain.bc.Decidedzone;
import com.heima.bos.domain.bc.Staff;
import com.heima.bos.domain.bc.Subarea;
import com.heima.bos.domain.customer.Customer;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("bos")
public class DecidedzoneAction extends BaseAction<Decidedzone> {
    private String[] ids;
    public void setIds(String[] ids) {
        this.ids = ids;
    }
    
    private String association;
    public void setAssociation(String association) {
        this.association = association;
    }

    @Action(value = "decidedzoneAction_saveDecidedzone", results = {
            @Result(name = "saveDecidedzone", location = "/WEB-INF/pages/base/decidedzone.jsp")})
    public String saveDecidedzone() {
        try {
            facadeService.getDecidedzoneService().saveDecidedzone(model, ids);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "saveDecidedzone";
    }
    
    
    @Action(value = "decidedzoneAction_ajaxList", results = {
    		@Result(name = "ajaxList", type="fastJson",params={"includeProperties","id,name,staff,telephone,station"})})
    public String ajaxList() {
    	try {
    		List<Decidedzone> decidedzones = facadeService.getDecidedzoneService().findAll();
    		push(decidedzones);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return "ajaxList";
    }
    
    @Action(value = "decidedzoneAction_pageQuery")
    public String pageQuery() {
    	Page<Decidedzone> pageData = facadeService.getDecidedzoneService().pageQuery(getPageRequest(), getSpecification());
        setPageDatas(pageData);
        return "pageQuery";
    	
    }

	private Specification<Decidedzone> getSpecification() {
		  Specification<Decidedzone> spec = new Specification<Decidedzone>() {
	            public Predicate toPredicate(Root<Decidedzone> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
	                List<Predicate> list = new ArrayList<>();
	                if (StringUtils.isNotBlank(model.getId())) {
	                    Predicate p1 = cb.equal(root.get("id").as(String.class), model.getId());
	                    list.add(p1);
	                }
	                if (model.getStaff()!=null) {
						Join<Decidedzone, Staff> staffJoin = root.join(root.getModel().getSingularAttribute("staff",Staff.class),JoinType.LEFT);
						if (StringUtils.isNotBlank(model.getStaff().getStation())) {
							Predicate p2 = cb.like(staffJoin.get("station").as(String.class),"%"+model.getStaff().getStation()+"%");
							list.add(p2);
						}
	                }
	                if (StringUtils.isNotBlank(association)) {
						if ("1".equals(association)) {
							Predicate p3 = cb.isNotEmpty(root.get("subareas").as(Set.class));
							list.add(p3);
						} else if ("0".equals(association)) {
							Predicate p3 = cb.isEmpty(root.get("subareas").as(Set.class));
							list.add(p3);
						}
					}

	                Predicate[] ps = new Predicate[list.size()];
	                return cb.and(list.toArray(ps));
	            }
	        };

	        return spec;
	}
	
	@Action(value = "decidedzoneAction_findNoAssociationCustomers", results = {
    		@Result(name = "findNoAssociationCustomers", type="fastJson",params={"includeProperties","id,name"})})
    public String findNoAssociationCustomers() {
    	try {
    		List<Customer> customers = facadeService.getDecidedzoneService().findNoAssociationCustomers();
    		push(customers);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return "findNoAssociationCustomers";
    }
	
	@Action(value = "decidedzoneAction_findAssociatedCustomers", results = {
			@Result(name = "findAssociatedCustomers", type="fastJson",params={"includeProperties","id,name"})})
	public String findAssociatedCustomers() {
		try {
			List<Customer> customers = facadeService.getDecidedzoneService().findAssociatedCustomers(model.getId());
    		push(customers);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "findAssociatedCustomers";
	}
	
	@Action(value = "decidedzoneAction_assignCustomersToDecidedzone", results = {
            @Result(name = "assignCustomersToDecidedzone", location = "/WEB-INF/pages/base/decidedzone.jsp")})
	public String assignCustomersToDecidedzone() {
		String[] customerIds = ServletActionContext.getRequest().getParameterValues("customerIds");
		facadeService.getDecidedzoneService().assignCustomersToDecidedzone(model.getId(),customerIds);
		return "assignCustomersToDecidedzone";
	}
	
	@Action(value = "decidedzoneAction_findAssociationSubarea", results = {
			@Result(name = "findAssociationSubarea",type="fastJson" )})
	public String findAssociationSubarea() {
		List<Subarea> subareas = facadeService.getSubareaService().findAssociationSubarea(model.getId());
		push(subareas);
		return "findAssociationSubarea";
	}
	
	@Action(value = "decidedzoneAction_findAssociationCustomer", results = {
			@Result(name = "findAssociationCustomer",type="fastJson" ,params={"includeProperties","id,name,station"})})
	public String findAssociationCustomer() {
		try {
			List<Customer> customers = facadeService.getDecidedzoneService().findAssociatedCustomers(model.getId());
			push(customers);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "findAssociationCustomer";
	}

	
}
