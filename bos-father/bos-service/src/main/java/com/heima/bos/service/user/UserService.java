package com.heima.bos.service.user;

import com.heima.bos.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    void save(User user);

    void delete(User user);

    User findUserById(Integer id);

    List<User> findAll();

    User login(String username, String password);

    User findUserByTelephone(String telephone);

    void updatePasswordByTelephone(String newPassword, String telephone);

    void changePasswordWhenLoggedIn(String newPassword, String email);

    User findUserByEmail(String username);

    void saveUser(User model, String[] roleIds);

    Page<User> pageQuery(Pageable pageRequest);

    void delUser(User model, String userIds);
}
