package gr.aueb.cf.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "trade_records")
public class TradeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "amount", nullable = false)
    private Double amount;



    private Pharmacy giver;


    private Pharmacy receiver;


    private Pharmacy recorder;

    private LocalDateTime transactionDate;

    public TradeRecord() {
    }

    public TradeRecord(Long id, String description, Double amount, Pharmacy giver,
                       Pharmacy receiver, Pharmacy recorder, LocalDateTime transactionDate) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.giver = giver;
        this.receiver = receiver;
        this.recorder = recorder;
        this.transactionDate = transactionDate;
    }
}
