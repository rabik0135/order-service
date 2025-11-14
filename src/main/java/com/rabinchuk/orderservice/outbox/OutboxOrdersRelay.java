package com.rabinchuk.orderservice.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabinchuk.orderservice.dto.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxOrdersRelay {

    private final OutboxOrdersRepository outboxOrdersRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 10000)
    @SneakyThrows
    public void processEvents() {
        List<OutboxOrders> outboxOrders = outboxOrdersRepository.findAllByStatus(EventStatus.PENDING);

        if (outboxOrders.isEmpty()) {
            return;
        }

        for (OutboxOrders event : outboxOrders) {
            OrderCreatedEvent orderCreatedEvent = objectMapper.readValue(event.getPayload(), OrderCreatedEvent.class);
            kafkaTemplate.send(event.getTopic(), orderCreatedEvent);
            event.setStatus(EventStatus.PROCESSED);
            outboxOrdersRepository.save(event);
        }
    }

}
