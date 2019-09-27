package com.bite.travel.service.impl;


import com.bite.travel.dao.UserDao;
import com.bite.travel.dao.impl.UserDaoImpl;
import com.bite.travel.po.User;
import com.bite.travel.service.UserService;
import com.bite.travel.util.MailUtils;
import com.bite.travel.util.UuidUtil;

/**
 * 用户相关的业务实现层
 */
public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl() ;
    /**
     * 用户注册功能
     * @param user
     * @return
     */
    @Override
    public boolean regist(User user) {
        //1.调用dao,通过用户名查询用户
        User u = userDao.findByUsername(user.getUsername());
        //判断当前用户是否为空
        if(u!=null){
            return false ;
        }
        //如果为空,直接返回true
        //保存用户
        //给用户设置激活码
        user.setCode(UuidUtil.getUuid()); //生成随机的激活码(唯一的)
        //设置当前用户的默认激活状态(N,没有激活)
        user.setStatus("N");
        userDao.saveUser(user);

        //注册成功了,激活邮件
        //发送邮件
        //收件人(邮箱)  邮件内容  邮件标题
        //public static boolean sendMail(String to, String text, String title)
        //邮件的内容
        String content = "您的邮箱尚未激活,请<a href='http://localhost/travel/user/active?code="+user.getCode()+"'>点击激活</a>" ;
        MailUtils.sendMail(user.getEmail(),content,"激活") ;
        return true;
    }

    /**
     * 用户激活
     * @param code
     * @return
     */
    @Override
    public boolean active(String code) {
        //调用userDao通过激活码查询用户
        User user = userDao.findByCode(code) ;
        //判断User是否存在
        if(user !=null){
         //更新用户的状态
            userDao.updateStatus(user) ;
            return true ;

        }else{
            return false;
        }

    }


    /**
     * 根据用户名和密码查询用户
     * @param user
     * @return
     */
    @Override
    public User findByUsernameAndPassword(User user) {
        return userDao.findByUsernameAndPassword(user.getUsername(),user.getPassword()) ;
    }
}
