package com.paypal.reward.service;

import com.paypal.reward.entity.Reward;

import java.util.List;

public interface RewardService {

    Reward sendReward(Reward reward);


    List<Reward> getRewardsByUserId(Long UserId);


}
