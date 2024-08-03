/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.SBMonitoring;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import jxl.write.WritableWorkbook;

/**
 *
 * @author Manoj PC
 */
public interface SBMonitoringDAO {
    public List  getSBDeptWiseList(String fromDate, String toDate);
    public List getSBDeptWiseRetireList(String year, String deptCode);
    public List getSBDistrictWiseRetireList(String year, String deptCode);
    public List getSBOfficeWiseList(String displayFromDate, String displayToDate, String deptcode);
    public List getSBOfficeWiseRetireList(String deptcode, String year);
    public List getSBOfficeWiseDetailList(String deptcode, String year);
    public List getSBDistOfficeWiseRetireList(String distCode, String year, String deptCode);
    public List getSBEmpWiseList(String displayFromDate, String displayToDate, String offCode);
    //public List getSBEmpWiseRetireList(String offCode);
    public List getSBEmpWiseRetireList(String offCode, String year);
    public List getSBOffWiseRetireList(String offCode, String year, String strType);
    public List getSBOffWiseAllRetireList(String offCode, String year, String strType);
    public List getSBDeptWiseRetireList(String deptCode, String year, String strType);
    public List getDOSEmpList(String offCode);
    public void updateSBUpdateStatus(String empId, String sbStatus);
    public List getCadreWiseRetireList(String year);
    public List getSBDCWiseRetireList(String distCode, String year);
    
    public String getServiceBookValidationStatus(String empid);
    public String getOffCode(String empId);
    public void downloadDeptWiseSBUpdatedReport(OutputStream out, String fileName, WritableWorkbook workbook);
    public void downloadOfficeWiseSBUpdatedReport(String deptCode,OutputStream out, String fileName, WritableWorkbook workbook);
    public void downloadeDistrcitWiseSBUpdatedReport(String deptcode,OutputStream out, String fileName, WritableWorkbook workbook);
    public void downloadeSBDistOfficeWiseRetireList(String deptcode,String distName,String distCode,String fileName, WritableWorkbook workbook);
    public List getEmpCadreList(String deptCode);
}
