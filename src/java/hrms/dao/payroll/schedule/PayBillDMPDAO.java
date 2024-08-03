/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.payroll.schedule;

import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.billbrowser.BillConfigObj;
import hrms.model.payroll.schedule.SectionWiseAqBean;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author Manas 
 */
public interface PayBillDMPDAO {
   // public File writePayBillDMPI(String billNo);
    
    public String getBtidSwrWrr(String billNo);

    public List getQtrPool();

    public List getWRRScheduleEmployeeList(String billno, String schedule, String qtr_pool_id, int month, int year);
    
    public HashMap getPayAbstract(ArrayList aqlist, String billno, int month, int year);

    public ArrayList getSectionWiseBillDtls(String billno, int month, int year, String formatType, BillConfigObj getBillConfig, String empType);

    public String[] getColConfiguredDtata(String offCode, String billgroupId, int colNumber, String configLevel);

    public BillConfigObj getBillConfig(String billno);

    public String getADCodeHead(String billNo, String ad,int aqMonth, int aqYear);

    public int getGPFAmount(String billId, String btId,int aqMonth, int aqYear);

    public SectionWiseAqBean getAqBillDetails(String billno, int mon, int year, String section, int secslno, BillConfigObj billConfigObj);

    public SectionWiseAqBean getAqBillDetailsF2(String billno, int mon, int year, String section, int secslno, BillConfigObj billConfigObj);

    public SectionWiseAqBean getAqBillDetailsContractual(String billno, int mon, int year, String section, int secslno, BillConfigObj billConfigObj);

    public int getGrossPay(String slno, String billno);

    public int getPrivateLoan(String aqslno, String billno);

    public int getPrivateDeductionLoanForEmp(String aqslno, String billno);

    public int getPrivateDeductionLoan(String aqslno, boolean isTransferToDDOAccount, String billno);

    public Vector getPrivateDeductionLoan(String aqslno, String billno);

    public int getPrivateDeduction(String aqslno, String billno);

    public int getTotalDedn(String slno, String billno);

    public String getRefDesc(String adCode, String nowdedn, String aqSlNo, String billNo);

    public CommonReportParamBean getCommonReportParameter(String billNo);
    
    public CommonReportParamBean getCommonReportParameterDHE(String billNo);
    
    

}
