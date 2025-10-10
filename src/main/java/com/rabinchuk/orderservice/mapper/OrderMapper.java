package com.rabinchuk.orderservice.mapper;

import com.rabinchuk.orderservice.dto.CreateOrderRequestDto;
import com.rabinchuk.orderservice.dto.OrderItemResponseDto;
import com.rabinchuk.orderservice.dto.OrderResponseDto;
import com.rabinchuk.orderservice.model.Order;
import com.rabinchuk.orderservice.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", uses = {ItemMapper.class})
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderStatus", constant = "CREATED")
    @Mapping(target = "creationDate", expression = "java(getCurrentDateTime())")
    Order toEntity(CreateOrderRequestDto createOrderRequestDto);

    @Mapping(source = "orderStatus", target = "status")
    @Mapping(source = "orderItems", target = "items")
    OrderResponseDto toDto(Order order);

    OrderItemResponseDto toDto(OrderItem orderItem);

    default LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

}
