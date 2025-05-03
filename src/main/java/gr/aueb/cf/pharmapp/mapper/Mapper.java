package gr.aueb.cf.pharmapp.mapper;

import gr.aueb.cf.pharmapp.core.RoleType;
import gr.aueb.cf.pharmapp.dto.*;
import gr.aueb.cf.pharmapp.model.Pharmacy;
import gr.aueb.cf.pharmapp.model.PharmacyContact;
import gr.aueb.cf.pharmapp.model.TradeRecord;
import gr.aueb.cf.pharmapp.model.User;
import gr.aueb.cf.pharmapp.security.SecurityUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class Mapper {

    private Mapper() {}

    public static Pharmacy mapPharmacyInsertToModel(PharmacyInsertDTO dto){
        return new Pharmacy(dto.getName());
    }

    public static Pharmacy mapPharmacyUpdateToModel(PharmacyUpdateDTO dto,
                                                    Pharmacy existing){
        existing.setName(dto.getName());
        return existing;
    }

    public static Optional<PharmacyReadOnlyDTO> mapPharmacyToReadOnlyDTO(Pharmacy pharmacy){
        if(pharmacy == null) return Optional.empty();
        return Optional.of(new PharmacyReadOnlyDTO(
                pharmacy.getName(),
                pharmacy.getId(),
                pharmacy.getCreatedAt(),
                pharmacy.getUser().getUsername()
        ));
    }

    public static Optional<UserReadOnlyDTO> mapToReadOnlyDTO(User user) {
        if(user == null) return Optional.empty();
        return Optional.of(new UserReadOnlyDTO(user.getId(), user.getUsername(),
                user.getPassword(), user.getRoleType().toString(), user.getEmail()));
    }

    public static User mapToUser(UserInsertDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRoleType(RoleType.valueOf(dto.getRole()));
        return user;

    }

    public static User mapUserUpdateToModel(UserUpdateDTO dto,
                                            User existingUser){
        existingUser.setPassword(dto.getPassword());
        existingUser.setEmail(dto.getEmail());
        existingUser.setUsername(dto.getUsername());
        return existingUser;
    }

    public static TradeRecord mapTradeRecordInsertToModel(TradeRecordInsertDTO dto,
                                                          Pharmacy giver,
                                                          Pharmacy receiver,
                                                          User recorder) {
        TradeRecord record = new TradeRecord();
        record.setDescription(dto.getDescription());
        record.setAmount(dto.getAmount());
        record.setGiver(giver);
        record.setReceiver(receiver);
        record.setRecorder(recorder);
        record.setTransactionDate(LocalDateTime.now());
        return record;
    }

    public static TradeRecord mapTradeRecordUpdateToModel(TradeRecordUpdateDTO dto,
                                                          TradeRecord existing,
                                                          Pharmacy giver,
                                                          Pharmacy receiver,
                                                          User updater) {
        existing.setDescription(dto.getDescription());
        existing.setAmount(dto.getAmount());
        existing.setGiver(giver);
        existing.setReceiver(receiver);
        existing.setLastModifiedBy(updater);
        return existing;
    }

    public static Optional<TradeRecordReadOnlyDTO> mapTradeRecordToReadOnlyDTO(TradeRecord record) {
        if (record == null) return Optional.empty();

        return Optional.of(new TradeRecordReadOnlyDTO(
                record.getDescription(),
                record.getAmount(),
                record.getId(),
                record.getGiver() != null ? record.getGiver().getName() : null,
                record.getReceiver() != null ? record.getReceiver().getName() : null,
                record.getRecorder() != null ? record.getRecorder().getUsername() : null,
                record.getLastModifiedBy() != null ?
                        record.getLastModifiedBy().getUsername() : null,
                record.getTransactionDate()
        ));
    }

    public static Optional<BasePharmacyContactDTO> mapContactToDTO(PharmacyContact contact){
        if (contact == null) return Optional.empty();
        return Optional.of(new BasePharmacyContactDTO(
                contact.getId(),
                contact.getUser() != null ? contact.getUser().getId() : null,
                contact.getPharmacy() != null ? contact.getPharmacy().getId() : null,
                contact.getContactName(),
                contact.getPharmacy() != null ?
                        contact.getPharmacy().getName() : null
        ));
    }

    public static PharmacyContact mapContactDTOtoModel(BasePharmacyContactDTO dto){
        PharmacyContact contact = new PharmacyContact();
        contact.setContactName(dto.getContactName());
        contact.setId(dto.getId());
        return contact;
    }

}
