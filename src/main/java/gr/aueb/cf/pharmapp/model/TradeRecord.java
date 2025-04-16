package gr.aueb.cf.pharmapp.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Table(name = "trade_records")
public class TradeRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "amount", nullable = false)
    private Double amount;

    // το lazy δηλωνει οτι θελουμε να φορτωθει μονο αν ζητηθει.
    // (.EAGER φορτωνεται παντα)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giver_id" )
    private Pharmacy giver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Pharmacy receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recorder_id")
    private User recorder;

    @Column(name = "deleted_by_giver")
    private boolean deletedByGiver = false;

    @Column(name = "deleted_by_receiver")
    private boolean deletedByReceiver = false;

    @Column(name = "last_modified_by")
    private User lastModifiedBy;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    public TradeRecord() {
    }

    public TradeRecord(Long id, String description, Double amount, Pharmacy giver, Pharmacy receiver,
                       User recorder, boolean deletedByGiver,
                       boolean deletedByReceiver, User lastModifiedBy,
                       LocalDateTime transactionDate) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.giver = giver;
        this.receiver = receiver;
        this.recorder = recorder;
        this.deletedByGiver = deletedByGiver;
        this.deletedByReceiver = deletedByReceiver;
        this.lastModifiedBy = lastModifiedBy;
        this.transactionDate = transactionDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Pharmacy getGiver() {
        return giver;
    }

    public void setGiver(Pharmacy giver) {
        this.giver = giver;
    }

    public Pharmacy getReceiver() {
        return receiver;
    }

    public void setReceiver(Pharmacy receiver) {
        this.receiver = receiver;
    }

    public User getRecorder() {
        return recorder;
    }

    public void setRecorder(User recorder) {
        this.recorder = recorder;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }


    public boolean isDeletedByGiver() {
        return deletedByGiver;
    }

    public void setDeletedByGiver(boolean deletedByGiver) {
        this.deletedByGiver = deletedByGiver;
    }

    public boolean isDeletedByReceiver() {
        return deletedByReceiver;
    }

    public void setDeletedByReceiver(boolean deletedByReceiver) {
        this.deletedByReceiver = deletedByReceiver;
    }

    public User getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(User lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public String toString() {
        return "TradeRecord{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", giver=" + giver +
                ", receiver=" + receiver +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
