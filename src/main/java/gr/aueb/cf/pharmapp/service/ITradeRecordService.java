package gr.aueb.cf.pharmapp.service;

import gr.aueb.cf.pharmapp.dto.*;
import gr.aueb.cf.pharmapp.exceptions.*;
import gr.aueb.cf.pharmapp.model.TradeRecord;

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
    TradeRecordReadOnlyDTO recordTrade(TradeRecordInsertDTO dto, Long recorderUserId)
            throws TradeRecordDAOException, PharmacyNotFoundException,
            UserAnauthorizedException, UserDAOException,UserNotFoundException
            , PharmacyDAOException;
    Integer getTradeCountBetweenPharmacies(Long pharmacy1Id,
                                           Long pharmacy2Id) throws TradeRecordDAOException;
    List<RecentTradeDTO> getRecentTradesBetweenPharmacies(Long pharmacy1Id, Long pharmacy2Id, int limit)
            throws TradeRecordDAOException;





}
