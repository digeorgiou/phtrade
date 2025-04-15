package gr.aueb.cf.pharmapp.dto;

import java.time.LocalDateTime;

public class TradeRecordInsertDTO extends BaseTradeRecordDTO{

    private Long giverPharmacyId;
    private Long receiverPharmacyId;
    private Long recorderUserId;

    public TradeRecordInsertDTO() {
    }

    public TradeRecordInsertDTO(String description, Double amount, LocalDateTime transactionDate,
                                Long giverPharmacyId, Long receiverPharmacyId
            , Long recorderUserId) {

        super(description, amount);
        this.giverPharmacyId = giverPharmacyId;
        this.receiverPharmacyId = receiverPharmacyId;
        this.recorderUserId = recorderUserId;

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

    public Long getRecorderUserId() {
        return recorderUserId;
    }

    public void setRecorderUserId(Long recorderUserId) {
        this.recorderUserId = recorderUserId;
    }
}
