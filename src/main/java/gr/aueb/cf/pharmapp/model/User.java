package gr.aueb.cf.pharmapp.model;


import gr.aueb.cf.pharmapp.core.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role" , updatable = false)
    private RoleType roleType;

    @OneToMany(mappedBy = "user")
    private Set<Pharmacy> pharmacies = new HashSet<>();

    @OneToMany(mappedBy = "recorder")
    private Set<TradeRecord> recordsRecorder;

    @OneToMany(mappedBy = "user")
    private Set<PharmacyContact> contacts = new HashSet<>();

    public void addRecordRecorder(TradeRecord tradeRecord){
        if (recordsRecorder == null) recordsRecorder = new HashSet<>();
        recordsRecorder.add(tradeRecord);
        tradeRecord.setRecorder(this);
    }

    public void removeRecordRecorder(TradeRecord tradeRecord){
        if (recordsRecorder == null) return;
        recordsRecorder.remove(tradeRecord);
        tradeRecord.setRecorder(null);
    }

    public void addPharmacy(Pharmacy pharmacy) {
        if(pharmacies == null) pharmacies = new HashSet<>();
        pharmacies.add(pharmacy);
        pharmacy.setUser(this);
    }

    public void removePharmacy(Pharmacy pharmacy) {
        if(pharmacies == null) return;
        pharmacies.remove(pharmacy);
        pharmacy.setUser(null);
    }

    public void addContact(PharmacyContact contact){
        if (contacts == null) contacts = new HashSet<>();
        contacts.add(contact);
        contact.setUser(this);
    }

    public void removeContact(PharmacyContact contact){
        if (contacts == null) return;
        contacts.remove(contact);
        contact.setUser(null);
    }
}
