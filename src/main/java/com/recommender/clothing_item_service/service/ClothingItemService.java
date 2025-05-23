package com.recommender.clothing_item_service.service;

import com.recommender.clothing_item_service.model.ClothingItem;
import com.recommender.clothing_item_service.model.ESize;
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

    public List<ClothingItem> getItemsBySize(ESize size){
        return clothingItemRepository.findBySize(size);
    }

    public List<ClothingItem> getItemsByStyle(EStyle style){
        return clothingItemRepository.findByStyle(style);
    }

    public List<ClothingItem> getItemsByColor(String color){
        return clothingItemRepository.findByColor(color);
    }

    public List<ClothingItem> getItemsCheaperThan(double limit){
        return clothingItemRepository.findByPriceLessThan(limit);
    }

    public List<ClothingItem> getItemsBySizeAndStyles(ESize size, List<EStyle> styles) {
        return clothingItemRepository.findBySizeAndStyleIn(size, styles);
    }

    public List<ClothingItem> getItemsBySizeStylesAndMaxPrice(ESize size, List<EStyle> styles, Double maxPrice) {
        return clothingItemRepository.findBySizeAndStyleInAndPriceLessThanEqual(size, styles, maxPrice);
    }

    public List<ClothingItem> getItemsBySizeStylesColorAndMaxPrice(ESize size, List<EStyle> styles, String color, Double maxPrice) {
        return clothingItemRepository.findBySizeAndStyleInAndColorAndPriceLessThanEqual(size, styles, color, maxPrice);
    }

    public List<ClothingItem> getExcessStockItems() {
        return clothingItemRepository.findExcessStockItems();
    }


}
