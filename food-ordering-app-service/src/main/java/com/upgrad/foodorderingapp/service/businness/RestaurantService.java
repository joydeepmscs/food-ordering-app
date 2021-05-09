package com.upgrad.foodorderingapp.service.businness;

import com.upgrad.foodorderingapp.service.common.FoodAppUtil;
import com.upgrad.foodorderingapp.service.dao.CategoryDao;
import com.upgrad.foodorderingapp.service.dao.RestaurantDao;
import com.upgrad.foodorderingapp.service.entity.CategoryEntity;
import com.upgrad.foodorderingapp.service.entity.RestaurantCategoryEntity;
import com.upgrad.foodorderingapp.service.entity.RestaurantEntity;
import com.upgrad.foodorderingapp.service.exception.CategoryNotFoundException;
import com.upgrad.foodorderingapp.service.exception.InvalidRatingException;
import com.upgrad.foodorderingapp.service.exception.RestaurantNotFoundException;
import org.decimal4j.util.DoubleRounder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.upgrad.foodorderingapp.service.common.GenericErrorCode.*;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private CustomerService customerService;

    private final Logger log = LoggerFactory.getLogger(CustomerService.class);

    /**
     * Method to get all restaurants in order of their ratings
     *
     * @return - list of Restaurant Entities in order of their ratings
     */
    public List<RestaurantEntity>  restaurantsByRating(){
        return restaurantDao.getAllRestaurants();
    }


    /**
     * Method to get all restaurants using restaurant name
     *
     * @param restaurantName - String representing restaurant name
     * @return - list of Restaurant Entities using restaurant name
     * @throws RestaurantNotFoundException - if the restaurant name is empty
     */
    public List<RestaurantEntity> restaurantsByName(final String restaurantName) throws RestaurantNotFoundException{
        if(FoodAppUtil.isEmptyField(restaurantName)){
            throw new RestaurantNotFoundException(RNF_003.getCode(), RNF_003.getDefaultMessage());
        }
        List<RestaurantEntity> restaurantEntity = restaurantDao.getRestaurantByName(restaurantName);
        return restaurantEntity;
    }

    /**
     * Method to retrieve RestaurantEntity for the given restaurant UUID
     * @param restaurantId - String represents restaurant UUID
     * @return - RestaurantEntity object
     * @throws RestaurantNotFoundException - if no restaurant in found in the database for the given restaurant uuid
     */
    public RestaurantEntity restaurantByUUID(final String restaurantId) throws RestaurantNotFoundException{
        if(FoodAppUtil.isEmptyField(restaurantId)) {
            throw new RestaurantNotFoundException(RNF_003.getCode(), RNF_003.getDefaultMessage());
        }
        RestaurantEntity restaurantEntity=restaurantDao.getRestaurantById(restaurantId);
        if(restaurantEntity==null)
            throw new RestaurantNotFoundException(RNF_001.getCode(), RNF_001.getDefaultMessage());
        return restaurantEntity;
    }

    /**
     * Method to update the restaurant rating given the restaurant Id
     * @param restaurantEntity - Restarurant Entity
     * @param customerRating - customer rating
     * @return - RestaurantEntity object
     * @throws InvalidRatingException - if the given customer rating is not valid
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity updateRestaurantRating(RestaurantEntity restaurantEntity,final Double customerRating) throws InvalidRatingException {
        if(customerRating<1 || customerRating>5){
            throw new InvalidRatingException(IRE_001.getCode(), IRE_001.getDefaultMessage());
        }
        Double totalSum = restaurantEntity.getCustomerRating()*restaurantEntity.getNumberCustomersRated();
        restaurantEntity.setNumberCustomersRated(restaurantEntity.getNumberCustomersRated()+1);
        restaurantEntity.setCustomerRating(DoubleRounder.round((totalSum+customerRating)/restaurantEntity.getNumberCustomersRated(),2));
        restaurantDao.updateCustomerRating(restaurantEntity);
        return restaurantEntity;
    }

    /**
     * Method to get all restaurants using category uuid
     *
     * @param categoryUuid - String represents category UUID
     * @return - list of Restaurant Entities using  category uuid
     * @throws CategoryNotFoundException - if category uuid is empty, or no category exists for the give category uuid
     */
    public List<RestaurantEntity> restaurantByCategory(final String categoryUuid) throws CategoryNotFoundException {
        // Throw exception if the category uuid is empty
        if (FoodAppUtil.isEmptyField(categoryUuid)) {
            throw new CategoryNotFoundException(CNF_001.getCode(), CNF_001.getDefaultMessage());
        }

        CategoryEntity categoryEntity = categoryDao.getCategoryById(categoryUuid);
        // Throw exception if no category exists for the give category uuid
        if (categoryEntity == null) {
            throw new CategoryNotFoundException(CNF_002.getCode(), CNF_002.getDefaultMessage());
        }

        List<RestaurantEntity> restaurantEntities = new ArrayList<>();

        List<RestaurantCategoryEntity> restaurantCategoryEntities = restaurantDao
                .restaurantsByCategoryId(categoryEntity.getUuid());
        restaurantCategoryEntities.forEach(restaurantCategoryEntity -> {
            restaurantEntities.add(restaurantCategoryEntity.getRestaurantEntity());
        });
        return restaurantEntities;
    }

}
