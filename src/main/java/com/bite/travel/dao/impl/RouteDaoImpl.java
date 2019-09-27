package com.bite.travel.dao.impl;


import com.bite.travel.dao.RouteDao;
import com.bite.travel.po.Route;
import com.bite.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * 旅游线路商品的dao的实现层
 */
public class RouteDaoImpl implements RouteDao {
    //声明查询模板对象
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource()) ;
    @Override
    public int findTotalPage(int cid) {


        /**
         * 通过分类id查询总记录数
         * @param cid
         * @return
         */
        //准备sql
        String sql = "select  count(*) from tab_route where cid = ?" ;
        //查询
        return template.queryForObject(sql,Integer.class,cid) ;

    }

    /**
     * 查询当前页面数据集合,通过分页查询
     * @param cid
     * @param start
     * @param pageSize
     * @return
     */
    @Override
    public List<Route> findByPage(int cid, int start, int pageSize) {
        //准备sql
        String sql = "SELECT * FROM tab_route where  cid = ? limit ?,?" ;
        //查询
        return template.query(sql,new BeanPropertyRowMapper<Route>(Route.class),cid,start,pageSize) ;
    }
}
