package hrms.model.payroll.billbrowser;

import java.util.ArrayList;

public class MajorHeadAttribute {
    
    private String majorhead;
    private String nowDedn;
    private int totalAmt;
    private int ptotalAmt;
    private int itotalAmt;
    
    ArrayList vchPriList;
    ArrayList vchIntList;
    
    public String getMajorhead() {
        return majorhead;
    }

    public void setMajorhead(String majorhead) {
        this.majorhead = majorhead;
    }

    public String getNowDedn() {
        return nowDedn;
    }

    public void setNowDedn(String nowDedn) {
        this.nowDedn = nowDedn;
    }

    public int getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(int totalAmt) {
        this.totalAmt = totalAmt;
    }

    public int getPtotalAmt() {
        return ptotalAmt;
    }

    public void setPtotalAmt(int ptotalAmt) {
        this.ptotalAmt = ptotalAmt;
    }

    public int getItotalAmt() {
        return itotalAmt;
    }

    public void setItotalAmt(int itotalAmt) {
        this.itotalAmt = itotalAmt;
    }

    public ArrayList getVchPriList() {
        return vchPriList;
    }

    public void setVchPriList(ArrayList vchPriList) {
        this.vchPriList = vchPriList;
    }

    public ArrayList getVchIntList() {
        return vchIntList;
    }

    public void setVchIntList(ArrayList vchIntList) {
        this.vchIntList = vchIntList;
    }
}
