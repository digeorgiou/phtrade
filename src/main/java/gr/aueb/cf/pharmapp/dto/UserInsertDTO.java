package gr.aueb.cf.pharmapp.dto;

public class UserInsertDTO extends BaseUserDTO{

    private String role;
    private Long pharmacyId;

    public UserInsertDTO() {
    }

    public UserInsertDTO(String username, String password, String confirmedPassword,
                         String email, String role) {
        super(username, password, confirmedPassword, email);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getPharmacyId() {
        return pharmacyId;
    }

    public void setPharmacyId(Long pharmacyId) {
        this.pharmacyId = pharmacyId;
    }
}
