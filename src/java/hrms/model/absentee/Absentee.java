package hrms.model.absentee;

import java.util.Date;

public class Absentee {
    private String fromDate = null;
    private String toDate = null;
    private int totaldays = 0;
    private String absid = null;
    private String notId = null;
    private String empId = null;
    private String entempId = null;
    private int sltyear = 0;
    private int sltmonth = 0;
    private Date entDate = null;

    public Date getEntDate() {
        return entDate;
    }

    public void setEntDate(Date entDate) {
        this.entDate = entDate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public int getTotaldays() {
        return totaldays;
    }

    public void setTotaldays(int totaldays) {
        this.totaldays = totaldays;
    }    

    public String getAbsid() {
        return absid;
    }

    public void setAbsid(String abssid) {
        this.absid = abssid;
    }

    public String getNotId() {
        return notId;
    }

    public void setNotId(String notId) {
        this.notId = notId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEntempId() {
        return entempId;
    }

    public void setEntempId(String entempId) {
        this.entempId = entempId;
    }

    public int getSltyear() {
        return sltyear;
    }

    public void setSltyear(int sltyear) {
        this.sltyear = sltyear;
    }

    public int getSltmonth() {
        return sltmonth;
    }

    public void setSltmonth(int sltmonth) {
        this.sltmonth = sltmonth;
    }

    
    
    
}
