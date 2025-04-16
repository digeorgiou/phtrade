package gr.aueb.cf.pharmapp.model;


import gr.aueb.cf.pharmapp.core.RoleType;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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


    public User() {
    }

    public User(Long id, String username, String password, String email, RoleType roleType,
                Set<Pharmacy> pharmacies, Set<TradeRecord> recordsRecorder, Set<PharmacyContact> contacts) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roleType = roleType;
        this.pharmacies = pharmacies;
        this.recordsRecorder = recordsRecorder;
        this.contacts = contacts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Set<Pharmacy> getPharmacies() {
        return pharmacies;
    }

    public void setPharmacies(Set<Pharmacy> pharmacies) {
        this.pharmacies = pharmacies;
    }

    public Set<TradeRecord> getRecordsRecorder() {
        return recordsRecorder;
    }

    public void setRecordsRecorder(Set<TradeRecord> recordsRecorder) {
        this.recordsRecorder = recordsRecorder;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<PharmacyContact> getContacts() {
        return contacts;
    }

    public void setContacts(Set<PharmacyContact> contacts) {
        this.contacts = contacts;
    }

    public void addRecordRecorder(TradeRecord tradeRecord){
        if (recordsRecorder == null) recordsRecorder = new HashSet<>();
        recordsRecorder.add(tradeRecord);
        tradeRecord.setRecorder(this);
    }

    public void removeRecordRecorder(TradeRecord tradeRecord){
        recordsRecorder.remove(tradeRecord);
        tradeRecord.setRecorder(null);
    }

    public void addPharmacy(Pharmacy pharmacy) {
        pharmacies.add(pharmacy);
        pharmacy.setUser(this);
    }

    public void removePharmacy(Pharmacy pharmacy) {
        pharmacies.remove(pharmacy);
        pharmacy.setUser(null);
    }
}
