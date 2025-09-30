package com.paypal.reward.repository;

import com.paypal.reward.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RewardRepository extends JpaRepository<Reward, Long> {

    List<Reward> findByUserId(Long userId);


    Boolean existsByTransactionId(Long transactionId);
}
