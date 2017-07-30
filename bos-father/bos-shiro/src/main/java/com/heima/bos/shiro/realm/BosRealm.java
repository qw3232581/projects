package com.heima.bos.shiro.realm;

import com.heima.bos.domain.user.User;
import com.heima.bos.service.facade.FacadeService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class BosRealm extends AuthorizingRealm {
    @Autowired
    protected FacadeService facadeService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("---认证---");
        UsernamePasswordToken myToken = (UsernamePasswordToken) token;
        User existUser = facadeService.getUserService().findUserByEmail(myToken.getUsername());
        if (existUser == null){
            return  null;
        }else{
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(existUser,existUser.getPassword(),super.getName());
            return info;
        }
    }
}
