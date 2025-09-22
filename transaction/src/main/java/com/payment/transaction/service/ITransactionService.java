package com.payment.transaction.service;

import com.payment.transaction.entity.Transaction;

import java.util.List;

public interface ITransactionService {

    Transaction createTransaction(Transaction transaction);

    List<Transaction> getAllTransactions();
}
