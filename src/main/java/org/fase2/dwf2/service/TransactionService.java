package org.fase2.dwf2.service;

import org.fase2.dwf2.dto.Transaction.TransactionRequestDto;
import org.fase2.dwf2.entities.Account;
import org.fase2.dwf2.entities.Transaction;
import org.fase2.dwf2.enums.TransactionType;
import org.fase2.dwf2.repository.IAccountRepository;
import org.fase2.dwf2.repository.ITransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final ITransactionRepository transactionRepository;
    private final IAccountRepository accountRepository;



    @Autowired
    public TransactionService(ITransactionRepository transactionRepository, IAccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public TransactionRequestDto deposit(TransactionRequestDto transactionRequestDto) {
        if (transactionRequestDto.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        Account account = accountRepository.findByAccountNumber(transactionRequestDto.getAccountNumber());

        if (account == null) {
            throw new IllegalArgumentException("Account does not exist");
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionRequestDto.getAmount());
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setAccount(account);
        transactionRepository.save(transaction);

        account.setBalance(account.getBalance() + transactionRequestDto.getAmount());
        accountRepository.save(account);

        transactionRequestDto.setTransactionType(TransactionType.DEPOSIT.toString());

        return transactionRequestDto;
    }

    @Transactional
    public TransactionRequestDto withdraw(TransactionRequestDto transactionRequestDto) {
        if (transactionRequestDto.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        Account account = accountRepository.findByAccountNumber(transactionRequestDto.getAccountNumber());

        if (account == null) {
            throw new IllegalArgumentException("Account does not exist");
        }

        if (account.getBalance() < transactionRequestDto.getAmount()) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionRequestDto.getAmount());
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setAccount(account);
        transactionRepository.save(transaction);

        account.setBalance(account.getBalance() - transactionRequestDto.getAmount());
        accountRepository.save(account);

        transactionRequestDto.setTransactionType(TransactionType.WITHDRAW.toString());

        return transactionRequestDto;
    }

    @Transactional
    public TransactionRequestDto transfer(TransactionRequestDto transactionRequestDto) {
        if (transactionRequestDto.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        Account accountFrom = accountRepository.findByAccountNumber(transactionRequestDto.getAccountNumber());
        Account accountTo = accountRepository.findByAccountNumber(transactionRequestDto.getAccountNumberTo());

        if (accountFrom == null || accountTo == null) {
            throw new IllegalArgumentException("Account does not exist");
        }

        if (accountFrom.getBalance() < transactionRequestDto.getAmount()) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionRequestDto.getAmount());
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setAccount(accountFrom);
        transaction.setAccountTo(accountTo);
        transactionRepository.save(transaction);

        accountFrom.setBalance(accountFrom.getBalance() - transactionRequestDto.getAmount());
        accountRepository.save(accountFrom);

        accountTo.setBalance(accountTo.getBalance() + transactionRequestDto.getAmount());
        accountRepository.save(accountTo);

        transactionRequestDto.setTransactionType(TransactionType.TRANSFER.toString());

        return transactionRequestDto;
    }


    @Transactional(readOnly = true)
    public List<TransactionRequestDto> getTransactions(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account does not exist");
        }

        List<Transaction> transactions = transactionRepository.getTransactionsByAccount_AccountNumber(accountNumber);
        return transactions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private TransactionRequestDto mapToDto(Transaction transaction) {
        TransactionRequestDto dto = new TransactionRequestDto();
        dto.setAmount(transaction.getAmount());
        dto.setTransactionType(transaction.getTransactionType().toString());
        dto.setAccountNumber(transaction.getAccount().getAccountNumber());
        dto.setAccountNumberTo(transaction.getAccountTo() != null ? transaction.getAccountTo().getAccountNumber() : null);
        return dto;
    }
}