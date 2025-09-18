package com.rabinchuk.orderservice.service.impl;

import com.rabinchuk.orderservice.client.UserClient;
import com.rabinchuk.orderservice.dto.CreateOrderRequestDto;
import com.rabinchuk.orderservice.dto.OrderItemDto;
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
import com.rabinchuk.orderservice.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserClient userClient;


    @Override
    @Transactional(readOnly = true)
    public List<OrderWithUserResponseDto> getAll() {
        List<Order> orders = orderRepository.findAll();
        return enrichOrdersWithUserData(orders);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderWithUserResponseDto getById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Order with id " + id + " not found")
        );
        return enrichOrdersWithUserData(List.of(order)).getFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderWithUserResponseDto> getByIds(List<Long> ids) {
        List<Order> orders = orderRepository.findAllById(ids);
        return enrichOrdersWithUserData(orders);
    }

    @Override
    @Transactional
    public OrderWithUserResponseDto create(CreateOrderRequestDto requestDto) {
        Order order = Order.builder()
                .userId(requestDto.userId())
                .orderStatus(OrderStatus.CREATED)
                .creationDate(LocalDateTime.now())
                .build();

        Set<OrderItem> orderItems = processOrderItems(requestDto.orderItems(), order);
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        return enrichOrdersWithUserData(List.of(savedOrder)).getFirst();
    }

    @Override
    @Transactional
    public OrderWithUserResponseDto updateById(Long id, UpdateOrderRequestDto requestDto) {
        Order orderToUpdate = orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Order with id " + id + " not found")
        );

        if (requestDto.orderStatus() != null) {
            orderToUpdate.setOrderStatus(requestDto.orderStatus());
        }

        if (requestDto.items() != null) {
            orderToUpdate.getOrderItems().clear();
            Set<OrderItem> updatedOrderItems = processOrderItems(requestDto.items(), orderToUpdate);
            orderToUpdate.getOrderItems().addAll(updatedOrderItems);
        }

        Order savedOrder = orderRepository.save(orderToUpdate);
        return enrichOrdersWithUserData(List.of(savedOrder)).getFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderWithUserResponseDto> getByStatuses(List<OrderStatus> statuses) {
        List<Order> orders = orderRepository.findAllByOrderStatusIn(statuses);
        return enrichOrdersWithUserData(orders);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderWithUserResponseDto> getByUserId(Long userId) {
        List<UserResponseDto> userResponseDto = userClient.getUsersByIds(List.of(userId));
        if (userResponseDto.isEmpty()) {
            throw new EntityNotFoundException("User with id " + userId + " not found");
        }

        List<Order> orders = orderRepository.findAllByUserId(userId);
        return enrichOrdersWithUserData(orders);
    }

    @Override
    @Transactional
    public OrderWithUserResponseDto updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Order with id " + orderId + " not found")
        );
        order.setOrderStatus(orderStatus);

        return enrichOrdersWithUserData(List.of(order)).getFirst();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("Order with id " + id + " not found");
        }
        orderRepository.deleteById(id);
    }

    private List<OrderWithUserResponseDto> enrichOrdersWithUserData(List<Order> orders) {
        if (orders.isEmpty()) {
            return List.of();
        }

        List<Long> userIds = orders.stream()
                .map(Order::getUserId)
                .distinct()
                .toList();

        if (userIds.isEmpty()) {
            throw new EntityNotFoundException("User with id " + userIds + " not found");
        }

        Map<Long, UserResponseDto> userResponseDtoMap = userClient.getUsersByIds(userIds)
                .stream()
                .collect(Collectors.toMap(UserResponseDto::id, user -> user));

        return orders.stream()
                .map(order -> {
                    OrderResponseDto orderResponseDto = orderMapper.toDto(order);
                    UserResponseDto userResponseDto = userResponseDtoMap.get(order.getUserId());
                    return new OrderWithUserResponseDto(orderResponseDto, userResponseDto);
                })
                .collect(Collectors.toList());
    }

    private Set<OrderItem> processOrderItems(List<OrderItemDto> orderItemDtos, Order order) {
        if (orderItemDtos == null || orderItemDtos.isEmpty()) {
            return Set.of();
        }

        List<Long> itemIds = orderItemDtos.stream()
                .map(OrderItemDto::itemId)
                .toList();
        List<Item> foundItems = itemRepository.findAllById(itemIds);
        if (foundItems.size() != itemIds.size()) {
            throw new EntityNotFoundException("Some items were not found");
        }

        Map<Long, Item> itemMap =  foundItems.stream()
                .collect(Collectors.toMap(Item::getId, item -> item));

        return orderItemDtos.stream()
                .map(orderItemDto -> OrderItem.builder()
                        .order(order)
                        .item(itemMap.get(orderItemDto.itemId()))
                        .quantity(orderItemDto.quantity())
                        .build())
                .collect(Collectors.toSet());
    }

}
