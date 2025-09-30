package com.paypal.reward.service;


import com.paypal.reward.entity.Reward;
import com.paypal.reward.repository.RewardRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RewardServiceImpl implements RewardService{

    private RewardRepository rewardRepository;

    public RewardServiceImpl(RewardRepository repository){
        this.rewardRepository = repository;
    }


    @Override
    public Reward sendReward(Reward reward) {
        reward.setSentAt(LocalDateTime.now());
        return rewardRepository.save(reward);
    }

    @Override
    public List<Reward> getRewardsByUserId(Long userId) {
        return rewardRepository.findByUserId(userId);
    }
}
