package hrms.dao.report.superannuationProjectionReport;

import hrms.common.ChartAttribute;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import jxl.write.WritableWorkbook;

public interface SuperannuationProjectionReportDAO {

    public ArrayList getSuperannuationEmpList(String offcode, String fdate, String tdate);

    public ArrayList getDistSuperannuationEmpList(String distcode, String fdate, String tdate);

    public ArrayList getSuperAnnuationProjectionData(List deptlist, int fromyear, int frommonth, int toyear, int tomonth);

    public ArrayList ViewSupperannuationProjectionReportDeptWise(int year, int month);

    public ArrayList ViewSupperannuationProjectionReportAdministartiveDeptOnly(int year, int month, String acctType);

    public ArrayList getEmployeesSuperannuationListGroupWise(int year, int month, String offCode, String postGroup, String acctType);

    public ArrayList getEmployeesSuperannuationList(int year, int month, String offCode, String acctType);

    public ArrayList ViewSupperannuationProjectionReportDistWise(String deptCode, int year, int month);

    public ArrayList ViewSupperannuationDetailsDistWise(String deptCode, String fdate, String tdate);

    public ArrayList ViewSupperannuationProjectionReportOfficeWise(String districtCode, int year, int month, String deptCode);

    public ArrayList ViewSupperannuationProjectionReportEmpWise(String offcode, int year, int month);

    public ArrayList ViewSupperannuationProjectionReportOnlyDept(int year, int month);

    public String getSuperannuationDataGroupWise(int year);

    public List getSuperannuationReportPeriodWise(String fromPeriod, String toPeriod);

    public String getSuperannuationReportPeriodDepartmentWise(String fromPeriod, String toPeriod);

    public void downloadSuperannuationProjectHOA(OutputStream out, String fileName, String offCode, WritableWorkbook workbook, int month, int year, String acctType);

    public ArrayList getOfficeWiseEmpList(String offCode);
    
    public List getMacpRacpList(String empid);
    
    public List getTppoList(String empid);
    
    public ArrayList getOfficeList();
    
    public List getPOList(String empid);    
 
    public List getPolicePostList(String offcode);

}
