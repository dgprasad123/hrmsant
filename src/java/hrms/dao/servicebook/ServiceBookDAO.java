/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.servicebook;

import hrms.SelectOption;
import hrms.common.FileDownload;
import hrms.model.login.LoginUserBean;
import hrms.model.servicebook.EmpServiceBook;
import hrms.model.servicebook.ServiceBookAnomali;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lenovo pc
 */
public interface ServiceBookDAO {

    public EmpServiceBook getSHReport(String employeeCodes);

    public List getServiceBookAnnexureAData(String empid);

    public List getServiceBookAnnexureBData(String empid);

    public List getServiceBookAnnexureDData(String empid);

    public List getServiceBookAnnexureEData(String empid);

    public SelectOption getInitialJoiningData(String empid);

    public String saveServiceBookAnomali(ServiceBookAnomali ServiceBookAnomali);

    public List getAnomaliesListEmployee(String empId);

    public List getAnomaliesListOffice(String offcode);

    public FileDownload getFileDownloadData(int anomaliId);

    public List getEmployeeList(String loginempid, String loginspc);

    public EmpServiceBook getSHReportValidator(String employeeCodes);

    public int validateServiceBookEntry(String empid, String id,LoginUserBean lub);

    public int validateNoNotificationServiceBookEntry(String empid, String id, String tabname,LoginUserBean lub);

    public int inValidateNoNotificationServiceBookEntry(String empid, String id, String tabname);

    public int inValidateServiceBookEntry(String empid, String id);
    
    public String inValidateSBEntryGroupWise(String checkedSBModule,String empid);
    
    public int removeServiceBookEntryType(String empid, String id);
    
    public EmpServiceBook getSHReportForAG(String employeeCodes);
    
     public List getFinancialDetailsById(String empId);
}
