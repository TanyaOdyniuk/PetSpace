package com.netcracker.service.category;

import com.netcracker.model.category.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    List<Category> getCategories();
    int getPageCountAfterCatFilter(Category[] categories);
}
