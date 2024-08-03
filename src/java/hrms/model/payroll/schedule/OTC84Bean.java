/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.payroll.schedule;

public class OTC84Bean extends ScheduleHelper{
    
    private String netAmount=null;
    private String grossAmountWord=null;
    private String gross=null;
    private String billDesc=null;    
    private String offName=null;
    private String billDate=null;
    private String billMonth=null;
    private String billYear=null; 
    private String treasuryOffice=null; 
    private String branchManager=null; 
    private String branchName=null;

    
    
    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getGrossAmountWord() {
        return grossAmountWord;
    }

    public void setGrossAmountWord(String grossAmountWord) {
        this.grossAmountWord = grossAmountWord;
    }

    public String getGross() {
        return gross;
    }

    public void setGross(String gross) {
        this.gross = gross;
    }

    public String getBillDesc() {
        return billDesc;
    }

    public void setBillDesc(String billDesc) {
        this.billDesc = billDesc;
    }

    public String getOffName() {
        return offName;
    }

    public void setOffName(String offName) {
        this.offName = offName;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getBillMonth() {
        return billMonth;
    }

    public void setBillMonth(String billMonth) {
        this.billMonth = billMonth;
    }

    public String getBillYear() {
        return billYear;
    }

    public void setBillYear(String billYear) {
        this.billYear = billYear;
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
    
    
    
    
}
