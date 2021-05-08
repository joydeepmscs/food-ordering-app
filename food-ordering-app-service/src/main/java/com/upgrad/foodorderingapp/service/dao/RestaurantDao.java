package com.upgrad.foodorderingapp.service.dao;

import com.upgrad.foodorderingapp.service.entity.RestaurantCategoryEntity;
import com.upgrad.foodorderingapp.service.entity.RestaurantEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantDao {
    private final Logger log = LoggerFactory.getLogger(CustomerDao.class);
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Method to get all restaurants in order of their ratings
     *
     * @return - list of Restaurant Entities in order of their ratings
     */
    public List<RestaurantEntity> getAllRestaurants(){
            return entityManager.createNamedQuery("getAllRestaurants", RestaurantEntity.class).getResultList();
    }

    /**
     * Method to get all restaurants using restaurant name
     *
     * @param restaurantName - String representing restaurant name
     * @return - list of Restaurant Entities using restaurant name
     */
    public List<RestaurantEntity> getRestaurantByName(final String restaurantName){
          return  entityManager.createNamedQuery("getRestaurantByName",RestaurantEntity.class).setParameter("rName",restaurantName)
                    .getResultList();
    }

    /**
     * Method to retrieve RestaurantEntity for the given restaurant UUID
     *
     * @param restaurantId - String representing restaurant UUID
     * @return RestaurantEntity if data exists in the database, else return null
     */
    public RestaurantEntity getRestaurantById(final String restaurantId){
        try{
            RestaurantEntity restaurantEntity=entityManager.createNamedQuery("getRestaurantById",RestaurantEntity.class).setParameter("uuid",restaurantId).getSingleResult();
            return restaurantEntity;
        }catch (NoResultException nre){
            return null;
        }
    }

    /**
     * Method to get all restaurants category entity using category uuid
     *
     * @param categoryUuid - String representing Category UUID
     * @return - list of get all restaurants category entity using category uuid
     */
    public List<RestaurantCategoryEntity> restaurantsByCategoryId(final String categoryUuid) {
        return entityManager.createNamedQuery("getRestaurantsByCategory", RestaurantCategoryEntity.class)
                .setParameter("categoryUuid", categoryUuid)
                .getResultList();
    }

    /**
     * Method to get update customer rating for  restaurants
     *
     * @param restaurantEntity - restaurant entity to updated
     */
    public void updateCustomerRating(RestaurantEntity restaurantEntity){
        entityManager.merge(restaurantEntity);
    }


}
