package gr.aueb.cf.pharmapp.dao;

import gr.aueb.cf.pharmapp.dto.RecentTradeDTO;
import gr.aueb.cf.pharmapp.exceptions.TradeRecordDAOException;
import gr.aueb.cf.pharmapp.model.TradeRecord;

import java.time.LocalDateTime;
import java.util.List;

public interface ITradeRecordDAO {


    TradeRecord save(TradeRecord tradeRecord) throws TradeRecordDAOException;
    TradeRecord update(TradeRecord tradeRecord) throws TradeRecordDAOException;
    void delete(Long id) throws TradeRecordDAOException;
    TradeRecord getById(Long id) throws TradeRecordDAOException;
    List<TradeRecord> getAll() throws TradeRecordDAOException;
    List<TradeRecord> findRecentTradesByPharmacy(Long pharmacyId,
                                                        int limit) throws TradeRecordDAOException;


}
