/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.payroll.schedule;

public class PrivateLoanScheduleBean extends ScheduleHelper{
    
    private String officeName=null;  
    private String billDesc=null;    
    private int year=0;    
    private String month=null;  
    private String ddoAccountNo=null;

    private int deductedAmt=0;
    private String deductedAmtDesc=null;
        
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    


    public String getDdoAccountNo() {
        return ddoAccountNo;
    }

    public void setDdoAccountNo(String ddoAccountNo) {
        this.ddoAccountNo = ddoAccountNo;
    }

    public String getDeductedAmtDesc() {
        return deductedAmtDesc;
    }

    public void setDeductedAmtDesc(String deductedAmtDesc) {
        this.deductedAmtDesc = deductedAmtDesc;
    }

    public int getDeductedAmt() {
        return deductedAmt;
    }

    public void setDeductedAmt(int deductedAmt) {
        this.deductedAmt = deductedAmt;
    }
    
    
    
}
