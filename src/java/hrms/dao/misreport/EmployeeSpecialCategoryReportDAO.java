/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.misreport;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import jxl.write.WritableWorkbook;

/**
 *
 * @author HP
 */
public interface EmployeeSpecialCategoryReportDAO {

    public List getEmployeeSpecialCategoryReportList();
    public void downloadEmployeeSpecialCategoryReportExcel(OutputStream out, String fileName,WritableWorkbook workbook);
    
    public List searchEmployeeSpecialCategoryReportList(String fdate, String tdate);

}
