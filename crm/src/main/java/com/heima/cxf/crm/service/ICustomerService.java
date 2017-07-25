package com.heima.cxf.crm.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.heima.cxf.crm.domain.Customer;

@Produces("*/*")
public interface ICustomerService {

	@GET
	@Path("/customer")
	@Produces({ "application/xml", "application/json" })
	public List<Customer> getNoAssociations();

	@GET
	// 客户端 查询请求
	@Path("/customer/{decidezoneId}")
	// http://xxxx/user/1
	@Consumes({ "application/xml", "application/json" })
	// 客户端 只能发送 xml 数据类型
	@Produces({ "application/xml", "application/json" })
	public List<Customer> getInUseAssociations(@PathParam("decidezoneId") String decidezone_id);

	@PUT
	@Path("/customer/{decidezoneId}/{cids}")
	// /customer/1,2,3/dq001
	@Consumes({ "application/xml", "application/json" })
	public void assignCustomerToDecidedZone(@PathParam("cids") String customerids, @PathParam("decidezoneId") String decidedZone_id);

	
}
