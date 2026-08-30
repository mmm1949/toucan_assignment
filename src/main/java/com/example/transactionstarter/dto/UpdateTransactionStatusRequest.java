package com.example.transactionstarter.dto;

import com.example.transactionstarter.entity.TransactionStatus;

import jakarta.validation.constraints.NotNull;

public class UpdateTransactionStatusRequest {

    @NotNull(message = "status is required")
    private TransactionStatus status;

    public UpdateTransactionStatusRequest() {
    }

    public UpdateTransactionStatusRequest(TransactionStatus status) {
        this.status = status;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
}
