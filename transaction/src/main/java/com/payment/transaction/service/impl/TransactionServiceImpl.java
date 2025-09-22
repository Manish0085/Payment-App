package com.payment.transaction.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.transaction.entity.Transaction;
import com.payment.transaction.kafka.KafkaEventProducer;
import com.payment.transaction.repository.TransactionRepository;
import com.payment.transaction.service.ITransactionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class TransactionServiceImpl implements ITransactionService {

    private final TransactionRepository repository;

    private final ObjectMapper mapper;

    private final KafkaEventProducer kafkaEventProducer;

    public TransactionServiceImpl(TransactionRepository repository,
                                  KafkaEventProducer kafkaEventProducer,
                                  ObjectMapper mapper) {
        this.repository = repository;
        this.kafkaEventProducer = kafkaEventProducer;
        this.mapper = mapper;
    }


    @Override
    public Transaction createTransaction(Transaction request) {
        System.out.println("🚀 Entered createTransaction()");

        Long senderId = request.getSenderId();
        Long receiverId = request.getReceiverId();
        Double amount = request.getAmount();


        Transaction transaction = new Transaction();
        transaction.setSenderId(senderId);
        transaction.setReceiverId(receiverId);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setStatus("SUCCESS");

        System.out.println("📥 Incoming Transaction object: " + transaction);

        Transaction saved = repository.save(transaction);
        System.out.println("💾 Saved Transaction from DB: " + saved);

        try {
            String key = String.valueOf(saved.getId());
            kafkaEventProducer.sendTransactionEvent(key, saved);

            System.out.println("Kafka message sent");
        } catch (Exception e){
            System.err.println("Failed to send Kafka event: " + e.getMessage());
            e.printStackTrace();
        }
        return saved;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return repository.findAll();
    }
}
