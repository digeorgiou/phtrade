package gr.aueb.cf.pharmapp.mapper;

import gr.aueb.cf.pharmapp.core.RoleType;
import gr.aueb.cf.pharmapp.dto.*;
import gr.aueb.cf.pharmapp.model.Pharmacy;
import gr.aueb.cf.pharmapp.model.TradeRecord;
import gr.aueb.cf.pharmapp.model.User;
import gr.aueb.cf.pharmapp.security.SecurityUtil;

import java.time.LocalDateTime;
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

    public static UserReadOnlyDTO mapToReadOnlyDTO(User user) {
        return new UserReadOnlyDTO(user.getId(), user.getUsername(), user.getPassword());
    }

    public static User mapToUser(UserInsertDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(SecurityUtil.hashPassword(dto.getPassword()));
        user.setRoleType(RoleType.valueOf(dto.getRole()));
        return user;

    }

    public static TradeRecord mapTradeRecordInsertToModel(TradeRecordInsertDTO dto,
                                                          Pharmacy giver,
                                                          Pharmacy receiver,
                                                          Pharmacy recorder) {
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
                                                          Pharmacy recorder) {
        existing.setDescription(dto.getDescription());
        existing.setAmount(dto.getAmount());
        existing.setGiver(giver);
        existing.setReceiver(receiver);
        existing.setRecorder(recorder);
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
                record.getRecorder() != null ? record.getRecorder().getName() : null,
                record.getTransactionDate()
        ));
    }




}
