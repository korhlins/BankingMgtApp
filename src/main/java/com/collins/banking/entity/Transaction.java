package com.collins.banking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "Account_Id")
    private Long accountId;
    @Column(name = "Amount")
    private double amount;
    @Column(name = "Transaction_Type")
    private String transactionType;
    @Column(name = "Time_Stamp")
    private LocalDateTime timeStamp;
}
