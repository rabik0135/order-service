package com.rabinchuk.orderservice.service;

import com.rabinchuk.orderservice.client.UserClient;
import com.rabinchuk.orderservice.dto.CreateOrderRequestDto;
import com.rabinchuk.orderservice.dto.ItemResponseDto;
import com.rabinchuk.orderservice.dto.OrderItemDto;
import com.rabinchuk.orderservice.dto.OrderItemResponseDto;
import com.rabinchuk.orderservice.dto.OrderResponseDto;
import com.rabinchuk.orderservice.dto.OrderWithUserResponseDto;
import com.rabinchuk.orderservice.dto.UpdateOrderRequestDto;
import com.rabinchuk.orderservice.dto.UserResponseDto;
import com.rabinchuk.orderservice.mapper.OrderMapper;
import com.rabinchuk.orderservice.model.Item;
import com.rabinchuk.orderservice.model.Order;
import com.rabinchuk.orderservice.model.OrderItem;
import com.rabinchuk.orderservice.model.OrderStatus;
import com.rabinchuk.orderservice.repository.ItemRepository;
import com.rabinchuk.orderservice.repository.OrderRepository;
import com.rabinchuk.orderservice.service.impl.OrderServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private OrderServiceImpl orderService;

    private UserResponseDto userResponseDto;
    private Item item;
    private Order order;
    private OrderResponseDto orderResponseDto;
    private OrderWithUserResponseDto orderWithUserResponseDto;

    @BeforeEach
    public void setUp() {
        userResponseDto = UserResponseDto.builder()
                .id(1L)
                .name("John")
                .surname("Doe")
                .birthDate(LocalDate.of(1980, 1, 1))
                .email("john.doe@example.com")
                .build();

        item = Item.builder()
                .id(1L)
                .name("Item")
                .price(BigDecimal.TEN)
                .build();

        ItemResponseDto itemResponseDto = ItemResponseDto.builder()
                .id(1L)
                .name("Item")
                .price(BigDecimal.TEN)
                .build();

        order = Order.builder()
                .id(1L)
                .userId(1L)
                .orderStatus(OrderStatus.CREATED)
                .creationDate(LocalDateTime.now())
                .build();

        OrderItem orderItem = OrderItem.builder()
                .id(1L)
                .order(order)
                .item(item)
                .quantity(2)
                .build();
        order.setOrderItems(new HashSet<>(Set.of(orderItem)));

        OrderItemResponseDto orderItemResponseDto = OrderItemResponseDto.builder()
                .item(itemResponseDto)
                .quantity(2)
                .build();

        orderResponseDto = OrderResponseDto.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .status(order.getOrderStatus())
                .creationDate(order.getCreationDate())
                .items(List.of(orderItemResponseDto))
                .build();

        orderWithUserResponseDto = new OrderWithUserResponseDto(orderResponseDto, userResponseDto);
    }

    @Test
    @DisplayName("Get all orders")
    public void testGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(userClient.getUsersByIds(List.of(1L))).thenReturn(List.of(userResponseDto));
        when(orderMapper.toDto(order)).thenReturn(orderResponseDto);

        List<OrderWithUserResponseDto> result = orderService.getAll();

        assertThat(result).isEqualTo(List.of(orderWithUserResponseDto));
        verify(orderRepository, times(1)).findAll();
        verify(userClient, times(1)).getUsersByIds(List.of(1L));
        verify(orderMapper, times(1)).toDto(order);
    }


    @Test
    @DisplayName("Get order by id")
    public void testGetOrderById() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(userClient.getUsersByIds(List.of(1L))).thenReturn(List.of(userResponseDto));
        when(orderMapper.toDto(order)).thenReturn(orderResponseDto);

        OrderWithUserResponseDto result = orderService.getById(1L);

        assertThat(result).isNotNull();
        assertThat(result.order().id()).isEqualTo(1L);
        assertThat(result.user().id()).isEqualTo(1L);
        verify(orderRepository, times(1)).findById(1L);
        verify(userClient, times(1)).getUsersByIds(List.of(1L));
        verify(orderMapper, times(1)).toDto(order);
    }

    @Test
    @DisplayName("Get orders by ids")
    public void testGetOrdersByIds() {
        when(orderRepository.findAllById(List.of(1L))).thenReturn(List.of(order));
        when(userClient.getUsersByIds(List.of(1L))).thenReturn(List.of(userResponseDto));
        when(orderMapper.toDto(order)).thenReturn(orderResponseDto);

        List<OrderWithUserResponseDto> result = orderService.getByIds(List.of(1L));

        assertThat(result).isEqualTo(List.of(orderWithUserResponseDto));
        verify(orderRepository, times(1)).findAllById(List.of(1L));
        verify(userClient, times(1)).getUsersByIds(List.of(1L));
        verify(orderMapper, times(1)).toDto(order);
    }

    @Test
    @DisplayName("Create order")
    public void testCreateOrder() {
        CreateOrderRequestDto createOrderRequestDto = CreateOrderRequestDto.builder()
                .userId(1L)
                .orderItems(List.of(new OrderItemDto(1L, 2)))
                .build();

        when(itemRepository.findAllById(List.of(1L))).thenReturn(List.of(item));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(userClient.getUsersByIds(List.of(1L))).thenReturn(List.of(userResponseDto));
        when(orderMapper.toDto(order)).thenReturn(orderResponseDto);

        OrderWithUserResponseDto result = orderService.create(createOrderRequestDto);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(orderWithUserResponseDto);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(userClient, times(1)).getUsersByIds(List.of(1L));
        verify(orderMapper, times(1)).toDto(order);
    }

    @Test
    @DisplayName("Update order")
    public void testUpdateOrder() {
        UpdateOrderRequestDto updateOrderRequestDto = UpdateOrderRequestDto.builder()
                .orderStatus(OrderStatus.PROCESSING)
                .items(List.of(new OrderItemDto(1L, 3)))
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(itemRepository.findAllById(List.of(1L))).thenReturn(List.of(item));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(userClient.getUsersByIds(List.of(1L))).thenReturn(List.of(userResponseDto));
        when(orderMapper.toDto(order)).thenReturn(orderResponseDto);

        OrderWithUserResponseDto result = orderService.updateById(1l, updateOrderRequestDto);

        assertThat(result).isNotNull();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.PROCESSING);
        assertThat(order.getOrderItems().size()).isEqualTo(1);
        assertThat(order.getOrderItems().iterator().next().getQuantity()).isEqualTo(3);

        verify(orderRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).findAllById(List.of(1L));
        verify(orderMapper, times(1)).toDto(order);
        verify(userClient, times(1)).getUsersByIds(List.of(1L));
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("Get orders by statuses")
    public void testGetOrdersByStatuses() {
        when(orderRepository.findAllByOrderStatusIn(List.of(OrderStatus.CREATED))).thenReturn(List.of(order));
        when(userClient.getUsersByIds(List.of(1L))).thenReturn(List.of(userResponseDto));
        when(orderMapper.toDto(order)).thenReturn(orderResponseDto);

        List<OrderWithUserResponseDto> result = orderService.getByStatuses(List.of(OrderStatus.CREATED));

        assertThat(result).isEqualTo(List.of(orderWithUserResponseDto));
        verify(orderRepository, times(1)).findAllByOrderStatusIn(List.of(OrderStatus.CREATED));
        verify(userClient, times(1)).getUsersByIds(List.of(1L));
        verify(orderMapper, times(1)).toDto(order);
    }

    @Test
    @DisplayName("Get order by user id")
    public void testGetOrderByUserId() {
        when(orderRepository.findAllByUserId(1L)).thenReturn(List.of(order));
        when(userClient.getUsersByIds(List.of(1L))).thenReturn(List.of(userResponseDto));
        when(orderMapper.toDto(order)).thenReturn(orderResponseDto);

        List<OrderWithUserResponseDto> result = orderService.getByUserId(1L);

        assertThat(result).isEqualTo(List.of(orderWithUserResponseDto));
        verify(orderRepository, times(1)).findAllByUserId(1L);
        verify(userClient, times(2)).getUsersByIds(List.of(1L));
        verify(orderMapper, times(1)).toDto(order);
    }

    @Test
    @DisplayName("Update order status")
    public void testUpdateOrderStatus() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(userClient.getUsersByIds(List.of(1L))).thenReturn(List.of(userResponseDto));
        when(orderMapper.toDto(order)).thenReturn(orderResponseDto);

        orderService.updateOrderStatus(1L, OrderStatus.PROCESSING);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.PROCESSING);
        verify(orderRepository, times(1)).findById(1L);
        verify(userClient, times(1)).getUsersByIds(List.of(1L));
        verify(orderMapper, times(1)).toDto(order);
    }

    @Test
    @DisplayName("Delete order")
    public void testDeleteOrder() {
        when(orderRepository.existsById(1L)).thenReturn(true);

        orderService.deleteById(1L);

        verify(orderRepository, times(1)).existsById(1L);
        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Get all should return empty list")
    public void testGetAllShouldReturnEmptyList() {
        when(orderRepository.findAll()).thenReturn(List.of());
        List<OrderWithUserResponseDto> result = orderService.getAll();
        assertThat(result).isEmpty();
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get order by invalid id should throw exception")
    public void testGetOrderByIdInvalidIdShouldThrowException() {
        when(orderRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.getById(100L));
    }

    @Test
    @DisplayName("Create order with invalid item should throw exception")
    public void testCreateOrderWithInvalidItemShouldThrowException() {
        CreateOrderRequestDto createOrderRequestDto = CreateOrderRequestDto.builder()
                .userId(1L)
                .orderItems(List.of(new OrderItemDto(100L, 2)))
                .build();
        when(itemRepository.findAllById(List.of(100L))).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> orderService.create(createOrderRequestDto));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Update order by invalid id should throw exception")
    public void testUpdateOrderByInvalidIdShouldThrowException() {
        UpdateOrderRequestDto updateOrderRequestDto = UpdateOrderRequestDto.builder()
                .orderStatus(OrderStatus.PROCESSING)
                .items(List.of(new OrderItemDto(1L, 3)))
                .build();
        when(orderRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.updateById(100L, updateOrderRequestDto));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Update order status by invalid id should throw exception")
    public void testUpdateOrderStatusByInvalidIdShouldThrowException() {
        when(orderRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.updateOrderStatus(100L, OrderStatus.PROCESSING));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Delete order by invalid id should throw exception")
    public void testDeleteOrderByInvalidIdShouldThrowException() {
        when(orderRepository.existsById(100L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> orderService.deleteById(100L));
        verify(orderRepository, never()).deleteById(100L);
    }

}
