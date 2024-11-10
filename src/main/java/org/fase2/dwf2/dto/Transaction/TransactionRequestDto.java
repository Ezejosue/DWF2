package org.fase2.dwf2.dto.Transaction;

import lombok.Data;

@Data
public class TransactionRequestDto {
    private Double amount;
    private String transactionType;
    private String accountNumber;
    private String AccountNumberTo;
}
