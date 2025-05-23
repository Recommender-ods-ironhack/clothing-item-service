package com.recommender.clothing_item_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothingItemRepository extends JpaRepository<ClothingItemRepository, Long> {

}
