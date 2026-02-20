package com.nvminh162.notificationservice.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EventConsumer {
    
    @KafkaListener(topics = "test", containerFactory = "kafkaListenerContainerFactory")
     public void listen(String message){
        log.info("Received message: " + message);
    }
}
