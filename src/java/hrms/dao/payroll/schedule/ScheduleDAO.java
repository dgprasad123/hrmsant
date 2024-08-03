/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.schedule;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.SelectOption;
import hrms.model.WaterRent;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.QuaterRent;
import hrms.model.payroll.billbrowser.BillChartOfAccount;
import hrms.model.payroll.billbrowser.BillObjectHead;
import hrms.model.payroll.schedule.AbsenteeStatementScheduleBean;
import hrms.model.payroll.schedule.AuditRecoveryBean;
import hrms.model.payroll.schedule.BankAcountScheduleBean;
import hrms.model.payroll.schedule.BillBackPageBean;
import hrms.model.payroll.schedule.BillContributionRepotBean;
import hrms.model.payroll.schedule.ComputerTokenReportBean;
import hrms.model.payroll.schedule.ECScheduleForm;
import hrms.model.payroll.schedule.ExcessPayBean;
import hrms.model.payroll.schedule.GPFScheduleBean;
import hrms.model.payroll.schedule.GisAndFaScheduleBean;
import hrms.model.payroll.schedule.ItScheduleBean;
import hrms.model.payroll.schedule.LicScheduleBean;
import hrms.model.payroll.schedule.LoanAdvanceScheduleBean;
import hrms.model.payroll.schedule.OTC84Bean;
import hrms.model.payroll.schedule.OtcForm82Bean;
import hrms.model.payroll.schedule.OtcFormBean;
import hrms.model.payroll.schedule.OtcPlanForm40Bean;
import hrms.model.payroll.schedule.PLIScheduleBean;
import hrms.model.payroll.schedule.PrivateLoanScheduleBean;
import hrms.model.payroll.schedule.PtScheduleBean;
import hrms.model.payroll.schedule.ReceiptRecoveryScheduleForm;
import hrms.model.payroll.schedule.SecondScheduleBean;
import hrms.model.payroll.schedule.VacancyStatementScheduleBean;
import hrms.model.payroll.schedule.VehicleScheduleBean;
import hrms.model.payroll.schedule.VoucherSlipBean;
import hrms.model.payroll.schedule.WrrScheduleBean;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import jxl.write.WritableWorkbook;

public interface ScheduleDAO {
    

    public List getDisplayReportList(String billNo, String billType, String reportToDisplay, int year, int month);

    //Codeing for All / Common Properties Schedule   
    public CommonReportParamBean getCommonReportParameter(String billNo);

    //Codeing for GPF Schedule
    public GPFScheduleBean getGPFScheduleHeaderDetails(String billno);

    public List getGPFScheduleTypeList(String billno, int aqmonth, int aqyear);

    public List getGPFScheduleAbstractList(String billno, int aqmonth, int aqyear);

    //Codeing for WRR Schedule
    public WrrScheduleBean getWRRScheduleEmployeeList(String billno, String schedule, int aqmonth, int aqyear);
    //public WrrScheduleBean getWRRScheduleHeaderDetails(String billno, String schedule);

    //Codeing for HIRE CHARGE VEHICLE / IT / AIS GROUP INSURANCE SCHEME(Sl-21) Schedule   
    public List getITScheduleEmployeeList(String billno, String schedule, int aqMonth, int aqYear);

    public ItScheduleBean getITScheduleHeaderDetails(String billno, String schedule);

    //Codeing for PT Schedule     
    public List getPTScheduleEmployeeList(String billno, int aqMonth, int aqYear);

    public PtScheduleBean getPTScheduleHeaderDetails(String billno);

    //Sl No-12-15 *#*Codeing for HBA And MOTOR CAR ADVANCE And RECOVERY FOR(HOUSE BUILDING ADVANCE FOR CYCLONE/FLOOD OF OCT 1999 
    //And MOTOR CYCLE ADVANCE And MOPED ADVANCE) Schedule     
    public List getLoanAdvanceSchedulePrincipalList(String schedule, String billno, int aqMonth, int aqYear);

    public List getLoanAdvanceScheduleInterestList(String schedule, String billno, int aqMonth, int aqYear);

    public LoanAdvanceScheduleBean getLoanAdvanceScheduleHeaderDetails(String billno, String schedule);

    //Codeing for FASTIVAL & GIS ADVANCE Schedule      
    public GisAndFaScheduleBean getGisAndFaScheduleHeaderDetails(String schedule, String billno);

    public List getGISandFAScheduleEmpList(String schedule, String billno, int aqYear, int aqMonth);

    /* TO BE DEPLOY ON 17-NOV FROM HERE */
    //Codeing for LIC PREMIUM Schedule      
    public LicScheduleBean getLICScheduleHeaderDetails(String billno);

    public List getLICScheduleEmpList(String schedule, String billno, int aqMonth, int aqYear);

    //Codeing for PLI PREMIUM Schedule      
    public PLIScheduleBean getPLIScheduleHeaderDetails(String billno);

    public List getPLIScheduleEmpList(String billno, int year, int month);
    /* TO BE DEPLOY ON 17-NOV TO HERE */

    //Codeing for VACANCY STATEMENT  Schedule         
    public AbsenteeStatementScheduleBean getAbsntStmtScheduleHeaderDetails(String billno);

    public List getAbsntStmtScheduleEmpList(String billno);

    //Codeing for BANK SCHEDULE  Schedule         
    public BankAcountScheduleBean getBankAcountScheduleHeaderDetails(String billno);

    public List getBankAcountScheduleEmpList(String billno, int year, int month);

    public List getBankNameScheduleList(String billno, int year, int month);

    //Codeing for BILL BACK PAGE  Schedule       
    public BillBackPageBean getBillBackPgScheduleHeaderDetails(String billno, int aqYear, int aqMonth);

    public List getBillBackPgScheduleEmpList(String billno, int aqYear, int aqMonth);

    //Codeing for ANNEXURE-I to IV NPS Schedule       
    public BillContributionRepotBean getBillContributionRepotScheduleHeaderDetails(String annexure, String billno);

    public List getBillContributionRepotScheduleEmpList(String annexure, String billno, int year, int month);

// Codeing for TPF Schedule
    public List getEmployeeWiseTPFList(String billno);

    public List getTPFAbstractList(String billno);

    //Codeing for Periodic Absentee Statement Schedule       
    public List getPeriodicAbsenteeStatementScheduleEmpList(String billno);

    //Codeing for Periodic Absentee Statement Schedule       
    public VoucherSlipBean getVoucherSlipScheduleDetails(String billno, int aqYear, int aqMonth);

    //Codeing for OTC52 Schedule       
    public OtcForm82Bean getOTCForm82ScheduleDetails(String billno);

    //Coding for Arrear OTC52 Schedule       
    public OtcForm82Bean getArrOTCForm82ScheduleDetails(String billno);

    //Codeing for OTC52 Schedule       
    public OtcFormBean getOTCForm52ScheduleDetails(String billno, int year, int month);

    //Codeing for Arrear OTC52 Schedule       
    public OtcFormBean getArrOTCForm52ScheduleDetails(int billno);

    //Codeing for OTC40 Schedule       
    public OtcPlanForm40Bean getOTCForm40ScheduleDetails(String billno, int year, int month, int gpAmt, CommonReportParamBean crb);

    //Coding for Arrear OTC40 Schedule
    public OtcPlanForm40Bean getArrOTCForm40ScheduleDetails(String billno, int year, int month, CommonReportParamBean crb);

    public List getOTCForm40ScheduleEmpList(String billno, int year, int month);

    //Codeing for Computer Token Report Schedule       
    public ComputerTokenReportBean getCompTokenRepotScheduleDetails(String billno, int year, int month);

    //Codeing for Computer Token Report Schedule for Arrear
    public ComputerTokenReportBean getArrCompTokenRepotScheduleDetails(String billno, int year, int month);

    //Codeing for Excess Pay Schedule Schedule       
    public ExcessPayBean getExcessPayScheduleHeaderDetails(String billno);

    public List getExcessPayScheduleEmpDetails(String billno, int year, int month);

    //Codeing for OTC 84 Schedule       
    public OTC84Bean getOTC84ScheduleDetails(String billno, int year, int month, CommonReportParamBean bean);

    //Codeing for AUDIT RECOVERY Schedule       
    public AuditRecoveryBean getAuditRecoveryScheduleDetails(String billno);

    public List getAuditRecoveryScheduleEmpDetails(String billno, int year, int month);

    // Codeing for PRIVATE LOAN / DEDUCTION SCHEDULE   
    public PrivateLoanScheduleBean getPrivateLoanScheduleDetails(String billno);

    public List getPrivateLoanScheduleEmpDetails(String billno, int aqMonth, int aqYear);
    
    public List getCmrfScheduleEmpDetails(String billno, int aqMonth, int aqYear);

    public QuaterRent[] getRentData(int month, int year);
    
    public WaterRent[] getWaterRentData(String empid);

    public WaterRent[] getWaterRentData(int month, int year);
    
    public WaterRent[] getWaterRentData(int month, int year, String ddocode);

    public VacancyStatementScheduleBean getVacancyStmtScheduleHeaderDetails(String billno);

    public List getVacancyStmtScheduleEmpList(String billno);

    public void thirdSchedulePDF(Document document, String empid);

    public SecondScheduleBean getSecondScheduleData(String empid);

    public void secondSchedulePDF(Document document, String empid);

    public void secondScheduleIASPDF(Document document, String empid);

    public String isDuplicatePayRevisionOption(String empid);

    public void insertPayRevisionData(String empid, String offcode, String selectedOption, String postcode, String payscale, int gp, String txtDate, String hasUserChanged, String hasDDOChanged);

    public void billFrontPagePDF(Document document, String billmonth, String billNo, BillChartOfAccount billChartOfAccount, BillObjectHead boha, List scheduleList, List oaList, int spAmt, int irAmt, int payAmt, String payHead);

    public void billBackPagePDF(Document document, String billNo, BillBackPageBean backPageBean, List empList);

    //public void WRRSchedulePagePDF(Document document, String schedule, String billNo, WrrScheduleBean wrrBean, List empList);
    public void WRRSchedulePagePDF(Document document, String schedule, String billNo, CommonReportParamBean crb);

    public void WRRSchedulePagePDFF1(Document document, String schedule, String billNo, CommonReportParamBean crb);

    public void ITSchedulePagePDF(CommonReportParamBean crb, Document document, String schedule, String billNo, ItScheduleBean itBean, List empList);

    public void PTSchedulePagePDF(CommonReportParamBean crb, Document document, String billNo, PtScheduleBean ptBean, List empList, int aqMonth, int aqYear);

    public int getBasicAmount(String billNo);

    public int getAllowanceAndDeductionAmount(String billNo, String adType, int month, int year);

    public List getHCScheduleEmpList(String billNo, int year, int month);

    public List getLTCScheduleEmpList(String billNo, int year, int month);

    public String getChartOfAccount(String billNo);

    public VehicleScheduleBean getVehicleScheduleList(String billNo, int year, int month);

    public void LoanSchedulePagePDF(CommonReportParamBean crb, Document document, String schedule, String billNo, LoanAdvanceScheduleBean laBean, List priList, List intList);

    public void downloadPayDetailExcel(OutputStream out, String fileName, WritableWorkbook workbook, String billNo);

    public void downloadDACertificatePDF(Document document);

    public void OtcForm52SchedulePDF(Document document, String billNo, OtcFormBean otcBean);

    public void OtcForm40SchedulePDF(Document document, String billNo, OtcPlanForm40Bean otcBean, String payHead, List alowanceList, List deductList);

    public void OTCForm84PDF(Document document, String billNo, OTC84Bean otc84Bean);

    public void OTC82SchedulePDF(Document document, String billNo, OtcForm82Bean otcBean);

    public List getCSDataList(String billNo, int year, int month);

    public void getConveyanceSchedulePDF(Document document, String billNo, CommonReportParamBean crb, List dataList);

    public void getVehicleSchedulePDF(Document document, String billNo, CommonReportParamBean crb);

    public void getRTISchedulePdf(Document document, String billNo, CommonReportParamBean crb);

    public void getLtcSchedulePagePDF(Document document, String billNo, CommonReportParamBean crb);
    
    public void generateSchedulePDFReportsForAG(Document document,String schedule,ArrayList billList);
    
    public void generateBeneficiaryListPDF(Document document,CommonReportParamBean crb,String billNo,ArrayList emplist,double netAmt);
    
    public ArrayList getBillWiseEmployeeList(String billNo,String typeOfBill);
    
    public void secondScheduleUGCPDF(Document document, String empid);
    
    public SelectOption getBTPIHeaderClassification(String loantp);
    
    public void generateGISSchedulePDFReportsForAG(Document document, String schedule, ArrayList billList);
    
    public ECScheduleForm getECScheduleDetails(String billno);
    
    public void ECSchedulePagePDF(CommonReportParamBean crb, Document document, String billNo, ECScheduleForm ecForm, List empList, int aqMonth, int aqYear);
    
    public ReceiptRecoveryScheduleForm getReceiptRecoveryScheduleDetails(String billno);
    
    public void ReceiptRecoverySchedulePDF(CommonReportParamBean crb, Document document, String billNo, ReceiptRecoveryScheduleForm rrForm, List empList, int aqMonth, int aqYear);
    
    //Coding for GPF Schedule Pdf
    public void gpfSchedulePdf(PdfWriter writer,Document document, String billNo,List gpfTypeList, List gpfAbstractList,CommonReportParamBean crb,String totFig, double totAmt,GPFScheduleBean gpfHeader) ;
    
    //Coding for Computer Token Pdf
    public void computerTokenSchedulePdf(PdfWriter writer,Document document, String billNo,ComputerTokenReportBean tokenBean);
        
    //
    public void excesspaySchedulePdf(PdfWriter writer,Document document, String billNo,ExcessPayBean excessBean,List excessPayEmpList);

    public String allowedOfficeEsign(String offCode);
    
    public List getEPFScheduleTypeList(String billno, int aqmonth, int aqyear);
    
    public List getEPFScheduleAbstractList(String billno, int aqmonth, int aqyear);
    
    public void EPFSchedulePdf(PdfWriter writer,Document document, String billNo,List epfTypeList, List epfAbstractList,CommonReportParamBean crb,String totFig, double totAmt,GPFScheduleBean epfHeader) ;
    
    public void arrearBillFrontPagePDF(Document document, CommonReportParamBean crb, String billNo, BillChartOfAccount billChartOfAccount, List deductionList, List allowanceList, double allowanceAmt, double deductAmt, double netAmt,String payHead);
}
