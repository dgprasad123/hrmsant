package hrms.dao.payroll.payslip;

import com.itextpdf.text.Document;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.payslip.ADDetails;
import hrms.model.payroll.payslip.PaySlipDetailBean;
import java.util.ArrayList;
import java.util.List;

public interface PaySlipDAO {

    public List getPaySlip(String empid, int year, int month);
    public List getPaySlipForeignBody(String empid, int year, int month,int billNo );

    public String getAQSLNo(String empid, int year, int month);

    public PaySlipDetailBean getEmployeeData(String aqslno, int year, int month);

    //public List getAllowanceDeductionList(String aqslno, String adType, int year, int month);

    public List getPrivateDedeuctionList(String aqslno, int year, int month);
    
    public List getPrivateDedeuctionListForPaySlip(String aqslno, int year, int month);
    
    public List getLoanList(String aqslno, int year, int month);

    public void getPaySlipPDF(Document document, PaySlipDetailBean pbeandetail, ArrayList allownacelist, ArrayList deductionlist, ArrayList privatedeductionlist, ArrayList loanlist);

    public String getTokenGeneratedBillNo(String empid, int year, int month);
    
    public ADDetails[] getAllowanceDeductionList(String aqslno, String adType, int year, int month);
    
    public ADDetails[] getPrivateDeductionList(String aqslno, int year, int month);
    
    public void getPaySlipBillWise(Document document, List dataList, int billNo, CommonReportParamBean crb,String qrcodepath);
    
    public List getPayBillData(int billNo, int aqMonth, int aqYear);
    
    public void getPaySlipBillWise2(Document document, List dataList, int billNo, CommonReportParamBean crb);
    
    public List getBillWiseEmployeeList(String billNo, int year, int month);
    
    public PaySlipDetailBean billAmt(String billNo, int year, int month);
    
     public PaySlipDetailBean getContributionDetails(String empid, int year, int month);
    
}
