package com.recommender.clothing_item_service.serviceTest;

import com.recommender.clothing_item_service.exceptions.ItemNotFoundException;
import com.recommender.clothing_item_service.model.ClothingItem;
import com.recommender.clothing_item_service.model.ESize;
import com.recommender.clothing_item_service.model.EStyle;
import com.recommender.clothing_item_service.service.ClothingItemService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class ClothingItemServiceTest{

    @Autowired
    ClothingItemService clothingItemService;

    private ClothingItem testItem;

    @BeforeEach
    void setUp(){
        ClothingItem item = new ClothingItem();
        item.setName("Silver Hat");
        item.setStyle(Set.of(EStyle.CASUAL));
        item.setSize(ESize.M);
        item.setPrice(500.0);
        item.setColor("silver");
        item.setStock(2);

        testItem = clothingItemService.saveItem(item);
    }

    @AfterEach
    void tearDown() {
        clothingItemService.deleteItemById(testItem.getId());
    }

    @Test
    @Transactional
    @DisplayName("Save item works correctly")
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

    @Test
    @DisplayName("getItemsByNameContains works correctly")
    public void getItemsByNameContainsTest(){
        var items = clothingItemService.getItemsByNameContains("Silver");

        assertNotNull(items);
    }

    @Test
    @DisplayName("getItemById works correctly")
    public void getItemByIdTest(){
        var item = clothingItemService.getItemById(testItem.getId());

        assertNotNull(item);
        assertEquals(500.0, item.getPrice());
        assertThrows(ItemNotFoundException.class, () -> {
            clothingItemService.getItemById(-1L);
        });
    }

    @Test
    @DisplayName("getAllClothingItems works correctly")
    public void getAllClothingItemsTest(){
        var items = clothingItemService.getAllClothingItems();
        assertNotNull(items);
        assertFalse(items.isEmpty());

    }

    @Test
    @DisplayName("getItemsBySize works correctly")
    public void getItemsBySizeTest(){
        var items = clothingItemService.getItemsBySize(ESize.M);

        assertNotNull(items);
        assertFalse(items.isEmpty());
        assertTrue(items.stream().allMatch(item -> item.getSize() == ESize.M));
    }

    @Test
    @Transactional
    @DisplayName("getItemsByStyle works correctly")
    public void getItemsByStyleTest(){
        var items = clothingItemService.getItemsByStyle(List.of(EStyle.CASUAL));

        assertNotNull(items);
        assertFalse(items.isEmpty());
        assertTrue(items.stream().allMatch(item -> item.getStyle().contains(EStyle.CASUAL)));
    }

    @Test
    @DisplayName("getItemsByColor works correctly")
    public void getItemsByColorTest(){
        var items = clothingItemService.getItemsByColor("silver");

        assertNotNull(items);
        assertFalse(items.isEmpty());
        assertTrue(items.stream().allMatch(item -> item.getColor().equals("silver")));
    }

    @Test
    @DisplayName("getItemsCheaperThan works correctly")
    public void getItemsCheaperThanTest(){
        var items = clothingItemService.getItemsCheaperThan(600.0);

        assertNotNull(items);
        assertFalse(items.isEmpty());
        assertTrue(items.stream().allMatch(item -> item.getPrice() < 600.0));
    }

    @Test
    @Transactional
    @DisplayName("getItemsBySizeAndStyles works correctly")
    public void getItemsBySizeAndStylesTest() {
        var items = clothingItemService.getItemsBySizeAndStyles(ESize.M, List.of(EStyle.CASUAL));

        assertNotNull(items);
        assertFalse(items.isEmpty());
        assertTrue(items.stream().allMatch(item -> item.getSize() == ESize.M && item.getStyle().contains(EStyle.CASUAL)));
    }

    @Test
    @Transactional
    @DisplayName("getItemsBySizeStylesAndMaxPrice works correctly")
    public void getItemsBySizeStylesAndMaxPriceTest() {
        var items = clothingItemService.getItemsBySizeStylesAndMaxPrice(ESize.M, List.of(EStyle.CASUAL), 600.0);

        assertNotNull(items);
        assertFalse(items.isEmpty());
        assertTrue(items.stream().allMatch(item -> item.getSize() == ESize.M && item.getStyle().contains(EStyle.CASUAL) && item.getPrice() <= 600.0));
    }

    @Test
    @Transactional
    @DisplayName("getItemsBySizeStylesColorAndMaxPrice works correctly")
    public void getItemsBySizeStylesColorAndMaxPriceTest() {
        var items = clothingItemService.getItemsBySizeStylesColorAndMaxPrice(ESize.M, List.of(EStyle.CASUAL), "silver", 600.0);

        assertNotNull(items);
        assertFalse(items.isEmpty());
        assertTrue(items.stream().allMatch(item -> item.getSize() == ESize.M && item.getStyle().contains(EStyle.CASUAL) && item.getColor().equals("silver") && item.getPrice() <= 600.0));
    }

    @Test
    @DisplayName("getExcessStockItems works correctly")
    public void getExcessStockItemsTest() {
        var items = clothingItemService.getExcessStockItems();

        assertNotNull(items);
        assertFalse(items.isEmpty());
        items.forEach(item -> assertTrue(item.getStock() > 1));
    }

}
