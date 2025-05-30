package com.recommender.clothing_item_service.controller;

import com.recommender.clothing_item_service.DTOs.ClothingItemDTO;
import com.recommender.clothing_item_service.DTOs.ErrorResponseDTO;
import com.recommender.clothing_item_service.exceptions.ItemNotFoundException;
import com.recommender.clothing_item_service.model.ESize;
import com.recommender.clothing_item_service.model.EStyle;
import com.recommender.clothing_item_service.model.ClothingItem;
import com.recommender.clothing_item_service.service.ClothingItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/filtered")
    public ResponseEntity<?> getFilteredClothingItems(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) ESize size,
            @RequestParam(required = false) List<EStyle> styles,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Integer maxPrice
    ) {
        try {
            List<ClothingItem> items = clothingItemService.filterClothingItems(name, size, styles, color, maxPrice);
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

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchUser(@PathVariable Long id, @RequestBody ClothingItemDTO itemDTO){
        try {
            ClothingItem foundUser = clothingItemService.patchItem(id, itemDTO);
            return ResponseEntity.ok(foundUser);
        } catch (ItemNotFoundException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
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