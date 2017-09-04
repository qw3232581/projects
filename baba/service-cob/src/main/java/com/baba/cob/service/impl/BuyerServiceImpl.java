package com.baba.cob.service.impl;

import com.baba.dao.BuyerDAO;
import com.baba.pojo.Buyer;
import com.baba.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jamayette
 *         Created on  2017/8/20
 */
@Service("buyerService")
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    BuyerDAO buyerDAO;

    @Override
    public Buyer findByUsername(String username) {
        Buyer buyer = new Buyer();
        buyer.setUsername(username);
        return buyerDAO.selectOne(buyer);
    }
}
