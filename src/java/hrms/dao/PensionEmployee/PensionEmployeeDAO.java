/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */ 
package hrms.dao.PensionEmployee;

import hrms.model.employee.PensionNomineeList;
import hrms.model.employee.PensionProfile;
import java.util.List;

/**
 *
 * @author Manas
 */
public interface PensionEmployeeDAO {

    public void insertEmployeePensionDetails(PensionProfile profile, String empid);
    
    public void updateEmployeePensionDetails(PensionProfile profile, String empid);

    public List getPensionEmpNomineeDetails(String empid);

    public PensionProfile getPensionEmpProfileDetailsById(String empid, String acctType);

    public List getPensionEmpGuardianDetails(String empid);

    public void updatePensionCompletedStatusData(PensionProfile profile, String empid);
    
    public PensionProfile getEmpPensionDet(String empid);

    public String checkProfileStatus(String empid);
    
    public void updatePensionType(PensionNomineeList pensionNomineeList, String empid, int serialNo);

}
