/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.payroll.schedule;

public class OtcFormBean extends ScheduleHelper{
    
    private String billNo=null;
    private String billDesc=null;
    private String billDate=null;
    private String treasuryOffice=null;
    private String branchManager=null;
    private String branchName=null;
    private String netAmount=null;
    private String netAmountWord=null;
    private String ddoSignature=null;

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    
    public String getBillDesc() {
        return billDesc;
    }

    public void setBillDesc(String billDesc) {
        this.billDesc = billDesc;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getTreasuryOffice() {
        return treasuryOffice;
    }

    public void setTreasuryOffice(String treasuryOffice) {
        this.treasuryOffice = treasuryOffice;
    }

    public String getBranchManager() {
        return branchManager;
    }

    public void setBranchManager(String branchManager) {
        this.branchManager = branchManager;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getNetAmountWord() {
        return netAmountWord;
    }

    public void setNetAmountWord(String netAmountWord) {
        this.netAmountWord = netAmountWord;
    }

    public String getDdoSignature() {
        return ddoSignature;
    }

    public void setDdoSignature(String ddoSignature) {
        this.ddoSignature = ddoSignature;
    }
    
    
    
    
}
