package com.bite.travel.servlet;

import com.bite.travel.po.Category;
import com.bite.travel.po.ResultInfo;
import com.bite.travel.po.User;
import com.bite.travel.service.UserService;
import com.bite.travel.service.impl.UserServiceImpl;
import com.bite.travel.util.JDBCUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import sun.security.jca.ServiceId;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Service;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Demo extends BaseServlet{

    public void register(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //1.比较验证码
        String check = request.getParameter("check");
        String check_reg = (String) request.getSession().getAttribute("CHECK_SERVER");
        ResultInfo resultInfo = new ResultInfo();
        if (check_reg == null || !check_reg.equals(check)){
            //返回消息
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("验证码错误!");
            ObjectMapper mapper = new ObjectMapper();
            response.setContentType("application/json;charset=utf-8");
            mapper.writeValue(response.getOutputStream(),resultInfo);
        }

        //获取表单参数
        Map<String, String[]> map = request.getParameterMap();
        //转化为User,采用BeanUtils的populate(bean, map);方法  无返回值
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        UserServiceImpl userService = new UserServiceImpl();
        boolean flag = userService.regist(user);

        if (flag){
            resultInfo.setFlag(true);
            resultInfo.setErrorMsg("");
        } else {
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("注册失败");
        }
      //  response.setContentType("application/json;charset=utf-8");
        writeValue(resultInfo, response);

        //userDao
        JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
        String sql = "select * from user where username = ? and password = ?";
        User user1 = (User) jdbcTemplate.query(sql, new BeanPropertyRowMapper<User>(User.class), user.getUsername());
        //返回结果 为false 就返回 user 调用产生随机码, 设置激活状态,和随机嘛  然后调用save()方法 true
        //您的邮箱尚未激活,请<a href='http://localhost/travel/user/active?code="+user.getCode()+"'>点击激活</a>" ;
        String content = "你的邮箱尚未激活请点击<a href='http://localhost/travel/user/active?code="+user.getCode()+"'>激活</a>";
        //并且发送激活码到用户邮箱  MailUtils.sendMail(email, content,title);
        //为true 就直接返回false 因为数据库中已经有相同的用户账号
    }


    protected void active(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");
        //调用业务逻辑active()
        UserServiceImpl userService = new UserServiceImpl();
        boolean flag = userService.active(code);
        ResultInfo resultInfo = new ResultInfo();
        String msg = "";
        if (flag){
            msg = "xxx";
        } else {
            msg = "xxx";
        }
        response.setContentType("application/json;charset=utf-8");
        writeValue(msg, response);
    }

    //登录 就是 先验证验证码
    //调用业务逻辑, 根据username,password查询,返回user ,如果status为N 就是未激活 提示激活 如果为nul 提示用户名或者密码错误


    protected void login(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //接受表单参数比较此处省略
        //直接调用业务逻辑, ===在本类中瞎几把胡写
        User user = new User();
        User user1 = loginService(user);
        ResultInfo resultInfo = new ResultInfo();
        if (user1 == null){
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("用户名或密码错误");
        } else {
            if (user1.getStatus().equals("N")){
                resultInfo.setFlag(false);
                resultInfo.setErrorMsg("你的邮箱尚未激活,请前往邮箱激活!");
            } else {
                resultInfo.setFlag(true);
                //要将信息存储在Session域中, 那个欢迎回来的界面
                request.getSession().setAttribute("user", user1);
                resultInfo.setErrorMsg("你已经成功登陆!");
            }
        }
        writeValue(resultInfo, response);
    }

    private User loginService(User user) {
        User user1 = null;
        user1 = daofindUser(user.getName(), user.getPassword());
        return  user1;
    }

    private User daofindUser(String name, String password) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
        String sql = "select * from tab_user where username =? and password = ?";
        User user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), name, password);
        return user;
    }

    //category 的分类查询 findAll

    protected void category(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //直接调用后台的方法,findAll();
        List<Category> list = findAll();
        if (list != null){
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), list);
        }
    }

    private List<Category> findAll() {

        //先从Redis获取存储数据
        //获取Jedis客户端
        Jedis jedis = new Jedis();
        Set<Tuple> category = jedis.zrangeWithScores("category", 0, -1);
        List<Category> categoryList = null;

        if (category == null){
            categoryList = findcategory();

            for (int i = 0; i < categoryList.size(); i++) {
                jedis.zadd("category", categoryList.get(i).getCid(), categoryList.get(i).getCname());
            }
        } else {
            for (Tuple tuple : category){
                Category category1 = new Category();
                category1.setCid((int) tuple.getScore());
                category1.setCname(tuple.getElement());
                categoryList.add(category1);
            }
        }
        return categoryList;
    }

    private List<Category> findcategory() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
        String sql = "select * from category";
        List<Category> query = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Category.class));
        return query;
    }
}
