package com.recommender.clothing_item_service.exceptions;

public class ItemNotFoundException extends RuntimeException{

     public ItemNotFoundException(Long id) {
            super("Clothing item not found with ID: " + id);
     }
}
