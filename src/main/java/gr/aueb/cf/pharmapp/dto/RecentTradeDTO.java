package gr.aueb.cf.pharmapp.dto;

import java.time.LocalDateTime;

public class RecentTradeDTO {

    private LocalDateTime transactionDate;
    private String description;
    private double amount;
    private String counterpartyName;
    private boolean isOutgoing;

    public RecentTradeDTO() {
    }

    public RecentTradeDTO(LocalDateTime transactionDate, String description, double amount, String counterpartyName, boolean isOutgoing) {
        this.transactionDate = transactionDate;
        this.description = description;
        this.amount = amount;
        this.counterpartyName = counterpartyName;
        this.isOutgoing = isOutgoing;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCounterpartyName() {
        return counterpartyName;
    }

    public void setCounterpartyName(String counterpartyName) {
        this.counterpartyName = counterpartyName;
    }

    public boolean isOutgoing() {
        return isOutgoing;
    }

    public void setOutgoing(boolean outgoing) {
        isOutgoing = outgoing;
    }
}
