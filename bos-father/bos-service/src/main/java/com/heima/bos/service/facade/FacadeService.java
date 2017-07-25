package com.heima.bos.service.facade;

import com.heima.bos.service.bc.*;
import com.heima.bos.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


}
