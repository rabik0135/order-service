package com.rabinchuk.orderservice.controller;

import com.rabinchuk.orderservice.AbstractIntegrationTest;
import com.rabinchuk.orderservice.model.Item;
import com.rabinchuk.orderservice.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = "/cleanup_items.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ItemControllerTest extends AbstractIntegrationTest {

    @MockitoSpyBean
    private ItemRepository itemRepository;

    @BeforeEach
    public void setUp() {
        Item item = itemRepository.save(Item.builder()
                .name("Test Item")
                .price(BigDecimal.TEN)
                .build());
    }

    @Test
    @DisplayName("Get all items")
    public void testGetAll() throws Exception {
        mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Get item by id")
    public void testGetById() throws Exception {
        mockMvc.perform(get("/api/items/{0}", "1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Get items by ids")
    public void testGetByIds() throws Exception {
        mockMvc.perform(get("/api/items/getByIds")
                        .param("ids", "1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Create item")
    public void testCreate() throws Exception {
        String itemRequestDto = """
                {
                  "id": 1,
                  "name": "Laptop",
                  "price": 500
                }""";

        mockMvc.perform(post("/api/items")
                        .content(itemRequestDto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("Update item by id")
    public void testUpdateById() throws Exception {
        String itemRequestDto = """
                {
                    "name": "Laptop 2",
                    "price": 1200
                }""";

        mockMvc.perform(put("/api/items/{id}", "1")
                        .content(itemRequestDto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Delete item by id")
    public void testDeleteById() throws Exception {
        mockMvc.perform(delete("/api/items/{0}", "1"))
                .andExpect(status().isNoContent());
    }

}
