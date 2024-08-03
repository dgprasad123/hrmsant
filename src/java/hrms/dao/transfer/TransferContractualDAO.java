package hrms.dao.transfer;

import hrms.model.transfer.TransferForm;
import java.sql.SQLException;
import java.util.List;

public interface TransferContractualDAO {
    
    public List getTransferContractualList(String empid);
    
    public void saveTransferContractual(TransferForm transferForm,String loginempid,String selectedempid);
    
    public void updateTransferContractual(TransferForm transferForm);
    
    public String getPostNomenclature(String empid);
    
    public TransferForm getEmpTransferData(TransferForm trform,int transferId) throws SQLException;
}
