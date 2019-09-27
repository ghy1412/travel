package com.bite.travel.service;


import com.bite.travel.po.PageBean;
import com.bite.travel.po.Route;

/**
 * 旅游线路商品的业务接口层
 */
public interface RouteService {
    PageBean<Route> pageQuery(int cid, int currentPage, int pageSize);
}
