package hrms.dao.ContractualToRegular;

import hrms.model.ContractualToRegular.ContractualToRegularForm;
import java.util.List;

public interface ContractualToRegularDAO {
    
    public List getContractualToRegularEmployeeList(String offCode);
    
    public ContractualToRegularForm getContractualToRegularData(String empid);
    
    public int saveContractualToRegular(ContractualToRegularForm ctrform);
    
    public int deleteContractualToRegular(ContractualToRegularForm ctrform);
    
    public List getFilteredEmployeeList(String offCode, String empType);
    
    public boolean isDuplicate7thPayRevisionData(String empid);
    
}
