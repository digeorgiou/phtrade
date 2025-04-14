package gr.aueb.cf.pharmapp.dto;

public class UserReadOnlyDTO extends BaseUserDTO {

    private Long id;

    public UserReadOnlyDTO() {}

    public UserReadOnlyDTO(Long id, String username, String password) {
        super(username, password);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
