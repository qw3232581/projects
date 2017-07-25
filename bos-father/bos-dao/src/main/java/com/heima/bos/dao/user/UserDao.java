package com.heima.bos.dao.user;

import com.heima.bos.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserDao extends JpaRepository<User, Integer> {

    @Query("from User where email = ?1 and password = ?2")
    User login(String username, String password);

    @Query("from User where telephone = ?1")
    User findUserByTelephone(String telephone);

    @Modifying
    @Query("update User u set u.password = ?1 where u.telephone = ?2")
    void updatePasswordByTelephone(String newPassword, String telephone);

    @Modifying
    @Query("update User u set u.password = ?1 where u.email = ?2")
    void changePasswordWhenLoggedIn(String newPassword, String email);


}
