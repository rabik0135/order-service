package com.rabinchuk.orderservice.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabinchuk.orderservice.dto.OrderCreatedEvent;
import com.rabinchuk.orderservice.model.Order;
import com.rabinchuk.orderservice.outbox.OutboxOrders;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface OutboxOrdersMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(getCurrentDateTime())")
    @Mapping(target = "orderId", source = "createdOrder.id")
    @Mapping(target = "topic", constant = "order-created-topic")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(
            target = "payload",
            expression = "java(objectMapper.writeValueAsString(orderCreatedEvent))"
    )
    OutboxOrders toEntity(Order createdOrder,
                            OrderCreatedEvent orderCreatedEvent,
                            @Context ObjectMapper objectMapper) throws JsonProcessingException;

    default LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

}
