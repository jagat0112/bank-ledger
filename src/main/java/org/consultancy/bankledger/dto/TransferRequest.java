package org.consultancy.bankledger.dto;

import lombok.Data;

@Data
public class TransferRequest {
    private Long toAccountId;
    private Long fromAccountId;
    private Double amount;
}
