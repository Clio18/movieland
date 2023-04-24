package com.tteam.movieland.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;

public interface KafkaConsumer<K, V> {
    void listen(ConsumerRecord<K, V> record, Acknowledgment ack);
}