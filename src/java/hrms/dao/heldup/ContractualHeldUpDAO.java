package hrms.dao.heldup;

import hrms.common.Message;
import java.util.List;

public interface ContractualHeldUpDAO {
    
    public List getContractualEmpList(String offCode,int page,int rows);
    
    public Message saveHeldUpData(String empid,String heldupdate);
    
    public int getTotalEmployeeCount(String offCode);
}
