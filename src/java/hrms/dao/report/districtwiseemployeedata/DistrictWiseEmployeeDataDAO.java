package hrms.dao.report.districtwiseemployeedata;

import javax.servlet.http.HttpServletResponse;

public interface DistrictWiseEmployeeDataDAO {
    
    public void downloadDistrictWiseEmployeeDataExcel(HttpServletResponse response,String distCode);
    
}
