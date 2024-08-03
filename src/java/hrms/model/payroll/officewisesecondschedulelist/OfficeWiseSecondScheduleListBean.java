package hrms.model.payroll.officewisesecondschedulelist;

public class OfficeWiseSecondScheduleListBean {

    private String empid = null;
    private String gpfno = null;
    private String empname = null;
    private String post = null;
    private String payscale = null;
    private int gp = 0;

    private String optionChosen = null;
    private String submittedAuth = null;

    private String sectionId = null;

    private String enteredDate = null;
    
    private String pendingAt;
    private String pendingAuthName;
    
    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getGpfno() {
        return gpfno;
    }

    public void setGpfno(String gpfno) {
        this.gpfno = gpfno;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getOptionChosen() {
        return optionChosen;
    }

    public void setOptionChosen(String optionChosen) {
        this.optionChosen = optionChosen;
    }

    public String getPayscale() {
        return payscale;
    }

    public void setPayscale(String payscale) {
        this.payscale = payscale;
    }

    public int getGp() {
        return gp;
    }

    public void setGp(int gp) {
        this.gp = gp;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSubmittedAuth() {
        return submittedAuth;
    }

    public void setSubmittedAuth(String submittedAuth) {
        this.submittedAuth = submittedAuth;
    }

    public String getEnteredDate() {
        return enteredDate;
    }

    public void setEnteredDate(String enteredDate) {
        this.enteredDate = enteredDate;
    }

    public String getPendingAt() {
        return pendingAt;
    }

    public void setPendingAt(String pendingAt) {
        this.pendingAt = pendingAt;
    }

    public String getPendingAuthName() {
        return pendingAuthName;
    }

    public void setPendingAuthName(String pendingAuthName) {
        this.pendingAuthName = pendingAuthName;
    }
}
