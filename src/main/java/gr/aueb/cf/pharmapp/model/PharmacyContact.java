package gr.aueb.cf.pharmapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pharmacy_contacts")
public class PharmacyContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "pharmacy_id", nullable = false)
    private Pharmacy pharmacy;

    @Column(nullable = false)
    private String contactName; // User-defined name for this contact

    public PharmacyContact() {
    }

    public PharmacyContact(Long id, User user, Pharmacy pharmacy, String contactName) {
        this.id = id;
        this.user = user;
        this.pharmacy = pharmacy;
        this.contactName = contactName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
}
