package com.collins.banking.Dto;

public record TransferFundsDto(Long fromAccountId,
                               Long toAccountId,
                               double transferAmount) {
}
