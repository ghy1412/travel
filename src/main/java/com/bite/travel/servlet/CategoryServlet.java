package com.bite.travel.servlet;


import com.bite.travel.po.Category;
import com.bite.travel.service.CategoryService;
import com.bite.travel.service.impl.CategoryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/category/*")
public class CategoryServlet extends BaseServlet {

    private CategoryService categoryService = new CategoryServiceImpl() ;
    /**
     * 查询分类信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findAll(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //1.调用CateService的findAll查询
        List<Category> list = categoryService.findAll() ;



        //2.将list集合写回到前台
        //创建解析器对象
       /* ObjectMapper mapper = new ObjectMapper() ;
        //处理服务器的响应的格式:json格式
        response.setContentType("application/json;charset=utf-8");
        //写回
        mapper.writeValue(response.getOutputStream(),list)*/;

        writeValue(list,response);


    }


}
