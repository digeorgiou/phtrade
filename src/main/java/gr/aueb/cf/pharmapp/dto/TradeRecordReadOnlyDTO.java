package gr.aueb.cf.pharmapp.dto;

import java.time.LocalDateTime;

public class TradeRecordReadOnlyDTO extends BaseTradeRecordDTO{

    private Long id;
    private String giverName;
    private String receiverName;
    private String recorderName;
    private LocalDateTime transactionDate;

    public TradeRecordReadOnlyDTO() {
    }

    public TradeRecordReadOnlyDTO(String description, Double amount, Long id,
                                  String giverName, String receiverName, String recorderName, LocalDateTime transactionDate) {
        super(description, amount);
        this.id = id;
        this.giverName = giverName;
        this.receiverName = receiverName;
        this.recorderName = recorderName;
        this.transactionDate = transactionDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGiverName() {
        return giverName;
    }

    public void setGiverName(String giverName) {
        this.giverName = giverName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getRecorderName() {
        return recorderName;
    }

    public void setRecorderName(String recorderName) {
        this.recorderName = recorderName;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}
