package com.bite.travel.dao;

import com.bite.travel.po.Category;

import java.util.List;

/**
 * 分类的dao 接口层
 */
public interface CategoryDao {
    List<Category> findAll();
}
