package gr.aueb.cf.pharmapp.dto;

import java.time.LocalDateTime;

public abstract class BasePharmacyDTO {

    private String name;

    public BasePharmacyDTO() {
    }

    public BasePharmacyDTO(String name) {
        this.name = name;
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
