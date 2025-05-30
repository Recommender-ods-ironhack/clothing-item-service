package com.recommender.clothing_item_service.DTOs;

import com.recommender.clothing_item_service.model.ESize;
import com.recommender.clothing_item_service.model.EStyle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClothingItemDTO {

    private String name;

    private Set<EStyle> style;

    private ESize size;

    private Double price;

    private String color;

    private Integer stock;

}
