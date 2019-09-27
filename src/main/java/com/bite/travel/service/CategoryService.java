package com.bite.travel.service;

import com.bite.travel.po.Category;

import java.util.List;

/**
 * 旅游分类的业务接口层
 */
public interface CategoryService {
    List<Category> findAll();
}
