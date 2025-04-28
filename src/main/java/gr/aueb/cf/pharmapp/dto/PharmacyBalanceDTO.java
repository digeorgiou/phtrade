package gr.aueb.cf.pharmapp.dto;

public class PharmacyBalanceDTO {

    private String contactName;
    private String pharmacyName;
    private Long pharmacyId;
    private double amount;

    public PharmacyBalanceDTO() {
    }

    public PharmacyBalanceDTO(String contactName, String pharmacyName, Long pharmacyId, double amount) {
        this.contactName = contactName;
        this.pharmacyName = pharmacyName;
        this.pharmacyId = pharmacyId;
        this.amount = amount;
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

    public Long getPharmacyId() {
        return pharmacyId;
    }

    public void setPharmacyId(Long pharmacyId) {
        this.pharmacyId = pharmacyId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
