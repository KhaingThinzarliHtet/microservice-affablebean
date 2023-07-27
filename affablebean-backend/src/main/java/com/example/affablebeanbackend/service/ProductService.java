package com.example.affablebeanbackend.service;

import com.example.affablebeanbackend.dao.CategoryDao;
import com.example.affablebeanbackend.dao.ProductDao;
import com.example.affablebeanbackend.entity.Products;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductDao productDao;
    private final CategoryDao categoryDao;

    public Products listAllProducts(){
        return new Products(productDao.findAll().spliterator());
    }
}
