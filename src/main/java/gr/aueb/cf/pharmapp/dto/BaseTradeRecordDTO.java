package gr.aueb.cf.pharmapp.dto;

import gr.aueb.cf.pharmapp.model.Pharmacy;

import java.time.LocalDateTime;

public class BaseTradeRecordDTO {

    private String description;
    private Double amount;

    public BaseTradeRecordDTO() {
    }

    public BaseTradeRecordDTO(String description, Double amount) {
        this.description = description;
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

}
