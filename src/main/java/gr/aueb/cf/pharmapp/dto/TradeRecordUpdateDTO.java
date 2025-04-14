package gr.aueb.cf.pharmapp.dto;

import java.time.LocalDateTime;

public class TradeRecordUpdateDTO extends BaseTradeRecordDTO{

    private Long id;
    private Long giverPharmacyId;
    private Long receiverPharmacyId;
    private Long recorderPharmacyId;

    public TradeRecordUpdateDTO() {
    }

    public TradeRecordUpdateDTO(String description, Double amount, LocalDateTime transactionDate,
                                  Long id, Long giverPharmacyId, Long receiverPharmacyId, Long recorderPharmacyId) {
        super(description, amount);
        this.id = id;
        this.giverPharmacyId = giverPharmacyId;
        this.receiverPharmacyId = receiverPharmacyId;
        this.recorderPharmacyId = recorderPharmacyId;
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

    public Long getRecorderPharmacyId() {
        return recorderPharmacyId;
    }

    public void setRecorderPharmacyId(Long recorderPharmacyId) {
        this.recorderPharmacyId = recorderPharmacyId;
    }
}
