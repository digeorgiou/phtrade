package gr.aueb.cf.pharmapp.dto;

public class PharmacyUpdateDTO extends BasePharmacyDTO{

    private Long id;

    public PharmacyUpdateDTO() {
    }

    public PharmacyUpdateDTO(String name, Long id) {
        super(name);
        this.id = id;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
