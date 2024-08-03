package hrms.dao.ContractualEmployeeAssignPost;

import hrms.model.ContractualEmployeeAssignPost.ContractualEmployeeAssignPostBean;
import java.util.List;

public interface ContractualEmployeeAssignPostDAO {
    
    public List getContractualEmployeeList(String offCode);
    
    public List getVacantPost(String offCode);
    
    public String mapVacantPostToEmployee(ContractualEmployeeAssignPostBean cbean);
    
}
