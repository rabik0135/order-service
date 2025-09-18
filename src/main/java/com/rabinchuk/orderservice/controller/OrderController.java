package com.rabinchuk.orderservice.controller;

import com.rabinchuk.orderservice.controller.api.OrderApi;
import com.rabinchuk.orderservice.dto.CreateOrderRequestDto;
import com.rabinchuk.orderservice.dto.OrderWithUserResponseDto;
import com.rabinchuk.orderservice.dto.UpdateOrderRequestDto;
import com.rabinchuk.orderservice.model.OrderStatus;
import com.rabinchuk.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController implements OrderApi {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderWithUserResponseDto>> getAll() {
        List<OrderWithUserResponseDto> orderWithUserResponseDtos = orderService.getAll();
        return ResponseEntity.ok(orderWithUserResponseDtos);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<OrderWithUserResponseDto> getById(@PathVariable Long id) {
        OrderWithUserResponseDto orderWithUserResponseDto = orderService.getById(id);
        return ResponseEntity.ok(orderWithUserResponseDto);
    }

    @GetMapping(value = "/getByIds", params = "ids")
    public ResponseEntity<List<OrderWithUserResponseDto>> getByIds(@RequestParam List<Long> ids) {
        List<OrderWithUserResponseDto> orderWithUserResponseDtos = orderService.getByIds(ids);
        return ResponseEntity.ok(orderWithUserResponseDtos);
    }

    @PostMapping
    public ResponseEntity<OrderWithUserResponseDto> create(@Valid @RequestBody CreateOrderRequestDto c) {
        OrderWithUserResponseDto orderWithUserResponseDto = orderService.create(c);
        return new ResponseEntity<>(orderWithUserResponseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderWithUserResponseDto> updateById(@PathVariable Long id, @Valid @RequestBody UpdateOrderRequestDto u) {
        OrderWithUserResponseDto orderWithUserResponseDto = orderService.updateById(id, u);
        return ResponseEntity.ok(orderWithUserResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        orderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/getByStatuses", params = "statuses")
    public ResponseEntity<List<OrderWithUserResponseDto>> getByStatuses(@RequestParam List<OrderStatus> statuses) {
        List<OrderWithUserResponseDto> orderWithUserResponseDtos = orderService.getByStatuses(statuses);
        return ResponseEntity.ok(orderWithUserResponseDtos);
    }

    @GetMapping("/getByUserId/{userId}")
    public ResponseEntity<List<OrderWithUserResponseDto>> getByUserId(@PathVariable Long userId) {
        List<OrderWithUserResponseDto> orderWithUserResponseDtos = orderService.getByUserId(userId);
        return ResponseEntity.ok(orderWithUserResponseDtos);
    }

    @PutMapping("/updateStatus/{orderId}")
    public ResponseEntity<OrderWithUserResponseDto> updateOrderStatus(@PathVariable Long orderId, @RequestParam OrderStatus orderStatus) {
        OrderWithUserResponseDto orderWithUserResponseDto = orderService.updateOrderStatus(orderId, orderStatus);
        return ResponseEntity.ok(orderWithUserResponseDto);
    }

}
