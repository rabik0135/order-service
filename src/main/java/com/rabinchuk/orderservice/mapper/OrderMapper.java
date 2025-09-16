package com.rabinchuk.orderservice.mapper;

import com.rabinchuk.orderservice.dto.ItemResponseDto;
import com.rabinchuk.orderservice.dto.OrderResponseDto;
import com.rabinchuk.orderservice.model.Order;
import com.rabinchuk.orderservice.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ItemMapper.class})
public interface OrderMapper {

    @Mapping(source = "orderStatus", target = "status")
    @Mapping(source = "orderItems", target = "items")
    OrderResponseDto toDto(Order order);

    @Mapping(source = "item", target = ".")
    ItemResponseDto itemFromOrderItem(OrderItem orderItem);

}
