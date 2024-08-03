package hrms.dao.empinfo;

import com.itextpdf.text.Document;
import hrms.model.empinfo.EmployeeInformation;
import hrms.model.empinfo.MiscInfoBean;
import hrms.model.login.Users;
import hrms.model.notification.NotificationBean;
import java.util.ArrayList;

public interface EmployeeInformationDAO {
    
    public EmployeeInformation getEmployeeData(String empid,String gpf);
    
    public void updateEmployeeData(EmployeeInformation empinfo);
    
    public boolean isupdatePayOrPostingInfo(String empid,String wefdt,String ordDt,String type);
    
    public void updateEmpPayInfoOnDate(EmployeeInformation e,String empid);
    
    public ArrayList getMiscInfoList(String empId);
    
    public MiscInfoBean getMiscInfoData(NotificationBean nb);
    
    public EmployeeInformation GetIcardDetails(String empid,String gpf);
    
    public void getICardPDF(Document document, Users userBean, String filePath);
    
    public void getICardPDFforEsign(Document document, Users userBean, String filePath);
    
    public String unlockEmpprofileGroup(String checkedEmpProfile,String empid);
    
    public String getCheckedEmpIdentityProfile(String empid);
    
    public String getCheckedEmpLanguageProfile(String empid);
     
    public String getCheckedEmpQualificationProfile(String empid);
      
    public String getCheckedEmpFamilyProfile(String empid);
       
    public String getCheckedEmpAddressProfile(String empid);
}
