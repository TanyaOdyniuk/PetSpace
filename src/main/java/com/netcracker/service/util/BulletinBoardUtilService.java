package com.netcracker.service.util;

import com.netcracker.model.category.Category;
import org.springframework.stereotype.Service;

@Service
public class BulletinBoardUtilService {

    public String getFilterCategoryAdditionQuery(Category[] categories){
        String additionalParam;
        if (categories.length == 1) {
            additionalParam = " = " + categories[0].getObjectId();
        } else {
            StringBuilder stringBuilder = new StringBuilder("in ( ");
            for (Category c : categories) {
                stringBuilder.append(c.getObjectId()).append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
            stringBuilder.append(" )");
            additionalParam = stringBuilder.toString();
        }
        return additionalParam;
    }

}
