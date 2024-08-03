/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.updateleave;

import java.io.Serializable;

public class UpdateLeave implements Serializable {

    private String offCode;
    private String cldays;
    private String year;
    private String month;
    private String postCode;
    private String empid;
    private String empName;
    private String totalCl;
    private String totalClAvail;
    private String totalEl;
    private String totalElAvail;
    private String tolId;
    private String totLateInpunch;
    private String totAbsentCurMonth;
    private String totMissingOutPunch;
    private String totBalanceAsOnDate;
    private String workingDate;
    private String workingDay;
    private String inpunchTime;
    private String outpunchTime;
    private String dailyWorkingHour;
    private String curDate;
    private String biometricId;
    private String sltmonth;
    private String sltyear;
    private String totalBalance;
    private String totalAvailable;
    private String leaveType;
    private String totDaysForThreeDaysLate;

    public String getTotDaysForThreeDaysLate() {
        return totDaysForThreeDaysLate;
    }

    public void setTotDaysForThreeDaysLate(String totDaysForThreeDaysLate) {
        this.totDaysForThreeDaysLate = totDaysForThreeDaysLate;
    }
    
    

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }


    public String getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(String totalBalance) {
        this.totalBalance = totalBalance;
    }

    public String getTotalAvailable() {
        return totalAvailable;
    }

    public void setTotalAvailable(String totalAvailable) {
        this.totalAvailable = totalAvailable;
    }

    public String getSltyear() {
        return sltyear;
    }

    public void setSltyear(String sltyear) {
        this.sltyear = sltyear;
    }

    public String getSltmonth() {
        return sltmonth;
    }

    public void setSltmonth(String sltmonth) {
        this.sltmonth = sltmonth;
    }

    public String getBiometricId() {
        return biometricId;
    }

    public void setBiometricId(String biometricId) {
        this.biometricId = biometricId;
    }

    public String getCurDate() {
        return curDate;
    }

    public void setCurDate(String curDate) {
        this.curDate = curDate;
    }

    public String getWorkingDate() {
        return workingDate;
    }

    public void setWorkingDate(String workingDate) {
        this.workingDate = workingDate;
    }

    public String getWorkingDay() {
        return workingDay;
    }

    public void setWorkingDay(String workingDay) {
        this.workingDay = workingDay;
    }

    public String getInpunchTime() {
        return inpunchTime;
    }

    public void setInpunchTime(String inpunchTime) {
        this.inpunchTime = inpunchTime;
    }

    public String getOutpunchTime() {
        return outpunchTime;
    }

    public void setOutpunchTime(String outpunchTime) {
        this.outpunchTime = outpunchTime;
    }

    public String getDailyWorkingHour() {
        return dailyWorkingHour;
    }

    public void setDailyWorkingHour(String dailyWorkingHour) {
        this.dailyWorkingHour = dailyWorkingHour;
    }

    public String getTotLateInpunch() {
        return totLateInpunch;
    }

    public void setTotLateInpunch(String totLateInpunch) {
        this.totLateInpunch = totLateInpunch;
    }

    public String getTotAbsentCurMonth() {
        return totAbsentCurMonth;
    }

    public void setTotAbsentCurMonth(String totAbsentCurMonth) {
        this.totAbsentCurMonth = totAbsentCurMonth;
    }

    public String getTotMissingOutPunch() {
        return totMissingOutPunch;
    }

    public void setTotMissingOutPunch(String totMissingOutPunch) {
        this.totMissingOutPunch = totMissingOutPunch;
    }

    public String getTotBalanceAsOnDate() {
        return totBalanceAsOnDate;
    }

    public void setTotBalanceAsOnDate(String totBalanceAsOnDate) {
        this.totBalanceAsOnDate = totBalanceAsOnDate;
    }

    public String getTolId() {
        return tolId;
    }

    public void setTolId(String tolId) {
        this.tolId = tolId;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getTotalCl() {
        return totalCl;
    }

    public void setTotalCl(String totalCl) {
        this.totalCl = totalCl;
    }

    public String getTotalClAvail() {
        return totalClAvail;
    }

    public void setTotalClAvail(String totalClAvail) {
        this.totalClAvail = totalClAvail;
    }

    public String getTotalEl() {
        return totalEl;
    }

    public void setTotalEl(String totalEl) {
        this.totalEl = totalEl;
    }

    public String getTotalElAvail() {
        return totalElAvail;
    }

    public void setTotalElAvail(String totalElAvail) {
        this.totalElAvail = totalElAvail;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getCldays() {
        return cldays;
    }

    public void setCldays(String cldays) {
        this.cldays = cldays;
    }

    public String getOffCode() {
        return offCode;
    }

    public void setOffCode(String offCode) {
        this.offCode = offCode;
    }

}
