package com.rabinchuk.orderservice.mapper;

import com.rabinchuk.orderservice.dto.OrderCreatedEvent;
import com.rabinchuk.orderservice.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "userId", source = "order.userId")
    OrderCreatedEvent toOrderCreatedEvent(Order order, BigDecimal paymentAmount);
}
