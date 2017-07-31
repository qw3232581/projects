package com.heima.bos.service.user.impl;

import com.heima.bos.dao.user.UserDao;
import com.heima.bos.domain.auth.Role;
import com.heima.bos.domain.user.User;
import com.heima.bos.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.PushbackInputStream;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public void delete(User user) {
        userDao.delete(user);
    }

    @Override
    public User findUserById(Integer id) {
        return userDao.findOne(id);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }


    @Override
    public User login(String username, String password) {
        return userDao.login(username, password);
    }

    @Override
    public User findUserByTelephone(String telephone) {
        return userDao.findUserByTelephone(telephone);
    }

    @Override
    public void updatePasswordByTelephone(String newPassword, String telephone) {
        userDao.updatePasswordByTelephone(newPassword, telephone);
    }

    @Override
    public void changePasswordWhenLoggedIn(String newPassword, String email) {
        userDao.changePasswordWhenLoggedIn(newPassword, email);
    }

    @Override
    public User findUserByEmail(String username) {
        return userDao.findUserByEmail(username);
    }

    @Override
    public void saveUser(User model, String[] roleIds) {
        userDao.saveAndFlush(model);
        if (roleIds!=null || roleIds.length != 0){
            Set<Role> roles = model.getRoles();
            for (String rid : roleIds) {
                Role r = new Role();
                r.setId(rid);
                roles.add(r);
            }
        }
    }

    @Override
    public Page<User> pageQuery(Pageable pageRequest) {
        return userDao.findAll(pageRequest);
    }

}
