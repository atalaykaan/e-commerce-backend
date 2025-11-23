package com.atalaykaan.e_commerce_backend.domain.log.service;

import com.atalaykaan.e_commerce_backend.domain.order.model.Order;
import com.atalaykaan.e_commerce_backend.log.model.LogModel;
import com.atalaykaan.e_commerce_backend.log.repository.LogModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogEventConsumerService {

    private final LogModelRepository logModelRepository;

    @KafkaListener(topics = {"${spring.kafka.topic.order-created}", "${spring.kafka.topic.order-updated}"},
            groupId = "log-group")
    public void handleMessage(Order order) {

        LogModel logModel = new LogModel();
        logModel.setMessage(order.toString());

        log.info("Received message: {}", order.toString());

        logModelRepository.save(logModel);
    }
}
