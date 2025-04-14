package gr.aueb.cf.pharmapp.dto;

import java.time.LocalDateTime;

public class TradeRecordInsertDTO extends BaseTradeRecordDTO{

    private Long giverPharmacyId;
    private Long receiverPharmacyId;
    private Long recorderPharmacyId;

    public TradeRecordInsertDTO() {
    }

    public TradeRecordInsertDTO(String description, Double amount, LocalDateTime transactionDate,
                                Long giverPharmacyId, Long receiverPharmacyId, Long recorderPharmacyId) {

        super(description, amount);
        this.giverPharmacyId = giverPharmacyId;
        this.receiverPharmacyId = receiverPharmacyId;
        this.recorderPharmacyId = recorderPharmacyId;

    }
}
