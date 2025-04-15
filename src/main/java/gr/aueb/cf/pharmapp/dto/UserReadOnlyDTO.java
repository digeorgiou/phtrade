package gr.aueb.cf.pharmapp.dto;

public class UserReadOnlyDTO extends BaseUserDTO {

    private Long id;
    private String role;

    public UserReadOnlyDTO() {}

    public UserReadOnlyDTO(Long id, String username, String password,
                           String role) {
        super(username, password);
        this.id = id;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
