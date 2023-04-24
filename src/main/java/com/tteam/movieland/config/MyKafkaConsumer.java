package com.tteam.movieland.config;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@NoArgsConstructor
public class MyKafkaConsumer implements KafkaConsumer<String, byte[]> {

    @Override
//    @KafkaListener(topics = "allMoviesRequest")
    public void listen(ConsumerRecord<String, byte[]> record, Acknowledgment ack) {
        try {
            log.info("Received message: key={}, value={}", record.key(), record.value());
        } catch (Exception e) {
            log.error("Error while processing message: {}", e.getMessage(), e);
        } finally {
            if (ack != null) {
                ack.acknowledge();
            }
        }
    }
}
