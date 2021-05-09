package com.upgrad.foodorderingapp.service.businness;

import com.upgrad.foodorderingapp.service.dao.CategoryDao;
import com.upgrad.foodorderingapp.service.dao.ItemDao;
import com.upgrad.foodorderingapp.service.entity.*;
import com.upgrad.foodorderingapp.service.exception.ItemNotFoundException;
import com.upgrad.foodorderingapp.service.exception.RestaurantNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.upgrad.foodorderingapp.service.common.GenericErrorCode.INF_003;
import static com.upgrad.foodorderingapp.service.common.GenericErrorCode.RNF_001;

@Service
public class ItemService {
    private final Logger log = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private CategoryDao categoryDao;

    /**
     * Method tp retrieve item details using restaurant Id
     *
     * @param  restaurantEntity - restaurant entity object
     * @return ItemEntity- item entity top 5
     * @throws RestaurantNotFoundException - if no ItemEntity is found in the databse for the given item uuid
     */
    public List<ItemEntity> getItemsByPopularity(final RestaurantEntity restaurantEntity) throws RestaurantNotFoundException{
        List<RestaurantItemEntity> restaurantItemEntities = itemDao.getItemForRestaurantUUID(restaurantEntity.getUuid());
        if(restaurantItemEntities.size()<1){
            throw new RestaurantNotFoundException(RNF_001.getCode(), RNF_001.getDefaultMessage());
        }
        restaurantItemEntities=getTop5(restaurantItemEntities,restaurantEntity);
        List<ItemEntity> itemEntityList = new ArrayList<ItemEntity>();
        for(RestaurantItemEntity rt:restaurantItemEntities){
            itemEntityList.add(rt.getItemEntity());
        }

        return itemEntityList;
    }

    /**
     * Method to get top5 based on sorting
     *
     * @param  restaurantItemEntities - restaurant entity object
     * @return RestaurantItemEntity- item entity top 5
     */
    private   List<RestaurantItemEntity> getTop5(List<RestaurantItemEntity> restaurantItemEntities,final RestaurantEntity restaurantEntity){
        HashMap<String,Integer> hmap= new HashMap<String,Integer>();
        List<OrderItemEntity> orderItemEntities = itemDao.getItemByRestaurantUUID(restaurantEntity.getUuid());
        for(OrderItemEntity ot:orderItemEntities){
            if(hmap.containsKey(ot.getItemId().getUuid())){
                hmap.replace(ot.getItemId().getUuid(),hmap.get(ot.getItemId().getUuid())+1);
            }else{
                hmap.put(ot.getItemId().getUuid(),1);
            }
        }
        hmap=hmap.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        List<RestaurantItemEntity> finalList = new ArrayList<RestaurantItemEntity>();
        int counter=1;
        for(Map.Entry<String,Integer> entry:hmap.entrySet()){
            if(counter>=5){
                break;
            }
            for(RestaurantItemEntity rt:restaurantItemEntities){
                if(rt.getItemEntity().getUuid().equals(entry.getKey())){
                    finalList.add(rt);
                    counter++;
                }
            }

        }
        for (RestaurantItemEntity rest:restaurantItemEntities){
            if(!finalList.contains(rest) && counter<=5){
                finalList.add(rest);
                counter++;
            }

        }

        return finalList;
    }

    /**
     * Method to get items by category and restaruarnt
     *
     * @param  restaurantID - restaurant ID
     * @param  categoryId - category id
     * @return List<ItemEntity>
     */
    public List<ItemEntity> getItemsByCategoryAndRestaurant(final String restaurantID,final String categoryId){
        List<RestaurantItemEntity> restaurantItemEntities = itemDao.getItemForRestaurantUUID(restaurantID);
        List<CategoryItemEntity> categoryItemEntities = categoryDao.getAllCategoryItems(categoryId);
        List<ItemEntity> itemEntityList = new ArrayList<ItemEntity>();
        for(CategoryItemEntity ct:categoryItemEntities){
            boolean found=false;
            for(RestaurantItemEntity rt:restaurantItemEntities){
                if(rt.getItemEntity().getUuid().equals(ct.getItemEntity().getUuid()))
                    found=true;
            }
            if(found)
                itemEntityList.add(ct.getItemEntity());
        }
        return itemEntityList;
    }

    public ItemEntity getItemByUUID(String itemUuid) throws ItemNotFoundException {
        ItemEntity itemEntity = itemDao.getItemByUUID(itemUuid);
        if(itemEntity == null){
            throw new ItemNotFoundException(INF_003.getCode(), INF_003.getDefaultMessage());
        }
        return itemEntity;
    }

}
