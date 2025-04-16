package gr.aueb.cf.pharmapp.dto;

public class BasePharmacyContactDTO {
    private Long id;
    private Long userId;
    private Long pharmacyId;
    private String contactName;  // User-defined name
    private String pharmacyName; // Actual pharmacy name

    // Constructors, getters, and setters
    public BasePharmacyContactDTO() {}

    public BasePharmacyContactDTO(Long id, Long userId, Long pharmacyId, String contactName, String pharmacyName) {
        this.id = id;
        this.userId = userId;
        this.pharmacyId = pharmacyId;
        this.contactName = contactName;
        this.pharmacyName = pharmacyName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPharmacyId() {
        return pharmacyId;
    }

    public void setPharmacyId(Long pharmacyId) {
        this.pharmacyId = pharmacyId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPharmacyName() {
        return pharmacyName;
    }

    public void setPharmacyName(String pharmacyName) {
        this.pharmacyName = pharmacyName;
    }

}
