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

@WebServlet("/registUserServlet")
public class RegistUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //加入验证码的操作
        //获取前台页面中的验证码
        String check = request.getParameter("check");
        //从session域中获取服务器端存储随机的验证码
        String  checkcode_server = (String) request.getSession().
                getAttribute("CHECKCODE_SERVER");
        //一次性验证,防止验证码被复用

        request.getSession().removeAttribute("CHECKCODE_SERVER");
        //如果验证码不一致,后面代码不执行了
        //防止nullPointerException
        if(checkcode_server==null || !checkcode_server.equalsIgnoreCase(check)){

            //验证码存在问题
            //创建响应结果对象
            ResultInfo resultInfo = new ResultInfo() ;
            //设置响应数
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("验证码存在问题!!");
            //创建json的解析对象 (jackson的jar包核心类)
            ObjectMapper mapper = new ObjectMapper() ;
            String json = mapper.writeValueAsString(resultInfo);
            //设置服务器响应格式 :json类型
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            //不执行了
            return ;
        }


        //1.获取参数数据
        Map<String, String[]> map = request.getParameterMap();
        //2.封装User对象
        User user = new User() ;
        //使用commons-beanutils工具类 :封装Javabean
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //3.调用Service,查询是否存在该用户
        UserService userService = new UserServiceImpl() ;
        boolean flag = userService.regist(user) ;
        //创建响应结果对象:ResultInfo
        ResultInfo resultInfo = new ResultInfo() ;
        //判断用注册是否成功
        if(flag){
            //注册成功了
            resultInfo.setFlag(true);
        }else{
            //注册失败
            resultInfo.setFlag(false);
            //提示信息
            resultInfo.setErrorMsg("注册失败!!!");
        }
        //4.服务器将数据响应到前台,(resultInfo)
        //json:jsonLib /Gson/fastJson
        //创建json的解析对象 (jackson的jar包核心类)
        ObjectMapper mapper = new ObjectMapper() ;
        String json = mapper.writeValueAsString(resultInfo);
        //设置服务器响应格式 :json类型
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request,response);

    }
}
