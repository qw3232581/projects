package com.heima.bos.action.city;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.heima.bos.base.BaseAction;
import com.heima.bos.domain.city.City;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("bos")
public class CityAction extends BaseAction<City> {
    
	@Action(value = "loadCityAction_load", results = { @Result(name = "load", type = "json") })
	public String load() {
		
//		List<City> citys = facadeService.getCityService().findAll(model.getPid());
//		push(cities);
		
		try {
			HttpServletResponse response = getResponse();
			response.setContentType("text/json;charset=utf-8");
			String fromRedis = facadeService.getCityService().findCityByPidFromRedis(model.getPid());
			response.getWriter().println(fromRedis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
}
