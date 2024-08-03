/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.payroll.schedule;


public class AuditRecoveryBean extends ScheduleHelper{
    
 // HEADER PROPERTIES   
    private String aqMonth=null;
    private String officeName=null;
    private String billDesc=null;
    
 // EMPLOYEE LIST PROPERTIES
    private String amtRec;
    private String amtDeduct;
    private String noofInstallment;
    private int balance;
    private String auditReport;
    private String hod;
    private String remarks;
    
    private String pagebreakAR;
    private String pageHeaderAR;
    private String carryFordAmt;
    
    public String getAqMonth() {
        return aqMonth;
    }

    public void setAqMonth(String aqMonth) {
        this.aqMonth = aqMonth;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getBillDesc() {
        return billDesc;
    }

    public void setBillDesc(String billDesc) {
        this.billDesc = billDesc;
    }

    public String getAmtRec() {
        return amtRec;
    }

    public void setAmtRec(String amtRec) {
        this.amtRec = amtRec;
    }

    public String getAmtDeduct() {
        return amtDeduct;
    }

    public void setAmtDeduct(String amtDeduct) {
        this.amtDeduct = amtDeduct;
    }

    public String getNoofInstallment() {
        return noofInstallment;
    }

    public void setNoofInstallment(String noofInstallment) {
        this.noofInstallment = noofInstallment;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getAuditReport() {
        return auditReport;
    }

    public void setAuditReport(String auditReport) {
        this.auditReport = auditReport;
    }

    public String getHod() {
        return hod;
    }

    public void setHod(String hod) {
        this.hod = hod;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPagebreakAR() {
        return pagebreakAR;
    }

    public void setPagebreakAR(String pagebreakAR) {
        this.pagebreakAR = pagebreakAR;
    }

    public String getPageHeaderAR() {
        return pageHeaderAR;
    }

    public void setPageHeaderAR(String pageHeaderAR) {
        this.pageHeaderAR = pageHeaderAR;
    }

    public String getCarryFordAmt() {
        return carryFordAmt;
    }

    public void setCarryFordAmt(String carryFordAmt) {
        this.carryFordAmt = carryFordAmt;
    }
    
    
    
}
