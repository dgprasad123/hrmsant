/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.incrementsanction;

import hrms.model.incrementsanction.ContractualEmployeeIncrementForm;
import java.util.List;

/**
 *
 * @author Surendra
 */
public interface IncrementForSanctionContractualDAO {
    
     public List getIncrementContractualList(String empid);

    public ContractualEmployeeIncrementForm getIncrementContractualData(int incrId, String empId) ;

    public void saveIncrementContractual(ContractualEmployeeIncrementForm incr,String loginempid);
    
    public void updateIncrementContractual(ContractualEmployeeIncrementForm incr, String loginempid);
    
   
}
