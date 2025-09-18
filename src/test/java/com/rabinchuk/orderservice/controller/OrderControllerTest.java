package com.rabinchuk.orderservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.rabinchuk.orderservice.AbstractIntegrationTest;
import com.rabinchuk.orderservice.dto.UserResponseDto;
import com.rabinchuk.orderservice.model.Item;
import com.rabinchuk.orderservice.model.Order;
import com.rabinchuk.orderservice.model.OrderItem;
import com.rabinchuk.orderservice.model.OrderStatus;
import com.rabinchuk.orderservice.repository.ItemRepository;
import com.rabinchuk.orderservice.repository.OrderRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderControllerTest extends AbstractIntegrationTest {

    @MockitoSpyBean
    private OrderController orderController;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @BeforeEach
    public void setup() {
        wireMock.resetAll();

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(1L)
                .name("John")
                .surname("Doe")
                .birthDate(LocalDate.of(1980, 1, 1))
                .email("john.doe@example.com")
                .build();

        Item item = itemRepository.save(
                Item.builder()
                        .name("Item")
                        .price(BigDecimal.TEN)
                        .build()
        );

        Order testOrder = Order.builder()
                .userId(userResponseDto.id())
                .orderStatus(OrderStatus.CREATED)
                .creationDate(LocalDate.now().atStartOfDay())
                .build();

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .quantity(2)
                .order(testOrder)
                .build();

        testOrder.addOrderItem(orderItem);

        Order order = orderRepository.save(testOrder);
        stubUserClient(userResponseDto);
    }

    @Nested
    @DisplayName("Happy scenarios")
    public class HappyScenarios {

        @Test
        @DisplayName("Get all orders")
        public void testGetAll() throws Exception {
            mockMvc.perform(get("/api/orders"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/happy/get_orders_response.json"), false));
        }

        @Test
        @DisplayName("Get order by id")
        public void testGetById() throws Exception {
            mockMvc.perform(get("/api/orders/getById/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/happy/get_order_by_id_response.json"), false));
        }

        @Test
        @DisplayName("Get orders by ids")
        public void getByIds() throws Exception {
            mockMvc.perform(get("/api/orders/getByIds?ids=1"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/happy/get_orders_response.json"), false));
        }

        @Test
        @DisplayName("Create order")
        public void testCreate() throws Exception {
            String createOrderRequestJson = AbstractIntegrationTest.readStringFromSource("json/happy/create_order_request.json");

            mockMvc.perform(post("/api/orders")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createOrderRequestJson))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/happy/create_order_response.json"), false));
        }

        @Test
        @DisplayName("Update order by id")
        public void testUpdateById() throws Exception {
            String updateOrderRequestJson = AbstractIntegrationTest.readStringFromSource("json/happy/update_order_request.json");

            mockMvc.perform(put("/api/orders/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updateOrderRequestJson))
                    .andExpect(status().isOk())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/happy/update_order_response.json")));
        }

        @Test
        @DisplayName("Delete order by id")
        public void deleteById() throws Exception {
            mockMvc.perform(delete("/api/orders/1"))
                    .andExpect(status().isNoContent());
            mockMvc.perform(get("/api/orders/getById/1"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Get orders by statuses")
        public void testGetByStatuses() throws Exception {
            mockMvc.perform(get("/api/orders/getByStatuses?statuses=CREATED"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/happy/get_orders_response.json"), false));
        }

        @Test
        @DisplayName("Get order by user id ")
        public void testGetByUserId() throws Exception {
            mockMvc.perform(get("/api/orders/getByUserId/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/happy/get_orders_response.json"), false));
        }

        @Test
        @DisplayName("Update order status")
        public void testUpdateOrderStatus() throws Exception {
            mockMvc.perform(put("/api/orders/updateStatus/1")
                            .param("orderStatus", "DELIVERED")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/happy/update_order_status_response.json"), false));
        }
    }

    @Nested
    @DisplayName("Error scenarios")
    public class ErrorScenarios {
        @Test
        @DisplayName("Get all orders should return empty list")
        public void testGetAllOrdersShouldReturnEmptyList() throws Exception {
            orderRepository.deleteAll();

            mockMvc.perform(get("/api/orders"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/error/blank_list_response.json"), false));
        }

        @Test
        @DisplayName("Get order by id should return error")
        public void testGetByIdShouldReturnError() throws Exception {
            mockMvc.perform(get("/api/orders/getById/1321"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/error/get_order_by_id_not_found_response.json"), false));
        }

        @Test
        @DisplayName("Create order should return exception")
        public void testCreateShouldReturnException() throws Exception {
            String createOrderRequestJson = AbstractIntegrationTest.readStringFromSource("json/error/create_invalid_order_request.json");

            mockMvc.perform(post("/api/orders")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createOrderRequestJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/error/create_order_error_response.json"), false));
        }

        @Test
        @DisplayName("Update order by invalid id should return exception")
        public void testUpdateUserByInvalidIdShouldReturnException() throws Exception {
            String updateOrderRequestJson = AbstractIntegrationTest.readStringFromSource("json/error/update_order_with_invalid_id_request.json");

            mockMvc.perform(put("/api/orders/1321")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updateOrderRequestJson))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/error/update_order_with_invalid_id_response.json")));
        }

        @Test
        @DisplayName("Update order with invalid fields should return exception")
        public void testUpdateUserByInvalidFieldsShouldReturnException() throws Exception {
            String updateOrderRequestJson = AbstractIntegrationTest.readStringFromSource("json/error/update_order_with_invalid_fields_request.json");

            mockMvc.perform(put("/api/orders/1321")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updateOrderRequestJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/error/update_order_with_invalid_fields_response.json")));
        }

        @Test
        @DisplayName("Delete order by invalid id should return exception")
        public void testDeleteUserByInvalidIdShouldReturnException() throws Exception {
            mockMvc.perform(delete("/api/orders/1321"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/error/delete_order_by_id_not_found_response.json"), false));
        }

        @Test
        @DisplayName("Get orders by statuses should return empty list")
        public void testGetByStatusesShouldReturnEmptyList() throws Exception {
            mockMvc.perform(get("/api/orders/getByStatuses?statuses=DELIVERED"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/error/blank_list_response.json"), false));
        }

        @Test
        @DisplayName("Get order by invalid user id should return exception")
        public void testGetByInvalidUserIdShouldReturnException() throws Exception {
            long invalidUserId = 12312L;

            wireMock.stubFor(WireMock.get(urlPathEqualTo("/api/users/getByIds"))
                    .withQueryParam("ids", containing(String.valueOf(invalidUserId)))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                            .withBody("[]")));

            mockMvc.perform(get("/api/orders/getByUserId/12312"))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/error/get_order_by_invalid_user_id_response.json"), false));
        }

        @Test
        @DisplayName("Update order status by invalid id should return exception")
        public void testUpdateOrderStatusByInvalidIdShouldReturnException() throws Exception {
            mockMvc.perform(put("/api/orders/updateStatus/1321")
                            .param("orderStatus", "DELIVERED")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/error/update_order_status_not_found_response.json"), false));
        }
    }

    private void stubUserClient(UserResponseDto userToReturn) throws JsonProcessingException {
        wireMock.stubFor(WireMock.get(urlPathEqualTo("/api/users/getByIds")) // Change this line
                .withQueryParam("ids", containing(String.valueOf(userToReturn.id())))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(List.of(userToReturn)))));
    }

}
