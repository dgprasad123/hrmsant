package hrms.dao.payroll.thirdscheduleVerifyingAuth;

import com.itextpdf.text.Document;
import hrms.common.Message;
import hrms.model.payroll.thirdscheduleverifyingauth.ThirdScheduleVerifyingAuthForm;
import java.util.List;

public interface ThirdScheduleVerifyingAuthDAO {
    
    public List getEmployeeList(String empid, int rows, int page,String offcode);

    public int getEmployeeListCount(String empid,String offcode);

    public void thirdSchedulePDF(Document document, String empid);
    
    public void thirdScheduleIASPDF(Document document, ThirdScheduleVerifyingAuthForm tform);
    
    public ThirdScheduleVerifyingAuthForm getThirdScheduleData(String empid);
    
    public Message saveThirdScheduleVerifyingAuthData(ThirdScheduleVerifyingAuthForm tform,String approve);
    
    public Message revertVerifyingAuthEmp(String chkEmp);
    
    public String getEmployeeOfficeName(String empid);
    
    public List getOfficeList(String empid);
    
    public void thirdScheduleUGCPDF(Document document, String empid);
}
