package com.bite.travel.service;


import com.bite.travel.po.User;

/**
 * 用户相关的业务接口层
 */
public interface UserService {
    /**
     * 注册
     * @param user
     * @return
     */
    boolean regist(User user);

    /**
     * 用户激活
     * @param code
     * @return
     */
    boolean active(String code);


    /**
     * 根据用户名和密码查询用户
     * @param user
     * @return
     */
    User findByUsernameAndPassword(User user);
}
