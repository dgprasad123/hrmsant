package hrms.dao.payroll.thirdscheduleCheckingAuth;

import com.itextpdf.text.Document;
import hrms.common.Message;
import hrms.model.payroll.thirdschedulecheckingauth.ThirdScheduleCheckingAuthForm;
import java.util.List;

public interface ThirdScheduleCheckingAuthDAO {
    
    public List getEmployeeList(String empid, int rows, int page,String offcode);

    public int getEmployeeListCount(String empid,String offcode);

    public void thirdSchedulePDF(Document document, String empid);
    
    public void thirdScheduleIASPDF(Document document, ThirdScheduleCheckingAuthForm tform);
    
    public ThirdScheduleCheckingAuthForm getThirdScheduleData(String empid);
    
    public Message saveThirdScheduleCheckingAuthData(ThirdScheduleCheckingAuthForm tform,String approve);
    
    public Message saveVerifyingAuthData(String chkEmp,String authEmp);
    
    public Message revertCheckingAuthEmp(String chkEmp);
    
    public String getEmployeeOfficeName(String empid);
    
    public List getOfficeList(String empid);
    
    public void thirdScheduleUGCPDF(Document document, String empid);
    
    public String thirdScheduleRevertDC(String empid);
}
