package gr.aueb.cf.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;


@Entity
@Table(name = "pharmacies")
public class Pharmacy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 255, unique = true, nullable = false)
    private String name;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "giver")
    private Set<TradeRecord> recordsGiver;

    @OneToMany(mappedBy = "receiver")
    private Set<TradeRecord> recordsReceiver;

    @OneToMany(mappedBy = "recorder")
    private Set<TradeRecord> recordsRecorder;

    public Pharmacy() {
    }

    public Pharmacy(Long id, String name, LocalDateTime createdAt, Set<TradeRecord> recordsGiver,
                    Set<TradeRecord> recordsReceiver, Set<TradeRecord> recordsRecorder) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.recordsGiver = recordsGiver;
        this.recordsReceiver = recordsReceiver;
        this.recordsRecorder = recordsRecorder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
