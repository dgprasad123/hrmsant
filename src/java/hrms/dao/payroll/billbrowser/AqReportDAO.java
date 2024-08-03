/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.billbrowser;

import com.itextpdf.text.Document;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.aqreport.AqreportBean;
import hrms.model.payroll.billbrowser.AcquaintanceBean;
import hrms.model.payroll.billbrowser.BillBean;
import hrms.model.payroll.billbrowser.BillChartOfAccount;
import hrms.model.payroll.billbrowser.BillConfigObj;
import hrms.model.payroll.billbrowser.BillObjectHead;
import hrms.model.payroll.schedule.Schedule;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jxl.write.WritableWorkbook;

/**
 *
 * @author Manas Jena
 */
public interface AqReportDAO {

    public Map<String, List> getAllColumnNameList(String billNo, String month, String year);

    public List getDeductionGrandAbstract(String billNo, String month, String year);

    public String getADCodeDesc(String adcode);

    public ArrayList<AcquaintanceBean> getAcquaintance1(String billNo, String aqDtlsTableName);

    public ArrayList<AcquaintanceBean> getAcquaintance(String billNo);

    public ArrayList<AcquaintanceBean> getAcquaintanceDHE(String billNo, String offCode);

    public AcquaintanceBean getAqMastDtl(String aqslno);

    public ArrayList getAcquaintanceDtlDed(String empcode, String aqslno, String aqDtlsTableName);

    public ArrayList getAcquaintanceDtlDed(String aqslno, String aqDtlsTableName);

    public ArrayList getAcquaintanceDtlAll(String aqslno, String aqDtlsTableName);

    public ArrayList getAcquaintanceDtl(String aqslno, String aqDtlsTableName);

    public BillConfigObj getBillConfig(String billno);

    public String getAqDtlsTableName(String billNo);

    public BillChartOfAccount getBillChartOfAccount(String billNo);

    public BillChartOfAccount getBillChartOfAccountDHE(String billNo);

    public ArrayList getScheduleListWithADCode(String billNo, int aqYear, int aqMonth);

    public BillObjectHead getBillObjectHeadAndAmt(String billNo, int aqYear, int aqMonth);

    public ArrayList getLongTermLoanDtls(BillBean bb, String aqDTLS);

    public List getLtaAdcodeWiseList(BillBean bb, String aqDtlsTableName);

    public Schedule getScheduleList(String scheduleName);

    public ArrayList getSectionWiseBillDtls(String billno, String mon, String year, String format, BillConfigObj billConfigObj, String empType,
            String column9NameList, String column10NameList, String column11NameList, String column12NameList, String column13NameList, String column14NameList,
            String column15NameList, String column16NameList, String column17NameList, String column18NameList);

    //-----------------For DHE-----------------------
    public ArrayList getSectionWiseBillDtlsDHE(String billno, String mon, String year, String format, String offCode, BillConfigObj billConfigObj, String empType,
            String column9NameList, String column10NameList, String column11NameList, String column12NameList, String column13NameList, String column14NameList,
            String column15NameList, String column16NameList, String column17NameList, String column18NameList);

    public String getMonth(int mon);

    public String getEmpType(String billno, String mon, String year);

    public int getOtherAllowance(String billno, String mon, String year);

    public CommonReportParamBean getBillDetails(String billNo);

    public CommonReportParamBean getBillDetailsDHE(String billNo);

    public int getColGrandTotal(ArrayList aqlist, String col, String colName, String nowdedn);

    public int getColGrandTotal(ArrayList aqlist, String col);

    public int getColGrandTotal(ArrayList aqlist, String col, int rowNo, String nowdedn);

    public HashMap getPayAbstract(ArrayList aqlist);

    public List getAllowanceDetails(String billNo, int year, int month);

    public int getPayAmt(int billNo);

    public double getTotalDeduction(String aqSlNo, String aqDtlsTableName);

    public double getTotalAllowance(String aqSlNo, String aqDtlsTableName);

    public void saveAquitanceBasic(String aqslNo, int aqbasic);

    public int getAquitanceBasic(String aqslNo);

    public void downloadAqReportExcel(OutputStream out, String fileName, WritableWorkbook workbook, String billNo);

    public void stopPay(String aqslno);

    public void generateAqReportPDF(Document document, String billNo, CommonReportParamBean crb, AqreportBean aqreportFormBean);

    public Schedule getArrScheduleListWithADCode(String billNo);

    public BillObjectHead getArrBillObjectHeadAndAmt(String billNo,String typeofbill);
    
    public void GenerateOtherArrearAquitancePDFReport(Document document, String billNo, CommonReportParamBean crb, List arrAqList,AqreportBean aqreportFormBean);
    
    public void GenerateArrear4Arrear6AquitancePDFReport(Document document, String billNo, CommonReportParamBean crb, List arrAqList,AqreportBean aqreportFormBean);
    
    public void GenerateNPSArrearAquitancePDFReport(Document document, String billNo, CommonReportParamBean crb, List arrAqList,AqreportBean aqreportFormBean);
    
    public void GenerateJudiciaryArrearAquitancePDFReport(Document document, String billNo, CommonReportParamBean crb, List arrAqList,AqreportBean aqreportFormBean);

}
