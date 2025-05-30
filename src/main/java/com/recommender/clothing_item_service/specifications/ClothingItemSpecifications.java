package com.recommender.clothing_item_service.specifications;

import com.recommender.clothing_item_service.model.ClothingItem;
import com.recommender.clothing_item_service.model.ESize;
import com.recommender.clothing_item_service.model.EStyle;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ClothingItemSpecifications {

    public static Specification<ClothingItem> hasName(String name){
        return (root, query, criteriaBuilder) ->
                name == null || name.trim().isEmpty() ? null :
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("name")),
                                "%" + name.toLowerCase() + "%"
                        );
    }

    public static Specification<ClothingItem> hasStyleIn(List<EStyle> styles){
        return (root, query, cb) -> {
            if (styles == null || styles.isEmpty()) return null;

            var join = root.join("style");
            return join.in(styles);
        };

    }

    public static Specification<ClothingItem> hasSize(ESize size){
        return (root, query, criteriaBuilder) ->
                size == null? null :
                criteriaBuilder.equal(root.get("size"), size);
    }

    public static Specification<ClothingItem> hasMaxPrice(Integer price){
        return (root, query, criteriaBuilder) ->
        price == null || price < 0 ? null :
        criteriaBuilder.lessThanOrEqualTo(root.get("price"),price);
    }

    public static Specification<ClothingItem> hasColor(String color){
        return (root, query, criteriaBuilder) ->
                color == null || color.trim().isEmpty() ? null :
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("color")),
                                "%" + color.toLowerCase() + "%"
                        );
    }


}
