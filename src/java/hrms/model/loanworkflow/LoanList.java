package hrms.model.loanworkflow;

public class LoanList {
    
    public String loanid = null;
    public String loanType = null;
    public String loanDate = null;
    public String purType=null;
    public String loanStatus=null;
    public int loanstatusid=0;
    public int taskid=0;
    private String designation = null;
    private String basicsalary = null;
    private String gpfno=null;
     private String EmpName = null;
   
    private String JobType = null;
    private String Empdob = null;

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public int getLoanstatusid() {
        return loanstatusid;
    }

    public void setLoanstatusid(int loanstatusid) {
        this.loanstatusid = loanstatusid;
    }
    
    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }
    
    

    public String getPurType() {
        return purType;
    }

    public void setPurType(String purType) {
        this.purType = purType;
    }
    
    public String getLoanid() {
        return loanid;
    }

    public void setLoanid(String loanid) {
        this.loanid = loanid;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getBasicsalary() {
        return basicsalary;
    }

    public void setBasicsalary(String basicsalary) {
        this.basicsalary = basicsalary;
    }

    public String getGpfno() {
        return gpfno;
    }

    public void setGpfno(String gpfno) {
        this.gpfno = gpfno;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String EmpName) {
        this.EmpName = EmpName;
    }

    public String getJobType() {
        return JobType;
    }

    public void setJobType(String JobType) {
        this.JobType = JobType;
    }

    public String getEmpdob() {
        return Empdob;
    }

    public void setEmpdob(String Empdob) {
        this.Empdob = Empdob;
    }

    
}
