package com.collins.banking.service;

import com.collins.banking.Dto.AccountDto;
import com.collins.banking.Dto.TransferFundsDto;
import com.collins.banking.entity.Account;

import java.util.List;

public interface AccountService {

    AccountDto createAccount(AccountDto accountDto);

    AccountDto getAccountById(Long id);

    AccountDto deposit(Long id, double amount);

    AccountDto withdraw(Long id, double amount);

    List<AccountDto> getAllAccounts();

    void deleteById(Long id);

    void transferFunds(TransferFundsDto transferFundsDto);
}