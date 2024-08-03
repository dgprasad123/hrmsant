/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.payroll.schedule;

public class OtcForm82Bean extends ScheduleHelper{
    
    private String billDesc;
    private String year;
    private String treasuryName;
    private String month;
    private String netPay=null;
    private String netPayWord=null;
    private int toDdoAccount;
    private String noofEmp=null;
    private String lastDate = null;
    private int monthinNum = 0;
    private String benRefNo = null;
    private String gradePayAmt = null;
    private String daAmt = null;
    private String hraAmt = null;
    private String otherAllowanceAmt = null;
    
    public String getBillDesc() {
        return billDesc;
    }

    public void setBillDesc(String billDesc) {
        this.billDesc = billDesc;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTreasuryName() {
        return treasuryName;
    }

    public void setTreasuryName(String treasuryName) {
        this.treasuryName = treasuryName;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getNetPay() {
        return netPay;
    }

    public void setNetPay(String netPay) {
        this.netPay = netPay;
    }

    public String getNoofEmp() {
        return noofEmp;
    }

    public void setNoofEmp(String noofEmp) {
        this.noofEmp = noofEmp;
    }

    public int getToDdoAccount() {
        return toDdoAccount;
    }

    public void setToDdoAccount(int toDdoAccount) {
        this.toDdoAccount = toDdoAccount;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public int getMonthinNum() {
        return monthinNum;
    }

    public void setMonthinNum(int monthinNum) {
        this.monthinNum = monthinNum;
    }

    public String getBenRefNo() {
        return benRefNo;
    }

    public void setBenRefNo(String benRefNo) {
        this.benRefNo = benRefNo;
    }

    public String getGradePayAmt() {
        return gradePayAmt;
    }

    public void setGradePayAmt(String gradePayAmt) {
        this.gradePayAmt = gradePayAmt;
    }

    public String getDaAmt() {
        return daAmt;
    }

    public void setDaAmt(String daAmt) {
        this.daAmt = daAmt;
    }

    public String getHraAmt() {
        return hraAmt;
    }

    public void setHraAmt(String hraAmt) {
        this.hraAmt = hraAmt;
    }

    public String getOtherAllowanceAmt() {
        return otherAllowanceAmt;
    }

    public void setOtherAllowanceAmt(String otherAllowanceAmt) {
        this.otherAllowanceAmt = otherAllowanceAmt;
    }

    public String getNetPayWord() {
        return netPayWord;
    }

    public void setNetPayWord(String netPayWord) {
        this.netPayWord = netPayWord;
    }

        
    
    
    
    
    
}
