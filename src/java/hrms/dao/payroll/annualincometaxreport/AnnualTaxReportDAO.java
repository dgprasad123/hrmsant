package hrms.dao.payroll.annualincometaxreport;

import hrms.model.payroll.AnnualIncomeTax;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

public interface AnnualTaxReportDAO {
    
    public List getBillList(String offCode);
    
    public List getFinyearList();
    
    public List getEmployeeList(String billgroupid,String finyear);
    
    public AnnualIncomeTax getAnnualIncomeTaxData(String empid,String finyear);
    
    public void downloadExcel(HttpServletResponse response,String empid,String offcode,String billgrpid,String finyear);
}
