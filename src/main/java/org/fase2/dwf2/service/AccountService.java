package org.fase2.dwf2.service;

import org.fase2.dwf2.dto.Account.AccountRequestDto;
import org.fase2.dwf2.entities.Account;
import org.fase2.dwf2.repository.IAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final IAccountRepository accountRepository;
    private final UserService userService;

    @Autowired
    public AccountService(IAccountRepository accountRepository, UserService userService) {
        this.accountRepository = accountRepository;
        this.userService = userService;
    }


    @Transactional(readOnly = true)
    public List<AccountRequestDto> findByUserEmail(String email) {
        return accountRepository.findByUserEmail(email).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AccountRequestDto createAccount(AccountRequestDto accountRequestDto) {
        System.out.println("AccountrequestDto: " + accountRequestDto);
        if (!userService.existsByDui(accountRequestDto.getDui())) {
            throw new IllegalArgumentException("User with this DUI does not exist");
        }
        Account account = new Account();
        account.setUser(userService.getUserByEmail(accountRequestDto.getUserEmail()));
        account.setBalance(accountRequestDto.getBalance());
        return convertToDto(accountRepository.save(account));
    }

    @Transactional
    public AccountRequestDto depositFunds(String accountNumber, double amount) {
        // Fetch the account by its number
        Account account = accountRepository.findByAccountNumber(accountNumber);

        // Update the account balance
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }

        account.setBalance(account.getBalance() + amount);
        Account updatedAccount = accountRepository.save(account);

        // Convert to DTO for response
        return convertToDto(updatedAccount);
    }

    @Transactional
    public AccountRequestDto withdrawFunds(String accountNumber, double amount) {
        // Fetch the account by its number
        Account account = accountRepository.findByAccountNumber(accountNumber);

        // Validate that the withdrawal amount is greater than zero and that the account has sufficient funds
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
        }
        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds for withdrawal");
        }

        // Deduct the amount from the account's balance
        account.setBalance(account.getBalance() - amount);
        Account updatedAccount = accountRepository.save(account);

        // Convert to DTO for response
        return convertToDto(updatedAccount);
    }



    private AccountRequestDto convertToDto(Account account) {
        AccountRequestDto accountRequestDto = new AccountRequestDto();
        accountRequestDto.setAccountNumber(account.getAccountNumber());
        accountRequestDto.setUserEmail(account.getUser().getEmail());
        accountRequestDto.setBalance(account.getBalance());
        accountRequestDto.setDui(account.getUser().getDui());
        return accountRequestDto;
    }

}
