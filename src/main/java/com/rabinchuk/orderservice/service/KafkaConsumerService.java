package com.rabinchuk.orderservice.service;

import com.rabinchuk.orderservice.dto.PaymentCreatedEvent;
import com.rabinchuk.orderservice.model.OrderStatus;
import com.rabinchuk.orderservice.service.impl.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final OrderServiceImpl orderService;

    @KafkaListener(topics = "payment-created-topic", groupId = "order-service-group")
    public void handlePaymentCreatedEvent(PaymentCreatedEvent paymentCreatedEvent) {
        log.info("Received payment created event for order: {}", paymentCreatedEvent.orderId());
        try {
            OrderStatus newStatus = switch (paymentCreatedEvent.status()) {
                case SUCCESS -> OrderStatus.PAYMENT_SUCCESS;
                case FAILED -> OrderStatus.PAYMENT_FAILED;
            };
            orderService.updateOrderStatus(paymentCreatedEvent.orderId(), newStatus);
            log.info("Payment status updated for order: {}. New order status: {}", paymentCreatedEvent.orderId(), newStatus);
        } catch (Exception e) {
            log.error("Error while processing payment created event for order: {}", paymentCreatedEvent.orderId());
        }
    }
}
