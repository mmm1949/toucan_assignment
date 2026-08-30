package com.example.transactionstarter.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.transactionstarter.dto.CreateTransactionRequest;
import com.example.transactionstarter.dto.UpdateTransactionStatusRequest;
import com.example.transactionstarter.entity.TransactionStatus;
import com.example.transactionstarter.entity.TransactionType;
import com.example.transactionstarter.repository.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
    }

    @Test
    void shouldCreateTransactionSuccessfully() throws Exception {
        CreateTransactionRequest request = new CreateTransactionRequest(
                "TX-1001",
                "CUST-001",
                new BigDecimal("120.50"),
                "USD",
                TransactionType.CREDIT
        );

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId").value("TX-1001"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void shouldReturnNotFoundForMissingTransaction() throws Exception {
        mockMvc.perform(get("/api/transactions/TX-DOES-NOT-EXIST"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRejectDuplicateTransactionId() throws Exception {
        CreateTransactionRequest request = new CreateTransactionRequest(
                "TX-2001",
                "CUST-002",
                new BigDecimal("25.00"),
                "EUR",
                TransactionType.DEBIT
        );

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldGetTransactionsForCustomer() throws Exception {
        CreateTransactionRequest first = new CreateTransactionRequest(
                "TX-3001",
                "CUST-003",
                new BigDecimal("10.00"),
                "USD",
                TransactionType.DEBIT
        );

        CreateTransactionRequest second = new CreateTransactionRequest(
                "TX-3002",
                "CUST-003",
                new BigDecimal("20.00"),
                "USD",
                TransactionType.CREDIT
        );

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(first)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(second)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/customers/CUST-003/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value("CUST-003"))
                .andExpect(jsonPath("$[1].customerId").value("CUST-003"));
    }

    @Test
    void shouldUpdateStatusSuccessfully() throws Exception {
        CreateTransactionRequest request = new CreateTransactionRequest(
                "TX-4001",
                "CUST-004",
                new BigDecimal("200.00"),
                "USD",
                TransactionType.CREDIT
        );

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        UpdateTransactionStatusRequest update = new UpdateTransactionStatusRequest(TransactionStatus.COMPLETED);

        mockMvc.perform(patch("/api/transactions/TX-4001/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void shouldRejectInvalidInput() throws Exception {
        CreateTransactionRequest request = new CreateTransactionRequest(
                "",
                "",
                new BigDecimal("0"),
                "usd",
                null
        );

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
