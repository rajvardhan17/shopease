package com.shopease.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class Payment implements Serializable {
    private int paymentId;
    private int orderId;
    private String paymentMethod; // e.g., UPI, CREDIT_CARD, DEBIT_CARD
    private String paymentStatus; // e.g., PENDING, SUCCESS, FAILED
    private String transactionId;
    private BigDecimal amount;
    private Timestamp paymentDate;

    public Payment() {}

    // Getters & Setters
    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public Timestamp getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Timestamp paymentDate) { this.paymentDate = paymentDate; }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", orderId=" + orderId +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", amount=" + amount +
                ", paymentDate=" + paymentDate +
                '}';
    }
}
