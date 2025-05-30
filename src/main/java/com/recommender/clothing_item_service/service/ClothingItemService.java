package com.recommender.clothing_item_service.service;

import com.recommender.clothing_item_service.DTOs.ClothingItemDTO;
import com.recommender.clothing_item_service.exceptions.ItemNotFoundException;
import com.recommender.clothing_item_service.model.ClothingItem;
import com.recommender.clothing_item_service.model.ESize;
import com.recommender.clothing_item_service.model.EStyle;
import com.recommender.clothing_item_service.repository.ClothingItemRepository;
import com.recommender.clothing_item_service.specifications.ClothingItemSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ClothingItemService {

    private final ClothingItemRepository clothingItemRepository;

    public ClothingItem getItemById(Long id){
       ClothingItem item = clothingItemRepository.findById(id)
               .orElseThrow(()-> new ItemNotFoundException(id));
        return item;
    }

    public List<ClothingItem> getAllClothingItems(){
        return clothingItemRepository.findAll();
    }

    public List<ClothingItem> getExcessStockItems() {
        return clothingItemRepository.findTop4ByStock(PageRequest.of(0, 4));

    }

    public List<ClothingItem> filterClothingItems(String name, ESize size, List<EStyle> styles, String color, Integer maxPrice) {
        Specification<ClothingItem> spec = Specification
                .where(ClothingItemSpecifications.hasName(name))
                .and(ClothingItemSpecifications.hasSize(size))
                .and(ClothingItemSpecifications.hasStyleIn(styles))
                .and(ClothingItemSpecifications.hasColor(color))
                .and(ClothingItemSpecifications.hasMaxPrice(maxPrice));

        return clothingItemRepository.findAll(spec);
    }

    public ClothingItem saveItem(ClothingItem item){
        return clothingItemRepository.save(item);
    }

    public ClothingItem patchItem(Long id, ClothingItemDTO itemDTO){

        ClothingItem existingItem = getItemById(id);

            if(itemDTO.getName()!= null){
                existingItem.setName(itemDTO.getName());
            }

            if (itemDTO.getStyle()!= null){
                existingItem.setStyle(itemDTO.getStyle());
            }

            if (itemDTO.getSize()!= null){
                existingItem.setSize(itemDTO.getSize());
            }

            if(itemDTO.getPrice()!= null){
                existingItem.setPrice(itemDTO.getPrice());
            }

            if(itemDTO.getColor()!= null){
            existingItem.setColor(itemDTO.getColor());
            }

            if(itemDTO.getStock()!= null){
            existingItem.setStock(itemDTO.getStock());
            }

            return clothingItemRepository.save(existingItem);

    }

    public ClothingItem deleteItemById(Long id){
        var user= getItemById(id);
        clothingItemRepository.deleteById(id);
        return user;
    }

}
