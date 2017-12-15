package com.netcracker.controller.category;

import com.netcracker.model.category.Category;
import com.netcracker.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping
    public List<Category> getCategories(){
        return categoryService.getCategories();
    }
    @PostMapping("/getPageCountAfterCatFilter")
    public int getCountAfterCatFilter(@RequestBody Category[] categories){
        return categoryService.getPageCountAfterCatFilter(categories);
    }
    @PostMapping("/getPageCountAfterCatFilter/{id}")
    public int getCountAfterCatFilterForProfile(@PathVariable("id") Integer id, @RequestBody Category[] categories){
        return categoryService.getPageCountAfterCatFilterForProfile(categories, id);
    }

}
