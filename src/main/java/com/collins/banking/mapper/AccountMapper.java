package com.collins.banking.mapper;

import com.collins.banking.Dto.AccountDto;
import com.collins.banking.entity.Account;

public class AccountMapper {

    public static Account mapToAccount(AccountDto accountDto){
        return new Account(
                accountDto.id(),
                accountDto.accountName(),
                accountDto.balance()
        );
    }

    public static AccountDto mapToAccountDto(Account account){
        return new AccountDto(
                account.getId(),
                account.getAccountName(),
                account.getBalance()
        );
    }

}
