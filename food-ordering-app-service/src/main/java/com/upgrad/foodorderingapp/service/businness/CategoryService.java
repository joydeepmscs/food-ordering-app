package com.upgrad.foodorderingapp.service.businness;

import com.upgrad.foodorderingapp.service.common.FoodAppUtil;
import com.upgrad.foodorderingapp.service.dao.CategoryDao;
import com.upgrad.foodorderingapp.service.entity.CategoryEntity;
import com.upgrad.foodorderingapp.service.entity.RestaurantCategoryEntity;
import com.upgrad.foodorderingapp.service.exception.CategoryNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.upgrad.foodorderingapp.service.common.GenericErrorCode.CNF_001;
import static com.upgrad.foodorderingapp.service.common.GenericErrorCode.CNF_002;

@Service
public class CategoryService {
    private final Logger log = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CategoryDao categoryDao;


    /**
     * Get  all categories
     *
     * @return - list of CategoryEntity
     */
    public List<CategoryEntity> getAllCategoriesOrderedByName() {
        List<CategoryEntity> allCategory = categoryDao.getAllCategoriesOrderedByName();
        return allCategory;
    }
    /**
     * Get  category details using category uuid
     *
     * @param categoryUuid - String represents category uuid
     * @return - category details using category uuid
     */
    public CategoryEntity getCategoryById(String categoryUuid) throws CategoryNotFoundException {
        if (FoodAppUtil.isEmptyField(categoryUuid)) {
            throw new CategoryNotFoundException(CNF_001.getCode(), CNF_001.getDefaultMessage());
        }
        CategoryEntity categoryEntity = categoryDao.getCategoryById(categoryUuid);

        if (categoryEntity == null) {
            throw new CategoryNotFoundException(CNF_002.getCode(), CNF_002.getDefaultMessage());
        }
        return categoryEntity;

    }

    /**
     * Get  List of Category Entities using restaurant uuid
     *
     * @param restaurantId - String represents restaurant uuid
     * @return - List of Category Entities using restaurant uuid
     */
    public List<CategoryEntity> getCategoriesByRestaurant(final String restaurantId){
        List<RestaurantCategoryEntity> restaurantCategoryEntities  = categoryDao.getAllCategoriesByRestaurant(restaurantId);
        List<CategoryEntity> categories = new ArrayList<CategoryEntity>();
        for(RestaurantCategoryEntity ct:restaurantCategoryEntities){
            categories.add(ct.getCategoryEntity());
        }
        return  categories;

    }


}
