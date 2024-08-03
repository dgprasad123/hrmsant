package hrms.dao.report.budgetRequirement;

import java.util.ArrayList;
import javax.servlet.http.HttpServletResponse;

public interface BudgetRequirementDAO {
    
    public ArrayList getEmployeeList(String offCode);
    
    public void downloadBudgetRequirementDataExcel(HttpServletResponse response,String offCode,ArrayList emplist);
    
}
