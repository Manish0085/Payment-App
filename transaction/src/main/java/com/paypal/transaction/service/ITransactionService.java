package com.paypal.transaction.service;

import com.paypal.common.entity.Transaction;


import java.util.List;

public interface ITransactionService {

    Transaction createTransaction(Transaction transaction);

    List<Transaction> getAllTransactions();
}
