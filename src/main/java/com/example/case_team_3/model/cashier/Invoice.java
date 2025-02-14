package com.example.case_team_3.model.cashier;

import java.time.LocalDate;
import java.util.List;

public class Invoice {
    private Integer invoiceId;
    private String customerName;
    private LocalDate invoiceDate;
    private List<TransactionHistory> transactions;
    private float totalAmount;

    public Invoice(Integer invoiceId,  String customerName, Integer roomId, LocalDate invoiceDate, List<TransactionHistory> transactions, float totalAmount) {
        this.invoiceId = invoiceId;
        this.customerName = customerName;
        this.invoiceDate = invoiceDate;
        this.transactions = transactions;
        this.totalAmount = totalAmount;
    }

    public Integer getInvoiceId() { return invoiceId; }
    public void setInvoiceId(Integer invoiceId) { this.invoiceId = invoiceId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public LocalDate getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(LocalDate invoiceDate) { this.invoiceDate = invoiceDate; }

    public List<TransactionHistory> getTransactions() { return transactions; }
    public void setTransactions(List<TransactionHistory> transactions) { this.transactions = transactions; }

    public float getTotalAmount() { return totalAmount; }
    public void setTotalAmount(float totalAmount) { this.totalAmount = totalAmount; }
}
