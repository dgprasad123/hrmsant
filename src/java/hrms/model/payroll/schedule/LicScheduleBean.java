/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.schedule;

import java.util.ArrayList;

public class LicScheduleBean extends ScheduleHelper {

    private String aqSlno = null;
    private String deptName = null;
    private String offName = null;
    private String ddoName = null;
    private String ddoDesg = null;
    private String billdesc = null;
    private String billNo = null;
    private String monthYear = null;
    private String aqYear = null;
    public ArrayList premiumDetails = null;
    public String total = null;
    private String carryForward = null;
    private String pagebreakLIC = null;
    private String pageHeaderLIC = null;

    public String getAqYear() {
        return aqYear;
    }

    public void setAqYear(String aqYear) {
        this.aqYear = aqYear;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getOffName() {
        return offName;
    }

    public void setOffName(String offName) {
        this.offName = offName;
    }

    public String getDdoName() {
        return ddoName;
    }

    public void setDdoName(String ddoName) {
        this.ddoName = ddoName;
    }

    public String getDdoDesg() {
        return ddoDesg;
    }

    public void setDdoDesg(String ddoDesg) {
        this.ddoDesg = ddoDesg;
    }

    public String getBilldesc() {
        return billdesc;
    }

    public void setBilldesc(String billdesc) {
        this.billdesc = billdesc;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public String getAqSlno() {
        return aqSlno;
    }

    public void setAqSlno(String aqSlno) {
        this.aqSlno = aqSlno;
    }

    public ArrayList getPremiumDetails() {
        return premiumDetails;
    }

    public void setPremiumDetails(ArrayList premiumDetails) {
        this.premiumDetails = premiumDetails;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCarryForward() {
        return carryForward;
    }

    public void setCarryForward(String carryForward) {
        this.carryForward = carryForward;
    }

    public String getPagebreakLIC() {
        return pagebreakLIC;
    }

    public void setPagebreakLIC(String pagebreakLIC) {
        this.pagebreakLIC = pagebreakLIC;
    }

    public String getPageHeaderLIC() {
        return pageHeaderLIC;
    }

    public void setPageHeaderLIC(String pageHeaderLIC) {
        this.pageHeaderLIC = pageHeaderLIC;
    }
    
    

}
