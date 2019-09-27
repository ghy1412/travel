package com.bite.travel.service.impl;


import com.bite.travel.dao.CategoryDao;
import com.bite.travel.dao.impl.CategoryDaoImpl;
import com.bite.travel.po.Category;
import com.bite.travel.service.CategoryService;
import com.bite.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 旅游分类业务接口实现层
 * 最终优化(将list集合存储到redis)
 */
public class CategoryServiceImpl implements CategoryService {
    //声明dao层对象
    private CategoryDao categoryDao = new CategoryDaoImpl() ;

    @Override
    public List<Category> findAll() {


        //每一次加载首页,都有从数据库去读取分类的信息
        //缺点:服务器压力大
        //用户体验差(等待页面整个加载完)
        //改进:使用redis技术
        /***
         * 1.应该从JedisUtils获取redis的客户端的对象:Jedis
         */
        Jedis jedis = JedisUtil.getJedis();

        //2.获取到客户端对象,操作redis数据库
        //应该先判断redis是否存在一个key名字"category"的信息范围(Set集合)
       // zrange key 0 -1
        //查询当前key中记录的所有信息   //zadd key field  value
       // Set<String> categorys = jedis.zrange("category", 0, -1);

        //改进:获取分类id
        Set<Tuple> categorys = jedis.zrangeWithScores("category",0,-1) ;

        //reids 数据结构:sortedset

        //声明List<Category>集合对象
        List<Category> list = null ;
        //3.判断当前redis是否存在categorys集合数据
        if(categorys==null || categorys.size()==0){
            System.out.println("从数据库中获取分类信息...");
            //调用CategoryDao中查询所有分类信息
            list = categoryDao.findAll() ; //第一次从数据库如果查询到了,将信息直接存储到redis中
            //遍历集合,获取每一个Category对象
            for(int i = 0 ; i <list.size() ; i++){
                //将当前cid和cname存储到redis中
                //查询旅游分类的value

                jedis.zadd("category",list.get(i).getCid(),list.get(i).getCname()) ;
            }

        }else{
            //不为空,将Set<String>---->List<Category>
            System.out.println("从redis数据库中获取分类信息....");
            //创建List<Category>对象
            list = new ArrayList<Category>() ;
            //创建Category对象
            /*Category category = new Category() ;
            for(String name:categorys){
//                    category.setCid(category.getCid()); //0
                    category.setCname(name);
                    list.add(category) ;
            }*/

          ;
            for(Tuple tuple:categorys){
                Category category = new Category() ;
                //Tuple :getElement():获取成员信息(分类名称)
                //获取score(对应的分类id)
                category.setCname(tuple.getElement());
                category.setCid((int)tuple.getScore());
                //将分类的实体对象添加到List集合
                list.add(category) ;
            }


        }

//        return categoryDao.findAll();

        return list ;
    }


}
