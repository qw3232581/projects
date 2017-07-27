package com.heima.bos.service.facade;

import com.heima.bos.service.qp.NoticeBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heima.bos.service.bc.DecidedzoneService;
import com.heima.bos.service.bc.RegionService;
import com.heima.bos.service.bc.StaffService;
import com.heima.bos.service.bc.StandardService;
import com.heima.bos.service.bc.SubareaService;
import com.heima.bos.service.city.CityService;
import com.heima.bos.service.user.UserService;

@Service
public class FacadeService {

    @Autowired
    private UserService userService;
    @Autowired
    private StandardService standardService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private SubareaService subareaService;
    @Autowired
    private DecidedzoneService decidedzoneService;
    @Autowired
    private CityService cityService;
    @Autowired
    private NoticeBillService noticeBillService;

    public UserService getUserService() {
        return userService;
    }

    public StandardService getStandardService() {
        return standardService;
    }

    public StaffService getStaffService() {
        return staffService;
    }

    public RegionService getRegionService() {
        return regionService;
    }

    public SubareaService getSubareaService() {
        return subareaService;
    }

    public DecidedzoneService getDecidedzoneService() {
        return decidedzoneService;
    }
    
    public CityService getCityService() {
    	return cityService;
    }

    public NoticeBillService getNoticeBillService()  {return noticeBillService;}
}
