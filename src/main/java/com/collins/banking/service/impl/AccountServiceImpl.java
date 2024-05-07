package com.collins.banking.service.impl;

import com.collins.banking.Dto.AccountDto;
import com.collins.banking.entity.Account;
import com.collins.banking.exceptions.AccountException;
import com.collins.banking.mapper.AccountMapper;
import com.collins.banking.repository.AccountRepository;
import com.collins.banking.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    AccountRepository accountRepository;

//    AccountServiceImpl(AccountRepository accountRepository){
//        this.accountRepository = accountRepository;
//    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {

//      Can also use BeanUtils to copy the value of the members of one object to another.
//         BeanUtils.copyProperties(accountDto, account);
//         BeanUtils.copyProperties(savedAccount, accountDto);

        Account account = AccountMapper.mapToAccount(accountDto);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto getAccountById(Long id){
        Account account = accountRepository
                .findById(id)
                .orElseThrow(()-> new AccountException("Account does not exit"));
        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public AccountDto deposit(Long id, double amount){
        AccountDto accountDto = getAccountById(id);
        double total = accountDto.balance() + amount;
        AccountDto accountDto1 = new AccountDto(accountDto.id(), accountDto.accountName(), total);
        Account account = AccountMapper.mapToAccount(accountDto1);
        return AccountMapper.mapToAccountDto(accountRepository.save(account));
    }

    @Override
    public AccountDto withdraw(Long id, double amount) {
        AccountDto accountDto = getAccountById(id);

        if(accountDto.balance() < amount){
            throw new AccountException("Insufficient amount");
        };
        double total = accountDto.balance() - amount;
        AccountDto accountDto1 = new AccountDto(accountDto.id(), accountDto.accountName(), total);
        Account account = AccountMapper.mapToAccount(accountDto1);
        return AccountMapper.mapToAccountDto(accountRepository.save(account));
    }

    @Override
    public List<AccountDto> getAllAccounts(){
        List<Account> accounts= accountRepository.findAll();
        return accounts.stream().map(AccountMapper::mapToAccountDto).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id){
        getAccountById(id);
        accountRepository.deleteById(id);
    }
}
