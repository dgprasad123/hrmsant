package hrms.model.parmast;

public class PARReportBean {
    
    private String deptname = null;
    private int noofemp;
    private int parapplied;
    private int parappliedwithoutspc;
    private int reportingpending;
    private int reviewingpending;
    private int acceptingpending;
    private int completed;

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public int getNoofemp() {
        return noofemp;
    }

    public void setNoofemp(int noofemp) {
        this.noofemp = noofemp;
    }

    public int getParapplied() {
        return parapplied;
    }

    public void setParapplied(int parapplied) {
        this.parapplied = parapplied;
    }

    public int getParappliedwithoutspc() {
        return parappliedwithoutspc;
    }

    public void setParappliedwithoutspc(int parappliedwithoutspc) {
        this.parappliedwithoutspc = parappliedwithoutspc;
    }

    public int getReportingpending() {
        return reportingpending;
    }

    public void setReportingpending(int reportingpending) {
        this.reportingpending = reportingpending;
    }

    public int getReviewingpending() {
        return reviewingpending;
    }

    public void setReviewingpending(int reviewingpending) {
        this.reviewingpending = reviewingpending;
    }

    public int getAcceptingpending() {
        return acceptingpending;
    }

    public void setAcceptingpending(int acceptingpending) {
        this.acceptingpending = acceptingpending;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }
    
    
}
