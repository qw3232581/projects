package com.taotao.cart.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PropertieService {
    
    @Value("${STATIC_TAOTAO_URL}")
    public String STATIC_TAOTAO_URL;

}
