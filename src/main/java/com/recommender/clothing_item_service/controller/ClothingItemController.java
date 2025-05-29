package com.recommender.clothing_item_service.controller;

import com.recommender.clothing_item_service.DTOs.ErrorResponseDTO;
import com.recommender.clothing_item_service.exceptions.ItemNotFoundException;
import com.recommender.clothing_item_service.model.ESize;
import com.recommender.clothing_item_service.model.EStyle;
import com.recommender.clothing_item_service.model.ClothingItem;
import com.recommender.clothing_item_service.service.ClothingItemService;
import jakarta.validation.Valid;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clothing-item")
public class ClothingItemController {

    private final ClothingItemService clothingItemService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getItemById(@PathVariable Long id) {
        try {
            ClothingItem item = clothingItemService.getItemById(id);
            return ResponseEntity.ok(item);
        } catch (ItemNotFoundException e) {
            ErrorResponseDTO error = new ErrorResponseDTO();
            error.setStatus(HttpStatus.NOT_FOUND.value());
            error.setError("Not found");
            error.setMessage(e.getMessage());
            return ResponseEntity.status(404).body(error);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchClothingItemsByName(@RequestParam(required = false)  String name) {


        try {
            List<ClothingItem> items;
            if (name == null){
                items = clothingItemService.getAllClothingItems();
            } else{
                items = clothingItemService.getItemsByNameContains(name);
            }
            return ResponseEntity.ok(items);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error searching items by name: " + e.getMessage());
        }
    }

    @GetMapping("/filtered")
    public ResponseEntity<?> getFilteredClothingItems(
            @RequestParam(required = false) ESize size,
            @RequestParam(required = false) List<EStyle> styles,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Double maxPrice
    ) {
        try {
            List<ClothingItem> items;

            if (size != null && styles == null && color == null && maxPrice == null) {
                items = clothingItemService.getItemsBySize(size);
            } else if (size == null && styles != null && color == null && maxPrice == null) {
                items = clothingItemService.getItemsByStyle(styles);
            } else if (size == null && styles == null && color != null && maxPrice == null) {
                items = clothingItemService.getItemsByColor(color);
            } else if (size == null && styles == null && color == null && maxPrice != null) {
                items = clothingItemService.getItemsCheaperThan(maxPrice);
            } else if (size != null && styles != null && color == null && maxPrice == null) {
                items = clothingItemService.getItemsBySizeAndStyles(size, styles);
            } else if (size != null && styles != null && maxPrice != null && color == null) {
                items = clothingItemService.getItemsBySizeStylesAndMaxPrice(size, styles, maxPrice);
            } else if (size != null && styles != null && color != null && maxPrice != null) {
                items = clothingItemService.getItemsBySizeStylesColorAndMaxPrice(size, styles, color, maxPrice);
            } else if (size == null && styles == null && color == null && maxPrice == null) {
                items = clothingItemService.getAllClothingItems();
            } else {
                return ResponseEntity.badRequest().body("Filter combination not supported.");
            }

            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error filtering items: " + e.getMessage());
        }
    }


    @GetMapping("/excess-stock")
    public ResponseEntity<?> getExcessStockItems() {
        try {
            return ResponseEntity.ok(clothingItemService.getExcessStockItems());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error obtaining excess stock items.");
        }
    }

    @PostMapping()
    public ResponseEntity<?> saveItem(@Valid @RequestBody ClothingItem item) {
        try {
            var savedItem = clothingItemService.saveItem(item);
            return ResponseEntity.ok(savedItem);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error saving item.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id){
        try{
            ClothingItem deletedClothingItem = clothingItemService.deleteItemById(id);
            String message = "Deleted clothing item: " + deletedClothingItem.getName();
            return  ResponseEntity.ok(message);
        } catch (ItemNotFoundException e){
            ErrorResponseDTO error = new ErrorResponseDTO();
            error.setStatus(HttpStatus.NOT_FOUND.value());
            error.setError("Not found");
            error.setMessage(e.getMessage());
            return ResponseEntity.status(404).body(error);
        }
    }
}