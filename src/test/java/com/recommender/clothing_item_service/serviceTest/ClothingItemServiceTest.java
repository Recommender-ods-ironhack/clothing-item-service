package com.recommender.clothing_item_service.serviceTest;

import com.recommender.clothing_item_service.model.ClothingItem;
import com.recommender.clothing_item_service.model.ESize;
import com.recommender.clothing_item_service.model.EStyle;
import com.recommender.clothing_item_service.service.ClothingItemService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class ClothingItemServiceTest{

    @Autowired
    ClothingItemService clothingItemService;

    @Test
    @Transactional
    @DisplayName("Create item works correctly")
    public void saveClothingItemTest(){
        ClothingItem item = new ClothingItem();

        var styles = Set.of(EStyle.FORMAL);

        item.setName("Golden Gloves");
        item.setStyle(styles);
        item.setSize(ESize.S);
        item.setPrice(900.0);
        item.setColor("golden");
        item.setStock(1);

        var savedItem = clothingItemService.saveItem(item);

        assertNotNull(savedItem.getId());
        assertEquals("Golden Gloves", savedItem.getName());
        assertTrue(savedItem.getStyle().contains(EStyle.FORMAL));
        assertEquals(styles, savedItem.getStyle());
        assertEquals(ESize.S, savedItem.getSize());
        assertEquals(900.0, savedItem.getPrice());
        assertEquals("golden", savedItem.getColor());
        assertEquals(1, savedItem.getStock());
    }

}
