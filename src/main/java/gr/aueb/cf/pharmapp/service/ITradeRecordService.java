package gr.aueb.cf.pharmapp.service;

import gr.aueb.cf.pharmapp.dto.TradeRecordInsertDTO;
import gr.aueb.cf.pharmapp.dto.TradeRecordReadOnlyDTO;
import gr.aueb.cf.pharmapp.dto.TradeRecordUpdateDTO;
import gr.aueb.cf.pharmapp.exceptions.*;

import java.time.LocalDateTime;
import java.util.List;

public interface ITradeRecordService {

    TradeRecordReadOnlyDTO create(TradeRecordInsertDTO dto) throws TradeRecordDAOException, TradeRecordAlreadyExistsException,
            PharmacyNotFoundException, PharmacyDAOException, UserAnauthorizedException,
            UserDAOException;
    TradeRecordReadOnlyDTO update(TradeRecordUpdateDTO dto) throws TradeRecordDAOException, TradeRecordAlreadyExistsException, TradeRecordNotFoundException,
            PharmacyNotFoundException,PharmacyDAOException, UserAnauthorizedException, UserDAOException;
    void delete(Long id, Long deleterUserId, Boolean isGiverSide) throws TradeRecordDAOException,
            TradeRecordNotFoundException, UserDAOException,
            UserAnauthorizedException;
    TradeRecordReadOnlyDTO getById(Long id) throws TradeRecordDAOException,
            TradeRecordNotFoundException;
    List<TradeRecordReadOnlyDTO> getAll() throws TradeRecordDAOException;
    List<TradeRecordReadOnlyDTO> getRecentTradesForPharmacy(Long pharmacyId, int limit)
            throws PharmacyNotFoundException, PharmacyDAOException, TradeRecordDAOException;
    List<TradeRecordReadOnlyDTO> getTradesBetweenPharmacies(
            Long pharmacy1Id, Long pharmacy2Id, LocalDateTime startDate, LocalDateTime endDate)
            throws PharmacyNotFoundException, PharmacyDAOException,
            TradeRecordDAOException;
    Double calculateBalanceBetweenPharmacies(Long pharmacy1Id, Long pharmacy2Id)
            throws PharmacyNotFoundException, PharmacyDAOException, TradeRecordDAOException;




}
