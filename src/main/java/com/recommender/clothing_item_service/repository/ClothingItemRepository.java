package com.recommender.clothing_item_service.repository;

import org.springframework.data.domain.Pageable;
import com.recommender.clothing_item_service.model.ClothingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClothingItemRepository extends JpaRepository<ClothingItem, Long>, JpaSpecificationExecutor<ClothingItem> {

    @Query("SELECT c FROM ClothingItem c ORDER BY c.stock DESC")
    List<ClothingItem> findTop4ByStock(Pageable pageable);

}
