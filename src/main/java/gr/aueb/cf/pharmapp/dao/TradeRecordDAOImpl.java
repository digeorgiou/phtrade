package gr.aueb.cf.pharmapp.dao;

import gr.aueb.cf.pharmapp.exceptions.TradeRecordDAOException;
import gr.aueb.cf.pharmapp.model.TradeRecord;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class TradeRecordDAOImpl implements ITradeRecordDAO {

    private final EntityManagerFactory emf;

    public TradeRecordDAOImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public TradeRecord save(TradeRecord tradeRecord) throws TradeRecordDAOException {
        return null;
    }

    @Override
    public TradeRecord update(TradeRecord tradeRecord) throws TradeRecordDAOException {
        return null;
    }

    @Override
    public void delete(Long id) throws TradeRecordDAOException {

    }

    @Override
    public TradeRecord getById(Long id) throws TradeRecordDAOException {
        return null;
    }

    @Override
    public List<TradeRecord> getAll() throws TradeRecordDAOException {
        return List.of();
    }

    @Override
    public List<TradeRecord> getByPharmacy(Long pharmacyId) throws TradeRecordDAOException {
        return List.of();
    }

    @Override
    public List<TradeRecord> getBetweenPharmacies(Long pharmacy1Id, Long pharmacy2Id) throws TradeRecordDAOException {
        return List.of();
    }

    @Override
    public Double calculateBalance(Long pharmacy1Id, Long pharmacy2Id) throws TradeRecordDAOException {
        return 0.0;
    }

    @Override
    public List<TradeRecord> getRecentTrades(Long pharmacyId, int days) throws TradeRecordDAOException {
        return List.of();
    }
}
