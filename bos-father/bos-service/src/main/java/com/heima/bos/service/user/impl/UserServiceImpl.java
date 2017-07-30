package com.heima.bos.service.user.impl;

import com.heima.bos.dao.user.UserDao;
import com.heima.bos.domain.user.User;
import com.heima.bos.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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


}
