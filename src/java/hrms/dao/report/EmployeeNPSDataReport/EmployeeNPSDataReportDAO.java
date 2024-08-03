package hrms.dao.report.EmployeeNPSDataReport;

import hrms.model.report.EmployeeNPSDataReport.EmployeeNPSDataReportForm;
import java.util.ArrayList;


public interface EmployeeNPSDataReportDAO {
    
    public EmployeeNPSDataReportForm getEmployeeNPSAquitanceData(String pranno,String vchyear,String vchmonth);
    
}
