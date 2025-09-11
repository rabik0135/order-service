package com.rabinchuk.orderservice.repository;

import com.rabinchuk.orderservice.model.Order;
import com.rabinchuk.orderservice.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderStatus(OrderStatus status);

}
