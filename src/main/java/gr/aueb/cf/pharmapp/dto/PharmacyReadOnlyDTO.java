package gr.aueb.cf.pharmapp.dto;

import java.time.LocalDateTime;

public class PharmacyReadOnlyDTO extends BasePharmacyDTO{

    private Long id;
    private LocalDateTime createdAt;
    private String username;

    public PharmacyReadOnlyDTO() {
    }

    public PharmacyReadOnlyDTO(String name, Long id, LocalDateTime createdAt,
     String username                          ) {
        super(name);
        this.id = id;
        this.createdAt = createdAt;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
