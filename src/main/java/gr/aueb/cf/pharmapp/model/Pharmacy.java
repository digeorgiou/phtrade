package gr.aueb.cf.pharmapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@Builder
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

    public void addRecordGiver(TradeRecord tradeRecord){
        if (recordsGiver == null) recordsGiver = new HashSet<>();
        recordsGiver.add(tradeRecord);
        tradeRecord.setGiver(this);
    }

    public void removeRecordGiver(TradeRecord tradeRecord){
        if (recordsGiver == null) return;
        recordsGiver.remove(tradeRecord);
        tradeRecord.setGiver(null);
    }

    public void addRecordReceiver(TradeRecord tradeRecord){
        if (recordsReceiver == null) recordsReceiver = new HashSet<>();
        recordsReceiver.add(tradeRecord);
        tradeRecord.setReceiver(this);
    }

    public void removeRecordReceiver(TradeRecord tradeRecord){
        if (recordsReceiver == null) return;
        recordsReceiver.remove(tradeRecord);
        tradeRecord.setReceiver(null);
    }

    public void addContactReference(PharmacyContact contact){
        if (contactReferences == null) contactReferences = new HashSet<>();
        contactReferences.add(contact);
        contact.setPharmacy(this);
    }

    public void removeContactReference(PharmacyContact contact){
        if (contactReferences == null) return;
        contactReferences.remove(contact);
        contact.setPharmacy(null);
    }
}
