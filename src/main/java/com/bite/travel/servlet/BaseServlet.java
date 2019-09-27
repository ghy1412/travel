package com.bite.travel.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 抽查的基类
 */

//RegistUserServlet
    ///registUserServlet
public class BaseServlet extends HttpServlet {
   /* protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }*/

   //重写HttpServlet中的service方法(serlvet的入口)

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // /   travle/user/add  :获取访问子类Servlet的uri的路径
        String uri = request.getRequestURI();
        System.out.println(uri);
        // 获取uri中的方法名add   travel/user/add
        String methodName = uri.substring(uri.lastIndexOf("/")+1)  ;//start end
        System.out.println(methodName);

        //1 .获取子类对象
        Class clazz = this.getClass();
        System.out.println(clazz);
        //2.调用子类中的方法
        //暴力反射(获取子类私有的/受保护的方法)
        try {
//            Method method = clazz.getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            Method method = clazz.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            //调用方法
            /**
             * 为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查
             */
            //(调用子类的方法,proptected修饰)
//            method.setAccessible(true);
            method.invoke(this,request,response) ;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


    }

    /**
     * 直接将obj对象转换成json格式,写回到前台
     * @param obj
     * @param response
     * @throws IOException
     */
    public void writeValue(Object obj,HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper() ;
        //处理服务器的响应的格式:json格式
        response.setContentType("application/json;charset=utf-8");
        //写回
        mapper.writeValue(response.getOutputStream(),obj);
    }

    /**
     * 将当前对象解析json串返回前台
     * @param obj
     * @return
             * @throws JsonProcessingException
     */
    public String writeValueAsString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper() ;
        String json = mapper.writeValueAsString(obj);
        return json;
    }
}
