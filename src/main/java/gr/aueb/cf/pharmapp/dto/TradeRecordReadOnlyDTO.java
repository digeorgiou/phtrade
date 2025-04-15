package gr.aueb.cf.pharmapp.dto;

import java.time.LocalDateTime;

public class TradeRecordReadOnlyDTO extends BaseTradeRecordDTO{

    private Long id;
    private String giverName;
    private String receiverName;
    private String recorderUsername;
    private String lastModifiedByUsername;
    private LocalDateTime transactionDate;

    public TradeRecordReadOnlyDTO() {
    }

    public TradeRecordReadOnlyDTO(String description, Double amount, Long id,
                                  String giverName, String receiverName, String recorderUsername,
                                  String lastModifiedByUsername, LocalDateTime transactionDate) {
        super(description, amount);
        this.id = id;
        this.giverName = giverName;
        this.receiverName = receiverName;
        this.recorderUsername = recorderUsername;
        this.lastModifiedByUsername = lastModifiedByUsername;
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

    public String getRecorderUsername() {
        return recorderUsername;
    }

    public void setRecorderUsername(String recorderUsername) {
        this.recorderUsername = recorderUsername;
    }

    public String getLastModifiedByUsername() {
        return lastModifiedByUsername;
    }

    public void setLastModifiedByUsername(String lastModifiedByUsername) {
        this.lastModifiedByUsername = lastModifiedByUsername;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}
