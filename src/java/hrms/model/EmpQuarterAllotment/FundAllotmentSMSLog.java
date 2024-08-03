/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.EmpQuarterAllotment;

/**
 *
 * @author Manas
 */
public class FundAllotmentSMSLog {

    private int qaid;
    private String smsdate;
    private String smsstring;
    private String mobileno;
    private String orderno;
    private String order_date;
    private int allotedAmt;
    private String employeeName;

    public int getQaid() {
        return qaid;
    }

    public void setQaid(int qaid) {
        this.qaid = qaid;
    }

    public String getSmsdate() {
        return smsdate;
    }

    public void setSmsdate(String smsdate) {
        this.smsdate = smsdate;
    }

    public String getSmsstring() {
        return smsstring;
    }

    public void setSmsstring(String smsstring) {
        this.smsstring = smsstring;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public int getAllotedAmt() {
        return allotedAmt;
    }

    public void setAllotedAmt(int allotedAmt) {
        this.allotedAmt = allotedAmt;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

}
