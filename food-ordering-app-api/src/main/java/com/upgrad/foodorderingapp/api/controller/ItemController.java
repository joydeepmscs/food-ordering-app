package com.upgrad.foodorderingapp.api.controller;

import com.upgrad.foodorderingapp.api.model.ItemList;
import com.upgrad.foodorderingapp.api.model.ItemListResponse;
import com.upgrad.foodorderingapp.service.businness.ItemService;
import com.upgrad.foodorderingapp.service.businness.RestaurantService;
import com.upgrad.foodorderingapp.service.entity.ItemEntity;
import com.upgrad.foodorderingapp.service.entity.RestaurantEntity;
import com.upgrad.foodorderingapp.service.exception.RestaurantNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
public class ItemController {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ItemService itemService;

    @Autowired
    private RestaurantService restaurantService;

    /**
     * RestController method called when the request pattern is of type "/item/restaurant//{restaurant_id}"
     * and the incoming request is of 'GET' type
     * Retrieve Restaurant items
     *
     * @return - ResponseEntity(ItemListResponse, HttpStatus.OK)
     */
    @RequestMapping(method = RequestMethod.GET,path = "/item/restaurant/{restaurant_id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ItemListResponse> getAllRestaurants(@PathVariable final String restaurant_id) throws RestaurantNotFoundException {
        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(restaurant_id);
        List<ItemEntity> itemEntityList = itemService.getItemsByPopularity(restaurantEntity);
        ItemListResponse itemListResponse = getItemListFromRestaurantItemEntity(itemEntityList);
        return new ResponseEntity<ItemListResponse>(itemListResponse, HttpStatus.OK);
    }

    /**
     * Method to set and return ItemListResponse
     *
     * @param itemEntities- List of itemEntity object
     * @return - ItemListResponse
     */
    public ItemListResponse getItemListFromRestaurantItemEntity(List<ItemEntity> itemEntities){
        ItemListResponse itemListResponse = new ItemListResponse();
        for(ItemEntity it:itemEntities){

            ItemList item=new ItemList().id(UUID.fromString(it.getUuid()))
                    .itemName(it.getItemName()).price(it.getPrice())
                    .itemType(it.getType().ordinal()==0? ItemList.ItemTypeEnum.VEG: ItemList.ItemTypeEnum.NON_VEG);
            itemListResponse.add(item);
        }
        return itemListResponse;
    }
}
