package gr.aueb.cf.pharmapp.dto;

import java.util.ArrayList;
import java.util.List;

public class PharmacyBalanceDTO {

    private String contactName;
    private String pharmacyName;
    private Long pharmacyId;
    private double amount;
    private List<RecentTradeDTO> recentTrades;
    private Integer tradeCount;

    public PharmacyBalanceDTO() {
    }

    public PharmacyBalanceDTO(String contactName, String pharmacyName,
                              Long pharmacyId, double amount,
                              List<RecentTradeDTO> recentTrades, Integer tradeCount) {
        this.contactName = contactName;
        this.pharmacyName = pharmacyName;
        this.pharmacyId = pharmacyId;
        this.amount = amount;
        this.recentTrades = recentTrades != null ? recentTrades : new ArrayList<>();
        this.tradeCount = tradeCount;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPharmacyName() {
        return pharmacyName;
    }

    public void setPharmacyName(String pharmacyName) {
        this.pharmacyName = pharmacyName;
    }

    public Long getPharmacyId() {
        return pharmacyId;
    }

    public void setPharmacyId(Long pharmacyId) {
        this.pharmacyId = pharmacyId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public List<RecentTradeDTO> getRecentTrades() {
        return recentTrades;
    }

    public void setRecentTrades(List<RecentTradeDTO> recentTrades) {
        this.recentTrades = recentTrades;
    }

    public Integer getTradeCount() {
        return tradeCount;
    }

    public void setTradeCount(Integer tradeCount) {
        this.tradeCount = tradeCount;
    }
}
