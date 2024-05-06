package com.collins.banking.Dto;

//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//
//@AllArgsConstructor
//@Data
//public class AccountDto {
//    private Long id;
//    private String accountName;
//    private  double balance;
//}

public record AccountDto( Long id,
                          String accountName,
                          double balance) {
}