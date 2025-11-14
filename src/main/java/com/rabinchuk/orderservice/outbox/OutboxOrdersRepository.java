package com.rabinchuk.orderservice.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxOrdersRepository extends JpaRepository<OutboxOrders, Long> {

    List<OutboxOrders> findAllByStatus(EventStatus status);

}
