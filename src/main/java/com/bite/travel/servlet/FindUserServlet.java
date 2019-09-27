package com.bite.travel.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/findUserServlet")
public class FindUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //1.直接从session域中获取用户对象
        Object user = request.getSession().getAttribute("user");
        //判断用户是否为空
            //服务器将user对象响应给前台页面:header.html(通过index.html)
            ObjectMapper mapper = new ObjectMapper() ;
            //支持json格式的响应
            response.setContentType("application/json;charset=utf-8");
            mapper.writeValue(response.getOutputStream(),user);
            //{uid:xx,name:'xx'}

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}
