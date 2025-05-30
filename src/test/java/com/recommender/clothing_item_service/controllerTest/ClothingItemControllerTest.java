package com.recommender.clothing_item_service.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recommender.clothing_item_service.model.ClothingItem;
import com.recommender.clothing_item_service.model.ESize;
import com.recommender.clothing_item_service.model.EStyle;
import com.recommender.clothing_item_service.repository.ClothingItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ClothingItemControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ClothingItemRepository clothingItemRepository;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    List<ClothingItem> items;


    @BeforeEach
    public void SetUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        ClothingItem item1 = new ClothingItem();
        item1.setName("Silver Hat");
        item1.setStyle(Set.of(EStyle.CASUAL));
        item1.setSize(ESize.M);
        item1.setPrice(1000.0);
        item1.setColor("silver");
        item1.setStock(2);

        ClothingItem item2 = new ClothingItem();
        item2.setName("Golden Gloves");
        item2.setStyle(Set.of(EStyle.FORMAL));
        item2.setSize(ESize.S);
        item2.setPrice(900.0);
        item2.setColor("golden");
        item2.setStock(1);

        items = clothingItemRepository.saveAll(List.of(item1, item2));
    }

    @AfterEach
    void tearDown() {
        clothingItemRepository.deleteAll(items);
    }

    @Test
    @DisplayName("getItemById works correctly")
    public void getItemByIdTest() throws Exception{
        MvcResult mvcResult = mockMvc.perform(get("/api/clothing-item/"+ items.getFirst().getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Silver"));
        assertNotNull(mvcResult.getResponse().getContentAsString());

        MvcResult mvcResultNull = mockMvc.perform(get("/api/clothing-item/0"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertNotNull(mvcResultNull.getResponse().getContentAsString());
    }


    @Test
    void searchClothingItemsByNameTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/clothing-item/search"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("golden"));

        ClothingItem[] clothingItems = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), ClothingItem[].class);

        for(ClothingItem item :  clothingItems){
            assertTrue(item.getId() != null, "ID cannot be null");
            assertTrue(item.getName() != null, "Name cannot be null");
            assertTrue(!item.getName().isEmpty(), "Name cannot be empty");
            assertTrue(item.getPrice() > 0, "Price must be positive");
            assertTrue(item.getStock() >= 0, "Stock must be zero or more");
        }

        MvcResult mvcResultByName = mockMvc.perform(get("/api/clothing-item/search?name=Silver"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResultByName.getResponse().getContentAsString().contains("silver"));

        ClothingItem[] clothingItemsByName = objectMapper
                .readValue(mvcResultByName.getResponse().getContentAsString(), ClothingItem[].class);

        for(ClothingItem item :  clothingItemsByName){
            assertEquals("Silver Hat",item.getName());
            assertEquals( 500.0,item.getPrice());

        }
    }

    @Test
    void searchAllClothingItemsTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/clothing-item/filtered"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("golden"));

        ClothingItem[] clothingItems = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ClothingItem[].class);

        for(ClothingItem item :  clothingItems){
            assertTrue(item.getId() != null, "ID cannot be null");
            assertTrue(item.getName() != null, "Name cannot be null");
            assertTrue(!item.getName().isEmpty(), "Name cannot be empty");
            assertTrue(item.getPrice() > 0, "Price must be positive");
            assertTrue(item.getStock() >= 0, "Stock must be zero or more");
        }
    }

    @Test
    @DisplayName("getFilteredClothingItems works correctly")
    void getFilteredClothingItemsTest() throws Exception {

        MvcResult mvcResultSize = mockMvc.perform(get("/api/clothing-item/filtered?size=M"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonResponseSize = mvcResultSize.getResponse().getContentAsString();
        ClothingItem[] itemsArraySize = objectMapper.readValue(jsonResponseSize, ClothingItem[].class);
        var itemSize = Arrays.stream(itemsArraySize).findFirst();

        assertSame(ESize.M, itemSize.get().getSize());

        MvcResult mvcResultCheaperThan = mockMvc.perform(get("/api/clothing-item/filtered?maxPrice=999"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonResponseCheaperThan = mvcResultSize.getResponse().getContentAsString();
        ClothingItem[] itemsArrayCheaperThan = objectMapper.readValue(jsonResponseCheaperThan, ClothingItem[].class);
        var itemCheaperThan = Arrays.stream(itemsArrayCheaperThan).findFirst();


        assertTrue(itemCheaperThan.get().getPrice()<999);

    }
    @Test
    @DisplayName("saveItem works correctly")
    void saveItemTest() throws Exception {
        ClothingItem newItem = new ClothingItem();
        newItem.setName("Item de prueba");
        newItem.setColor("Blue");
        newItem.setStyle(Set.of(EStyle.PUNK));
        newItem.setSize(ESize.XXL);
        newItem.setPrice(70.0);
        newItem.setStock(10);

        String itemJson = objectMapper.writeValueAsString(newItem);

        MvcResult mvcResult = mockMvc.perform(post("/api/clothing-item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemJson))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

        assertTrue(mvcResult.getResponse().getContentAsString().contains("prueba"));
    }





}
