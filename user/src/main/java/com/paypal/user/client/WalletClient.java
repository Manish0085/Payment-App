package com.paypal.user.client;


import com.paypal.user.dto.CreateWalletRequest;
import com.paypal.user.dto.WalletResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "wallet-service", url = "http://localhost:8088/api/v1/wallets")
public interface WalletClient {

    @PostMapping
    WalletResponse createWallet(@RequestBody CreateWalletRequest request);
}