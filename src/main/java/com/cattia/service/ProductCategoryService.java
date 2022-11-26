package com.cattia.service;

import com.cattia.entity.ProductCategoryEnum;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductCategoryService {

    public List<ProductCategoryEnum> getAllActiveProductCategories() {
        return Arrays.asList(ProductCategoryEnum.values());
    }
}
