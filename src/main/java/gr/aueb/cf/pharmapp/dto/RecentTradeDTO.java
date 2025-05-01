package gr.aueb.cf.pharmapp.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RecentTradeDTO {

    private LocalDateTime date = LocalDateTime.now();
    private String description = "No description";
    private double amount = 0.0;
    private boolean outgoing = false;
    private String formattedDate;

    public RecentTradeDTO() {
    }

    public RecentTradeDTO(LocalDateTime date, String description,
                          Double amount, boolean outgoing) {
        this.date = date != null ? date : LocalDateTime.now();
        this.description = description != null ? description : "No description";
        this.amount = amount != null ? amount : 0.0;
        this.outgoing = outgoing;
        this.formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM HH:mm"));
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
        this.formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM HH:mm"));
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

    public boolean isOutgoing() {
        return outgoing;
    }

    public void setOutgoing(boolean outgoing) {
        this.outgoing = outgoing;
    }
}
