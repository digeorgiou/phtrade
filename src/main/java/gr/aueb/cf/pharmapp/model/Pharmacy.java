package gr.aueb.cf.pharmapp.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "pharmacies")
public class Pharmacy implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 255, unique = true, nullable = false)
    private String name;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" )
    private User user;

    @OneToMany(mappedBy = "giver")
    private Set<TradeRecord> recordsGiver;

    @OneToMany(mappedBy = "receiver")
    private Set<TradeRecord> recordsReceiver;

    @OneToMany(mappedBy = "pharmacy")
    private Set<PharmacyContact> contactReferences = new HashSet<>();


    public Pharmacy() {
        this.createdAt = LocalDateTime.now();
    }

    public Pharmacy(String name){
        this();
        this.name = name;
    }

    public Pharmacy(Long id, String name, LocalDateTime createdAt, User user,
                    Set<TradeRecord> recordsGiver, Set<TradeRecord> recordsReceiver,
                    Set<PharmacyContact> contactReferences) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.user = user;
        this.recordsGiver = recordsGiver;
        this.recordsReceiver = recordsReceiver;
        this.contactReferences = contactReferences;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    protected Set<TradeRecord> getRecordsGiver() {
        return recordsGiver;
    }

    public Set<TradeRecord> getAllRecordsGiver() {
        return Collections.unmodifiableSet(recordsGiver);
    }

    public void setRecordsGiver(Set<TradeRecord> recordsGiver) {
        this.recordsGiver = recordsGiver;
    }

    protected Set<TradeRecord> getRecordsReceiver() {
        return recordsReceiver;
    }

    public Set<TradeRecord> getAllRecordsReceiver() {
        return Collections.unmodifiableSet(recordsReceiver);
    }

    public void setRecordsReceiver(Set<TradeRecord> recordsReceiver) {
        this.recordsReceiver = recordsReceiver;
    }

    public void addRecordGiver(TradeRecord tradeRecord){
        if (recordsGiver == null) recordsGiver = new HashSet<>();
        recordsGiver.add(tradeRecord);
        tradeRecord.setGiver(this);
    }

    public void removeRecordGiver(TradeRecord tradeRecord){
        recordsGiver.remove(tradeRecord);
        tradeRecord.setGiver(null);
    }

    public void addRecordReceiver(TradeRecord tradeRecord){
        if (recordsReceiver == null) recordsReceiver = new HashSet<>();
        recordsReceiver.add(tradeRecord);
        tradeRecord.setReceiver(this);
    }

    public void removeRecordReceiver(TradeRecord tradeRecord){
        recordsReceiver.remove(tradeRecord);
        tradeRecord.setReceiver(null);
    }

    public Set<PharmacyContact> getContactReferences() {
        return contactReferences;
    }

    public void setContactReferences(Set<PharmacyContact> contactReferences) {
        this.contactReferences = contactReferences;
    }

    @Override
    public String toString() {
        return "Pharmacy{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", user=" + user +
                '}';
    }
}
