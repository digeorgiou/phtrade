package gr.aueb.cf.pharmapp.dao;

import gr.aueb.cf.pharmapp.exceptions.TradeRecordDAOException;
import gr.aueb.cf.pharmapp.model.TradeRecord;

import java.time.LocalDateTime;
import java.util.List;

public interface ITradeRecordDAO {


    public TradeRecord save(TradeRecord tradeRecord) throws TradeRecordDAOException;
    public TradeRecord update(TradeRecord tradeRecord) throws TradeRecordDAOException;
    public void delete(Long id) throws TradeRecordDAOException;
    public TradeRecord getById(Long id) throws TradeRecordDAOException;
    public List<TradeRecord> getAll() throws TradeRecordDAOException;
    public List<TradeRecord> findTradesBetweenPharmacies(Long pharmacy1Id, Long pharmacy2Id,
                                                         LocalDateTime startDate,
                                                         LocalDateTime endDate)
            throws TradeRecordDAOException;

    public List<TradeRecord> getRecentTrades(Long pharmacyId, int days)
            throws TradeRecordDAOException;



}
