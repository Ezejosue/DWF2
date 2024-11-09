package org.fase2.dwf2.service;

import org.fase2.dwf2.dto.Account.AccountRequestDto;
import org.fase2.dwf2.entities.Account;
import org.fase2.dwf2.repository.IAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    //Get account by user email
    public List<AccountRequestDto> findByUserEmail(String email) {
        return accountRepository.findByUserEmail(email).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    //create account
    public AccountRequestDto createAccount(AccountRequestDto accountRequestDto) {
        if (!userService.existsByDui(accountRequestDto.getDui())) {
            throw new IllegalArgumentException("User with this DUI does not exist");
        }
        Account account = new Account();
        account.setUser(userService.getUserByEmail(accountRequestDto.getUserEmail()));
        account.setBalance(accountRequestDto.getBalance());
        return convertToDto(accountRepository.save(account));
    }

    //Convert Account to AccountRequestDto
    private AccountRequestDto convertToDto(Account account) {
        AccountRequestDto accountRequestDto = new AccountRequestDto();
        accountRequestDto.setAccountNumber(account.getAccountNumber());
        accountRequestDto.setUserEmail(account.getUser().getEmail());
        accountRequestDto.setBalance(account.getBalance());
        accountRequestDto.setDui(account.getUser().getDui());
        return accountRequestDto;
    }


}
