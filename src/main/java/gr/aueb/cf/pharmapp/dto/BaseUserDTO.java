package gr.aueb.cf.pharmapp.dto;

public abstract class BaseUserDTO {
    private String username;
    private String password;
    private String confirmedPassword;
    private String email;
    private boolean isTermsAccepted;

    public BaseUserDTO() {
    }

    public BaseUserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public BaseUserDTO(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public BaseUserDTO(String username, String password, String confirmedPassword, String email) {
        this.username = username;
        this.password = password;
        this.confirmedPassword = confirmedPassword;
        this.email = email;
    }

    public BaseUserDTO(String username, String password, String confirmedPassword, String email, boolean isTermsAccepted) {
        this.username = username;
        this.password = password;
        this.confirmedPassword = confirmedPassword;
        this.email = email;
        this.isTermsAccepted = isTermsAccepted;
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

    public String getConfirmedPassword() {
        return confirmedPassword;
    }

    public void setConfirmedPassword(String confirmedPassword) {
        this.confirmedPassword = confirmedPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isTermsAccepted() {
        return isTermsAccepted;
    }

    public void setTermsAccepted(boolean termsAccepted) {
        isTermsAccepted = termsAccepted;
    }
}
