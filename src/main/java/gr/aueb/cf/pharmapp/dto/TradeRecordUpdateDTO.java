package gr.aueb.cf.pharmapp.dto;

import java.time.LocalDateTime;

public class TradeRecordUpdateDTO extends BaseTradeRecordDTO{

    private Long id;
    private Long giverPharmacyId;
    private Long receiverPharmacyId;
    private Long updaterUserId;

    public TradeRecordUpdateDTO() {
    }

    public TradeRecordUpdateDTO(String description, Double amount, LocalDateTime transactionDate,
                                  Long id, Long giverPharmacyId, Long receiverPharmacyId, Long updaterUserIdId) {
        super(description, amount);
        this.id = id;
        this.giverPharmacyId = giverPharmacyId;
        this.receiverPharmacyId = receiverPharmacyId;
        this.updaterUserId = updaterUserId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGiverPharmacyId() {
        return giverPharmacyId;
    }

    public void setGiverPharmacyId(Long giverPharmacyId) {
        this.giverPharmacyId = giverPharmacyId;
    }

    public Long getReceiverPharmacyId() {
        return receiverPharmacyId;
    }

    public void setReceiverPharmacyId(Long receiverPharmacyId) {
        this.receiverPharmacyId = receiverPharmacyId;
    }

    public Long getUpdaterUserId() {
        return updaterUserId;
    }

    public void setUpdaterUserId(Long updaterUserId) {
        this.updaterUserId = updaterUserId;
    }
}
