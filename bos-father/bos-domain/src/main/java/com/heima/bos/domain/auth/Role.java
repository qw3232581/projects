package com.heima.bos.domain.auth;
// Generated 2017-7-30 16:54:52 by Hibernate Tools 3.2.2.GA


import com.heima.bos.domain.user.User;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Role generated by hbm2java
 */
@Entity
@Table(name="auth_role"
    ,catalog="bos"
    , uniqueConstraints = @UniqueConstraint(columnNames="name") 
)
public class Role  implements java.io.Serializable {


     private String id;
     private String name;
     private String code;
     private String description;
     private Set<Menu> menus = new HashSet<Menu>(0);
     private Set<User> users = new HashSet<User>(0);
     private Set<Function> functions = new HashSet<Function>(0);

    public Role() {
    }

    public Role(String name, String code, String description, Set<Menu> menus, Set<User> users, Set<Function> functions) {
       this.name = name;
       this.code = code;
       this.description = description;
       this.menus = menus;
       this.users = users;
       this.functions = functions;
    }

     @GenericGenerator(name="generator", strategy="uuid")@Id @GeneratedValue(generator="generator")

    @Column(name="id", unique=true, nullable=false, length=32)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name="name", unique=true)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name="code")
    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name="description")
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name="role_menu", catalog="bos", joinColumns = {
        @JoinColumn(name="role_id", nullable=false, updatable=false) }, inverseJoinColumns = {
        @JoinColumn(name="menu_id", nullable=false, updatable=false) })
    public Set<Menu> getMenus() {
        return this.menus;
    }

    public void setMenus(Set<Menu> menus) {
        this.menus = menus;
    }
@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name="user_role", catalog="bos", joinColumns = {
        @JoinColumn(name="role_id", nullable=false, updatable=false) }, inverseJoinColumns = {
        @JoinColumn(name="user_id", nullable=false, updatable=false) })
    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name="role_function", catalog="bos", joinColumns = {
        @JoinColumn(name="role_id", nullable=false, updatable=false) }, inverseJoinColumns = {
        @JoinColumn(name="function_id", nullable=false, updatable=false) })
    public Set<Function> getFunctions() {
        return this.functions;
    }
    
    public void setFunctions(Set<Function> functions) {
        this.functions = functions;
    }




}


