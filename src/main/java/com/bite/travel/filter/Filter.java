package com.bite.travel.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "Filter",
urlPatterns = "*.html")
public class Filter implements javax.servlet.Filter {
    private String charset;
    public void destroy() {
        //销毁时用到的方法

        System.out.println("你要被销毁啦, 哈哈哈!");
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
       // * 过滤方法, 主要对request, response做一些处理, 然后交给下一个过滤器或者Servlet处理
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        System.out.println(
                "suanni哼"
        );
        String uri = request.getRequestURI();
        int index = uri.lastIndexOf("/");
        String endWith = uri.substring(index+1);
        if ("route_list.html".equals(endWith)){
            response.sendRedirect("login.html");
        } else {
            chain.doFilter(req,response);
        }
    }

    private boolean isLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String name = "";
        String pwd = "";
     if (cookies != null){
         for (Cookie cookie : cookies){
             if (cookie != null && "name".equals(cookie.getName())){
                  name = cookie.getValue();

             }
             if (cookie != null && "pwd".equals(cookie.getName())){
                  pwd = cookie.getValue();
             }
         }
     }

     if (name.equals("")||pwd.equals("")){
         return false;
     }
     return true;
    }

    public void init(FilterConfig config) throws ServletException {
//初始化参数

        String filterName = config.getFilterName();
        System.out.println(filterName);
    }

}
