package com.paypal.reward.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.paypal.reward.entity.Reward;
import com.paypal.common.entity.Transaction;
import com.paypal.reward.repository.RewardRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RewardConsumer {


    private final RewardRepository repo;

    private final ObjectMapper mapper;


    public RewardConsumer(RewardRepository repo, ObjectMapper mapper){
        this.repo = repo;
        this.mapper = new ObjectMapper();

        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }


    @KafkaListener(topics = "txn-initiated", groupId = "reward-group")
    public void consumeTransaction(Transaction transaction){
        try{
            if (repo.existsByTransactionId(transaction.getId())){
                System.out.println("⚠️ Reward already exists for transaction: " + transaction.getId());
                return;
            }

            Reward reward = new Reward();
            reward.setUserId(transaction.getSenderId());
            reward.setPoints(transaction.getAmount() * 100);
            reward.setSentAt(LocalDateTime.now());
            reward.setTransactionId(transaction.getId());

            repo.save(reward);

            System.out.println("Reward saved: " + reward);
        }
        catch (Exception e){
            System.err.println("Failed to process transaction " + transaction.getId() + ": " + e.getMessage());
            throw e;
        }
    }

}
