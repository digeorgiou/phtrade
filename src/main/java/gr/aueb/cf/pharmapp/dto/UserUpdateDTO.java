package gr.aueb.cf.pharmapp.dto;

public class UserUpdateDTO extends BaseUserDTO{
    Long id;

    public UserUpdateDTO() {
    }

    public UserUpdateDTO(String username, String password,
                         String confirmedPassword, Long id) {
        super(username, password, confirmedPassword);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
