package hrms.dao.payroll.thirdschedule;

import com.itextpdf.text.Document;
import hrms.common.Message;
import hrms.model.payroll.thirdschedule.ThirdScheduleBean;
import hrms.model.payroll.thirdschedule.ThirdScheduleForm;
import java.util.List;

public interface ThirdScheduleDAO {

    public List getEmployeeList(String empid, int rows, int page);

    public int getEmployeeListCount(String empid);

    public void thirdSchedulePDF(Document document, String empid);
    
    public ThirdScheduleForm getThirdScheduleData(String empid);
    
    public Message saveThirdScheduleData(ThirdScheduleForm tform,String approve);
    
    public Message saveCheckingAuthData(String chkEmp,String authEmp);
    
    public Message revertPayFixationAuthData(String chkEmp);
    
    public void thirdScheduleIASPDF(Document document, ThirdScheduleForm tform);
    
    public String getEmployeeOfficeName(String empid);
    
    public void thirdScheduleUGCPDF(Document document, String empid);
    
    public List getBillGroupWiseEmployeeList(String billgroupid);
    
    public ThirdScheduleForm getCont6YrsToRegularData(String empid);
    
    public String saveContractual6YrsToRegularThirdScheduleData(ThirdScheduleForm tform);
    
    public String updateContractual6YrsToRegularPayData(ThirdScheduleForm tform);
    
    public void approveContractual6YrsToRegularThirdSchedule(ThirdScheduleForm tform);
    
    public String validatePayAgainstLevelAndCell(int level,int cell,int basic);
    
    public int revertCont6YrsThirdScheduleDC(String empid);
}
