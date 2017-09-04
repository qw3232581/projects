package com.taotao.sso.service;

import com.taotao.sso.bean.User;

public interface UserQueryService {

    /**
     * 根据token查询用户信息
     * 
     * @param token
     * @return
     */
    public User queryUserByToken(String token);
}
