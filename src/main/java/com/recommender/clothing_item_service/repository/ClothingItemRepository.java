package com.recommender.clothing_item_service.repository;

import com.recommender.clothing_item_service.model.ClothingItem;
import com.recommender.clothing_item_service.model.ESize;
import com.recommender.clothing_item_service.model.EStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClothingItemRepository extends JpaRepository<ClothingItem, Long> {
    //TODO mirar si esto lo hago con specification

    List<ClothingItem> findBySize(ESize size);

    List<ClothingItem> findByStyle(EStyle style);

    List<ClothingItem> findByColor(String color);

    List<ClothingItem> findByPriceLessThan(double limit);

    List<ClothingItem> findBySizeAndStyleIn
            (ESize size, List<EStyle> styles);

    List<ClothingItem> findBySizeAndStyleInAndPriceLessThanEqual
            (ESize size, List<EStyle> styles, Double maxPrice);

    List<ClothingItem> findBySizeAndStyleInAndColorAndPriceLessThanEqual
            (ESize size, List<EStyle> styles,String color, Double maxPrice);

    @Query(value="SELECT * FROM clothing_item ORDER BY stock ASC LIMIT 4")
    List<ClothingItem> findExcessStockItems();

}
