package com.heima.bos.shiro.realm;

import com.heima.bos.domain.auth.Function;
import com.heima.bos.domain.auth.Role;
import com.heima.bos.domain.user.User;
import com.heima.bos.service.facade.FacadeService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

public class BosRealm extends AuthorizingRealm {
    @Autowired
    protected FacadeService facadeService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        Subject subject = SecurityUtils.getSubject();
        User existUser = (User) subject.getPrincipal();
        //超级管理员
        if ("1@1.com".equals(existUser.getEmail())){
            List<Role> roles = facadeService.getRoleService().findAll();
            //存放所有角色和权限关键字
            for (Role role : roles) {
                info.addRole(role.getCode());
            }
            List<Function> functions = facadeService.getFunctionService().findAll();
            for (Function function : functions) {
                info.addStringPermission(function.getCode());
            }
        }else {
            List<Role> roles = facadeService.getRoleService().findAllRolesByUserId(existUser.getId());
            for (Role role : roles) {
                info.addRole(role.getCode());
                Set<Function> functions = role.getFunctions();
                for (Function function : functions) {
                    info.addStringPermission(function.getCode());
                }
            }
        }

        return info;
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
