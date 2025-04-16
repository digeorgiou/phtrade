package gr.aueb.cf.pharmapp.validator;

import gr.aueb.cf.pharmapp.dao.ITradeRecordDAO;
import gr.aueb.cf.pharmapp.dao.TradeRecordDAOImpl;
import gr.aueb.cf.pharmapp.dto.BaseTradeRecordDTO;
import gr.aueb.cf.pharmapp.exceptions.TradeRecordDAOException;
import gr.aueb.cf.pharmapp.service.ITradeRecordService;
import gr.aueb.cf.pharmapp.service.TradeRecordServiceImpl;
import jakarta.persistence.EntityManagerFactory;

import java.util.HashMap;
import java.util.Map;

public class TradeRecordValidator {

    private final EntityManagerFactory emf;
    private final ITradeRecordDAO tradeRecordDAO;
    private final ITradeRecordService tradeRecordService;

    public TradeRecordValidator(EntityManagerFactory emf){
        this.emf = emf;
        this.tradeRecordDAO = new TradeRecordDAOImpl(emf);
        this.tradeRecordService = new TradeRecordServiceImpl(emf);
    }

    public <T extends BaseTradeRecordDTO> Map<String, String > validate(T dto) throws TradeRecordDAOException {
        Map<String, String> errors = new HashMap<>();

        if(dto.getDescription().length() < 2 || dto.getDescription().length() > 100) {
            errors.put("description", "Η περιγραφή πρέπει να είναι μεταξύ 2 " +
                    "και 100 χαρακτήρων");
        }

        if(!dto.getAmount().toString().matches("^[0-9]+([,.][0-9]{1,2})?$")) {
            errors.put("amount", "Παρακαλώ εισάγετε αριθμό με μεχρι 2 " +
                    "δεκαδικά ψηφία");
        }

        return errors;
    }

}
