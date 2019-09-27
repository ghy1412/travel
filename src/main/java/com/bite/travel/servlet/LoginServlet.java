package com.bite.travel.servlet;


import com.bite.travel.po.ResultInfo;
import com.bite.travel.po.User;
import com.bite.travel.service.UserService;
import com.bite.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //验证码逻辑同注册逻辑
        //1.接收前台参数:username,password
        Map<String, String[]> map = request.getParameterMap();
        //2.封装User对象
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //3.调用service层完成查询用户
        UserService userService = new UserServiceImpl() ;
        User u = userService.findByUsernameAndPassword(user) ;
        //创建响应结果对象
        ResultInfo info = new ResultInfo() ;
        //4.判断查询的u是否存在
        if(u==null){
            //不存在,提示 用户名或者密码不匹配
            info.setFlag(false);
            info.setErrorMsg("用户名或者密码不匹配!!");
        }
        //如果用户存在,但是没有被激活
        if(u!=null &&  !"Y".equals(u.getStatus())){
            //尚未激活
            info.setFlag(false);
            info.setErrorMsg("您当前用户名尚未激活!!");
        }
        //判断登录成功
        if(u!=null && "Y".equals(u.getStatus())){
            //应该将u存储到session域中
            request.getSession().setAttribute("user",u);
            info.setFlag(true);
        }

        //将当前响应对象解析成json串发送前台
        ObjectMapper mapper = new ObjectMapper() ;
        //writerValueAsString(mapper,响应结果对象)
        //writerValue(字节输出流,响应结果对象)
        //需要设置响应格式:支持json格式
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),info);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request,response);
    }
}
