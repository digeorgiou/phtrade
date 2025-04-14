package gr.aueb.cf.pharmapp.model;


import gr.aueb.cf.pharmapp.core.RoleType;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToOne
    @JoinColumn(name = "pharmacy_id", unique = true)
    private Pharmacy pharmacy;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;


    public User() {
    }



    public User(Long id, String username, String password, Pharmacy pharmacy, RoleType roleType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.pharmacy = pharmacy;
        this.roleType = roleType;
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

    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }
}
