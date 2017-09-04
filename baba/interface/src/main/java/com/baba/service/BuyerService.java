package com.baba.service;

import com.baba.pojo.Brand;
import com.baba.pojo.Buyer;
import com.baba.utils.PageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : Jamayette
 * Created on 2017/8/20
 */
@Service
public interface BuyerService {

    /**
     * 通过买家用户名，查询买家
     * @param username
     * @return
     */
     Buyer findByUsername(String username);


}
