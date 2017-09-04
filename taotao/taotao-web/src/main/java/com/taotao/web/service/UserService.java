package com.taotao.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.sso.bean.User;
import com.taotao.sso.service.UserQueryService;

@Service
public class UserService {
    
    @Value("${TAOTAO_SSO_URL}")
    public String TAOTAO_SSO_URL;

    @Autowired
    private UserQueryService userQueryService;

    public User queryUserByToken(String token) {
        return this.userQueryService.queryUserByToken(token);
    }

}
