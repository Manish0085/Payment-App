package com.payment.reward.service;

import com.payment.reward.entity.Reward;

import java.util.List;

public interface RewardService {

    Reward sendReward(Reward reward);


    List<Reward> getRewardsByUserId(Long UserId);


}
