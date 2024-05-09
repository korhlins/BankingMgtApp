package com.collins.banking.service.impl;

import com.collins.banking.Dto.AccountDto;
import com.collins.banking.Dto.TransactionDto;
import com.collins.banking.Dto.TransferFundsDto;
import com.collins.banking.commons.transactionType;
import com.collins.banking.entity.Account;
import com.collins.banking.entity.Transaction;
import com.collins.banking.exceptions.AccountException;
import com.collins.banking.mapper.AccountMapper;
import com.collins.banking.repository.AccountRepository;
import com.collins.banking.repository.TransactionRepository;
import com.collins.banking.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    AccountRepository accountRepository;

    TransactionRepository transactionRepository;

    private TransactionDto convertEntityToDto(Transaction transaction){
        return new TransactionDto(
                transaction.getId(),
                transaction.getAccountId(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getTimeStamp()
        );
    }

//    AccountServiceImpl(AccountRepository accountRepository){
//        this.accountRepository = accountRepository;
//    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {

//      Can also use BeanUtils to copy the value of the members of one object to another, instead of creating a mapper
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
        Account accountUpdate = accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccountId(id);
        transaction.setAmount(amount);
        transaction.setTransactionType(transactionType.DEPOSIT.toString());
        transaction.setTimeStamp(LocalDateTime.now());

        transactionRepository.save(transaction);
        return AccountMapper.mapToAccountDto(accountUpdate);
    }

    @Override
    public AccountDto withdraw(Long id, double amount) {
        AccountDto accountDto = getAccountById(id);

        if(accountDto.balance() < amount){
            throw new AccountException("Insufficient funds");
        };
        double total = accountDto.balance() - amount;
        AccountDto accountDto1 = new AccountDto(accountDto.id(), accountDto.accountName(), total);
        Account account = AccountMapper.mapToAccount(accountDto1);
        Account accountUpdate = accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccountId(id);
        transaction.setAmount(amount);
        transaction.setTransactionType(transactionType.WITHDRAW.toString());
        transaction.setTimeStamp(LocalDateTime.now());

        transactionRepository.save(transaction);

        return AccountMapper.mapToAccountDto(accountUpdate);
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

    @Override
    public void transferFunds(TransferFundsDto transferFundsDto){

       AccountDto fromAccountDto = getAccountById(transferFundsDto.fromAccountId());
       AccountDto toAccountDto = getAccountById(transferFundsDto.toAccountId());

       Account fromAccount = AccountMapper.mapToAccount(fromAccountDto);
       Account toAccount = AccountMapper.mapToAccount(toAccountDto);

       if (fromAccount.getBalance() < transferFundsDto.transferAmount()) {
           throw new RuntimeException("Insufficient funds");
       }
       fromAccount.setBalance(fromAccount.getBalance() - transferFundsDto.transferAmount());
       toAccount.setBalance(toAccount.getBalance() + transferFundsDto.transferAmount());

       accountRepository.save(fromAccount);

       accountRepository.save(toAccount);

        Transaction transaction = new Transaction();
        transaction.setAccountId(fromAccount.getId());
        transaction.setAmount(transferFundsDto.transferAmount());
        transaction.setTransactionType(transactionType.TRANSFER.toString());
        transaction.setTimeStamp(LocalDateTime.now());

        transactionRepository.save(transaction);
    }

    @Override
    public List<TransactionDto> getAccountTransaction(Long accountId){
        List<Transaction> transactions = transactionRepository.findByAccountIdOrderByTimeStampDesc(accountId);
     return transactions.stream().
                map(this::convertEntityToDto).collect(Collectors.toList());
    }
}
