package gr.aueb.cf.pharmapp.dao;

import gr.aueb.cf.pharmapp.exceptions.TradeRecordDAOException;
import gr.aueb.cf.pharmapp.model.TradeRecord;

import java.util.List;

public interface ITradeRecordDAO {


    public TradeRecord save(TradeRecord tradeRecord) throws TradeRecordDAOException;
    public TradeRecord update(TradeRecord tradeRecord) throws TradeRecordDAOException;
    public void delete(Long id) throws TradeRecordDAOException;
    public TradeRecord getById(Long id) throws TradeRecordDAOException;
    public List<TradeRecord> getAll() throws TradeRecordDAOException;
    public List<TradeRecord> getByPharmacy(Long pharmacyId) throws TradeRecordDAOException;
    public List<TradeRecord> getBetweenPharmacies(Long pharmacy1Id,
                                                  Long pharmacy2Id) throws TradeRecordDAOException;
    public Double calculateBalance(Long pharmacy1Id, Long pharmacy2Id) throws TradeRecordDAOException;
    public List<TradeRecord> getRecentTrades(Long pharmacyId, int days) throws TradeRecordDAOException;



}
