package com.payment.notification.kafka;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.payment.notification.entity.Notification;
import com.payment.notification.entity.Transaction;
import com.payment.notification.repository.NotificationRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NotificationConsumer {

    private final NotificationRepository repository;
    private final ObjectMapper mapper;


    public NotificationConsumer(NotificationRepository repository) {
        this.repository = repository;

        // Setup ObjectMapper with JavaTimeModule to handle LocalDateTime
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }


    @KafkaListener(topics = "txn-initiated", groupId = "notification-group")
    public void consumeTransaction(Transaction transaction) throws JsonProcessingException {
        System.out.println("Received transaction " + transaction);

        Notification notification = new Notification();
        notification.setUserId(transaction.getSenderId());
        notification.setMessage("₹ " + transaction.getAmount() + " received from user " + transaction.getSenderId());
        notification.setSentAt(LocalDateTime.now());


        repository.save(notification);
        System.out.println("Saving notification: " + notification);
    }


}
