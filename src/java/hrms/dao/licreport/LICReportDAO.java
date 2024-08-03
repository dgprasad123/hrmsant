package hrms.dao.licreport;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

public interface LICReportDAO {
    
    public void downloadExcelLICTreasuryWise(HttpServletResponse response,String treasury,ArrayList paybilllist);
    
    public List getYearList();
    
    public List getDivisionList();
    
    public List getDivisionWiseTreasuryList(String division);
    
    public void downloadExcelLICDivisionWise(HttpServletResponse response,String treasury,String year,String month);
    
    public void createExcelLICTreasuryWise(OutputStream out,String treasury,ArrayList paybilllist);
    
    public void createExcelLICDivisionWise(OutputStream out, String treasury, String year, String month);
    
    public String getTreasuryNameFromDivision(String divCode,String trCodes);
}
