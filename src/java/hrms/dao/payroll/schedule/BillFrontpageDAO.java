/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.schedule;

import com.itextpdf.text.Document;
import hrms.model.payroll.billbrowser.BillChartOfAccount;
import hrms.model.payroll.billbrowser.BillObjectHead;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Surendra
 */
public interface BillFrontpageDAO {
    
    public String getDPHead(String billno,int aqMonth, int aqYear);
    
    public ArrayList getScheduleListWithADCode(String billNo,int aqMonth, int aqYear);
    
    public int getSpecialPayAmount(int billNo,int aqMonth, int aqYear);
    
    public int getIrAmount(int billNo,int aqMonth, int aqYear);
    
    public String getPayHead(int billNo);
    
    public int getEmployeeCount(String offcode,String postgrptype, int billId);
    
    public int getContractualEmployeeCount(String offcode,String type, int billId);
    
    public double getAmtCreditedToBeneficiary(int billNo,int aqMonth, int aqYear);
    
    public double getAmtCreditedToDDO(int billNo,int aqMonth, int aqYear);
    
   // public byte[] singlebillFrontPagePDF(String billmonth, String billNo, BillChartOfAccount billChartOfAccount, BillObjectHead boha, List scheduleList, List scheduleListTR, List oaList, int spAmt, int irAmt, int payAmt, String payHead,String offCode,int aqMonth, int aqYear);
    public void singlebillFrontPagePDF(Document document,String billmonth, String billNo, BillChartOfAccount billChartOfAccount, BillObjectHead boha, List scheduleList, List scheduleListTR, List oaList, int spAmt, int irAmt, int payAmt, String payHead,String offCode,int aqMonth, int aqYear);
    public ArrayList getScheduleListWithADCodeTR(String billNo,int aqMonth, int aqYear);
    
}
