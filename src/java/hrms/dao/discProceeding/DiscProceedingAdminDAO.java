/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.discProceeding;

import hrms.model.discProceeding.DPPendingReportBean;
import hrms.model.empinfo.EmployeeSearchResult;
import hrms.model.empinfo.SearchEmployee;
import java.util.ArrayList;

/**
 *
 * @author Manas
 */
public interface DiscProceedingAdminDAO {
    public EmployeeSearchResult locateEmployee(SearchEmployee searchEmployee);
    
    public ArrayList getPendingDPReportByAdmin();
    
    public ArrayList getPendingDisciplinaryProceedingReportByAdmin();
    
    public ArrayList getPendingDPReportDepartmentwiseByAdmin();
    
    public ArrayList getPendingDPReportCadrewiseByAdmin();
}
