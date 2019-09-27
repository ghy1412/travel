package com.bite.travel.dao;


import com.bite.travel.po.User;

/**
 * 用户相关的数据库访问接口层
 */
public interface UserDao {
    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    public User findByUsername(String username) ;


    /**
     * 保存用户
     * @param user
     */
    public void saveUser(User user) ;

    /**
     * 根据激活码查询对应的用户
     * @param code
     * @return
     */
    User findByCode(String code);

    /**
     * 更新用户的激活状态(N--Y)
     * @param user
     */
    void updateStatus(User user);

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    User findByUsernameAndPassword(String username, String password);
}
