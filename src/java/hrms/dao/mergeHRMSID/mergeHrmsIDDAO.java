/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.mergeHRMSID;

import hrms.model.mergeHRMSID.mergeDuplicateHrmsidForm;
import java.util.ArrayList;

/**
 *
 * @author Madhusmita
 */
public interface mergeHrmsIDDAO {

    public mergeDuplicateHrmsidForm getEmpData(String srcEmpId, String finalEmpId);

    public ArrayList getLastSalary(String empid);
    
    public mergeDuplicateHrmsidForm mergeBothHrmsId(String srcEmpId, String finalEmpId);

}
