package com.recommender.clothing_item_service.service;

import com.recommender.clothing_item_service.model.ClothingItem;
import com.recommender.clothing_item_service.model.EStyle;
import com.recommender.clothing_item_service.repository.ClothingItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ClothingItemService {

    private final ClothingItemRepository clothingItemRepository;

    public ClothingItem getClothingItemById(Long id){
       ClothingItem item = clothingItemRepository.findById(id)
               .orElseThrow(()-> new RuntimeException("Clothing Item doesnt exist with id: "+ id));
        return item;
    }

    public List<ClothingItem> getAllClothingItems(){
        return clothingItemRepository.findAll();
    }

    public List<ClothingItem> getAllClothingItemsByStyle(EStyle style){
        return clothingItemRepository.findByStyle(style);
    }
}
