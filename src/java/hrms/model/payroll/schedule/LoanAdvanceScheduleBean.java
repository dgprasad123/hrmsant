/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.schedule;

import java.util.ArrayList;

public class LoanAdvanceScheduleBean extends ScheduleHelper {

    private String scheduleOfRecovery = null;
    private String month = null;
    private String year = null;
    private String deptName = null;
    private String offName = null;
    private String ddoName = null;
    private String trName = null;
    private String billNo = null;
    private String billdesc = null;
    private String demandNo = null;
    private String scheduleName;
    
    private String empNameDesg=null;
    private String empName = null;
    private String empDesg = null;
    private String vchNo = null;
    private String vchDate = null;
    private String accNo = null;
    private double originalAmt;
    private String instalmentRec = null;
    private double decutedAmt;
    private double recAmt;
    private String drawingmonth = null;
    private double balOutstanding;
    private double total;
    private ArrayList piAdvRecList = null;
    private ArrayList piAdvRecInterestList = null;
    private String instalmentNo = null;
    private String deductionStdate = null;
    
    private String pagebreakLA = null;
    private String pageHeaderLA = null;

    
    public String getScheduleOfRecovery() {
        return scheduleOfRecovery;
    }

    public void setScheduleOfRecovery(String scheduleOfRecovery) {
        this.scheduleOfRecovery = scheduleOfRecovery;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
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

    public String getTrName() {
        return trName;
    }

    public void setTrName(String trName) {
        this.trName = trName;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBilldesc() {
        return billdesc;
    }

    public void setBilldesc(String billdesc) {
        this.billdesc = billdesc;
    }

    public String getEmpNameDesg() {
        return empNameDesg;
    }

    public void setEmpNameDesg(String empNameDesg) {
        this.empNameDesg = empNameDesg;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpDesg() {
        return empDesg;
    }

    public void setEmpDesg(String empDesg) {
        this.empDesg = empDesg;
    }

    public String getVchNo() {
        return vchNo;
    }

    public void setVchNo(String vchNo) {
        this.vchNo = vchNo;
    }

    public String getVchDate() {
        return vchDate;
    }

    public void setVchDate(String vchDate) {
        this.vchDate = vchDate;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public double getOriginalAmt() {
        return originalAmt;
    }

    public void setOriginalAmt(double originalAmt) {
        this.originalAmt = originalAmt;
    }

    public String getInstalmentRec() {
        return instalmentRec;
    }

    public void setInstalmentRec(String instalmentRec) {
        this.instalmentRec = instalmentRec;
    }

    public double getDecutedAmt() {
        return decutedAmt;
    }

    public void setDecutedAmt(double decutedAmt) {
        this.decutedAmt = decutedAmt;
    }

    public double getRecAmt() {
        return recAmt;
    }

    public void setRecAmt(double recAmt) {
        this.recAmt = recAmt;
    }

    public String getDrawingmonth() {
        return drawingmonth;
    }

    public void setDrawingmonth(String drawingmonth) {
        this.drawingmonth = drawingmonth;
    }

    public double getBalOutstanding() {
        return balOutstanding;
    }

    public void setBalOutstanding(double balOutstanding) {
        this.balOutstanding = balOutstanding;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public ArrayList getPiAdvRecList() {
        return piAdvRecList;
    }

    public void setPiAdvRecList(ArrayList piAdvRecList) {
        this.piAdvRecList = piAdvRecList;
    }

    public ArrayList getPiAdvRecInterestList() {
        return piAdvRecInterestList;
    }

    public void setPiAdvRecInterestList(ArrayList piAdvRecInterestList) {
        this.piAdvRecInterestList = piAdvRecInterestList;
    }

    public String getInstalmentNo() {
        return instalmentNo;
    }

    public void setInstalmentNo(String instalmentNo) {
        this.instalmentNo = instalmentNo;
    }

    public String getDeductionStdate() {
        return deductionStdate;
    }

    public void setDeductionStdate(String deductionStdate) {
        this.deductionStdate = deductionStdate;
    }

    public String getPagebreakLA() {
        return pagebreakLA;
    }

    public void setPagebreakLA(String pagebreakLA) {
        this.pagebreakLA = pagebreakLA;
    }

    public String getPageHeaderLA() {
        return pageHeaderLA;
    }

    public void setPageHeaderLA(String pageHeaderLA) {
        this.pageHeaderLA = pageHeaderLA;
    }

    
    
    
    
}
