package com.payment.transaction.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.payment.transaction.entity.Transaction;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

public class KafkaEventProducer {


    private static final String TOPIC = "txn-initiated";

    private final KafkaTemplate<String, Transaction> kafkaTemplate;
    private final ObjectMapper objectMapper;


    public KafkaEventProducer(KafkaTemplate<String, Transaction> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;

        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public void sendTransactionEvent(String key, Transaction transaction){
        System.out.println("Sending to Kafka -> Topic " + TOPIC + ", Key: " + key + ", Message: " + transaction);

        CompletableFuture<SendResult<String, Transaction>> send = kafkaTemplate.send(TOPIC, key, transaction);

        send.thenAccept(result -> {
            RecordMetadata metadata = result.getRecordMetadata();
            System.out.println("Kafka message sent successfully! Topic: " + metadata.topic() + ", Partition: " + metadata.partition() + ", offset: " + metadata.offset());
        }).exceptionally(ex -> {
            System.err.println("Failed to send kafka message: "+ex.getMessage());
            ex.printStackTrace();
            return null;
        });
    }


}
