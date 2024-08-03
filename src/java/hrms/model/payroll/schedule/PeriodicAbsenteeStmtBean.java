/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.payroll.schedule;

public class PeriodicAbsenteeStmtBean extends ScheduleHelper{
    
    private String nameofAbsentee="";
    private String rateofPay="";
    private String subPost="";
    private String subPay="";
    private String offPay="";
    private String absKind="";
    private String period="";
    private String fromTime="";
    private String totime="";

    public String getNameofAbsentee() {
        return nameofAbsentee;
    }

    public void setNameofAbsentee(String nameofAbsentee) {
        this.nameofAbsentee = nameofAbsentee;
    }

    public String getRateofPay() {
        return rateofPay;
    }

    public void setRateofPay(String rateofPay) {
        this.rateofPay = rateofPay;
    }

    public String getSubPost() {
        return subPost;
    }

    public void setSubPost(String subPost) {
        this.subPost = subPost;
    }

    public String getSubPay() {
        return subPay;
    }

    public void setSubPay(String subPay) {
        this.subPay = subPay;
    }

    public String getOffPay() {
        return offPay;
    }

    public void setOffPay(String offPay) {
        this.offPay = offPay;
    }

    public String getAbsKind() {
        return absKind;
    }

    public void setAbsKind(String absKind) {
        this.absKind = absKind;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getTotime() {
        return totime;
    }

    public void setTotime(String totime) {
        this.totime = totime;
    }
    
    
    
}
