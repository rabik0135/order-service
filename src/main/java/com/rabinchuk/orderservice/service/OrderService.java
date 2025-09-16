package com.rabinchuk.orderservice.service;

import com.rabinchuk.orderservice.dto.CreateOrderRequestDto;
import com.rabinchuk.orderservice.dto.OrderWithUserResponseDto;
import com.rabinchuk.orderservice.dto.UpdateOrderRequestDto;
import com.rabinchuk.orderservice.model.OrderStatus;

import java.util.List;

public interface OrderService extends CRUDService<OrderWithUserResponseDto, CreateOrderRequestDto, UpdateOrderRequestDto> {

    List<OrderWithUserResponseDto> getByStatuses(List<OrderStatus> statuses);

    List<OrderWithUserResponseDto> getByUserId(Long userId);

    OrderWithUserResponseDto updateOrderStatus(Long orderId, OrderStatus orderStatus);

}
