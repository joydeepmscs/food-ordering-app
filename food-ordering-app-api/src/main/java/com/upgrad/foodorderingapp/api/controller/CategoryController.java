package com.upgrad.foodorderingapp.api.controller;

import com.upgrad.foodorderingapp.api.model.CategoriesListResponse;
import com.upgrad.foodorderingapp.api.model.CategoryDetailsResponse;
import com.upgrad.foodorderingapp.api.model.CategoryListResponse;
import com.upgrad.foodorderingapp.api.model.ItemList;
import com.upgrad.foodorderingapp.service.businness.CategoryService;
import com.upgrad.foodorderingapp.service.entity.CategoryEntity;
import com.upgrad.foodorderingapp.service.entity.ItemEntity;
import com.upgrad.foodorderingapp.service.exception.CategoryNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("")
public class CategoryController {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryService categoryService;

    /**
     * RestController method called when the request pattern is of type "/category"
     * and the incoming request is of 'GET' type
     * Retrieve category list order by name
     *
     * @return - ResponseEntity(CategoriesListResponse, HttpStatus.OK)
     */
    @RequestMapping(method = RequestMethod.GET,path = "/category",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoriesListResponse> getAllCategoriesOrderedByName() throws CategoryNotFoundException {
        List<CategoryEntity> allCategories = categoryService.getAllCategoriesOrderedByName();
        CategoriesListResponse categoriesListResponse=new CategoriesListResponse();
        List<CategoryListResponse> categoryLists = new ArrayList<>();
        if (!allCategories.isEmpty()) {
            allCategories.forEach(
                    category -> categoryLists.add(setCategoryList(category))
            );
        }
        if(categoryLists.size()<=0)
            categoriesListResponse.setCategories(null);
        else
            categoriesListResponse.setCategories(categoryLists);



        return new ResponseEntity<CategoriesListResponse>(categoriesListResponse, HttpStatus.OK);
    }

    /**
     * RestController method called when the request pattern is of type "/order/coupon/{coupon_name}"
     * and the incoming request is of 'GET' type
     * Retrieve coupon details using coupon name
     *
     * @param category_id - This represents coupon name
     * @return - ResponseEntity(CouponDetailsResponse, HttpStatus.OK)
     * @throws CategoryNotFoundException - if incorrect/ invalid category id is sent,
     *                                   *                                      or the category id doesn't exist
     */
    @RequestMapping(method = RequestMethod.GET, path = "/category/{category_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoryDetailsResponse> getCategoryById(@PathVariable String category_id) throws CategoryNotFoundException {
        CategoryEntity categoryEntity = categoryService.getCategoryById(category_id);
        List<ItemEntity> itemEntities = categoryEntity.getItems();
        CategoryDetailsResponse categoryDetailsResponse = new CategoryDetailsResponse();
        categoryDetailsResponse.setId(UUID.fromString(categoryEntity.getUuid()));
        categoryDetailsResponse.setCategoryName(categoryEntity.getCategoryName());
        List<ItemList> itemLists = new ArrayList<>();
        if (!itemEntities.isEmpty()) {
            itemEntities.forEach(itemEntity -> {
                itemLists.add(setItemList(itemEntity));
            });
            categoryDetailsResponse.setItemList(itemLists);
        }
        return new ResponseEntity<CategoryDetailsResponse>(categoryDetailsResponse, HttpStatus.OK);
    }



    /**
     * Method to set and return ItemList
     *
     * @param itemEntity - itemEntity object
     * @return - ItemList
     */
    private ItemList setItemList(ItemEntity itemEntity) {
        ItemList itemList = modelMapper.map(itemEntity,ItemList.class);
        itemList.setId(UUID.fromString(itemEntity.getUuid()));
        return itemList;
    }

    /**
     * Method to set all  field into CategoryListResponse
     *
     * @param category - CategoryEntity object
     * @return - CategoryListResponse
     */
    private CategoryListResponse setCategoryList(Object category) {
        CategoryEntity categoryEntity = (CategoryEntity) category;
        CategoryListResponse categoryListResponse = new CategoryListResponse();
        categoryListResponse.setCategoryName(categoryEntity.getCategoryName());
        categoryListResponse.setId(UUID.fromString(categoryEntity.getUuid()));
        return categoryListResponse;
    }

}
