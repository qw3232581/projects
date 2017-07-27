package com.heima.cxf.crm.service;

import java.util.List;

import javax.ws.rs.*;

import com.heima.cxf.crm.domain.Customer;

@Produces("*/*")
public interface ICustomerService {

	@GET
	@Path("/customer/noAssociation")
	@Produces({ "application/xml", "application/json" })
	List<Customer> getNoAssociations();

	@GET
	@Path("/customer/{decidezoneId}")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	List<Customer> getInUseAssociations(@PathParam("decidezoneId") String decidezone_id);

	@PUT
	@Path("/customer/assignCustomerToDecidedZone/{decidezoneId}/{cids}")
	@Consumes({ "application/xml", "application/json" })
	void assignCustomerToDecidedZone(@PathParam("cids") String customerids, @PathParam("decidezoneId") String decidedZone_id);

	@GET
	@Path("/customer/findCustomerByTelephone/{telephone}")
	@Consumes({ "application/xml", "application/json" })
    @Produces({ "application/xml", "application/json" })
	Customer findCustomerByTelephone(@PathParam("telephone") String telephone);

	@GET
	@Path("/customer/updateAddressById/{cutomerId}/{pickAddress}")
	@Consumes({ "application/xml", "application/json" })
	void updateAddressById(@PathParam("cutomerId") Integer customerId,@PathParam("pickAddress") String pickAddress);

	@POST
	@Path("/customer/saveBillNotice")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	Customer saveBillNotice(Customer customer);

	@GET
	@Path("/customer/findCustomerByAddress/{pickAddress}")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	Customer findCustomerByAddress(@PathParam("pickAddress") String pickAddress);


}
