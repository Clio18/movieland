package com.tteam.movieland.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Properties;

@Component
public class MyKafkaConsumerFactory {
    public KafkaConsumer<String, byte[]> getConsumer() {
        KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(getProperties());
        String topic = "allMoviesRequest";
        consumer.subscribe(Collections.singletonList(topic));
        return consumer;
    }

    @Bean
    private Properties getProperties() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group_one");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getCanonicalName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getCanonicalName());
        return properties;
    }
}