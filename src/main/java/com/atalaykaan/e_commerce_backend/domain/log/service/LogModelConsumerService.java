package com.atalaykaan.e_commerce_backend.log.service;

import com.atalaykaan.e_commerce_backend.log.model.LogModel;
import com.atalaykaan.e_commerce_backend.log.repository.LogModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogModelConsumerService {

    private final LogModelRepository logModelRepository;

    @KafkaListener(topics = "${spring.kafka.topic}", groupId = "${spring.kafka.group.id}")
    public void handleMessage(String message) {

        LogModel logModel = new LogModel();
        logModel.setMessage(message);

        log.info("Received message: {}", message);

        logModelRepository.save(logModel);
    }
}
