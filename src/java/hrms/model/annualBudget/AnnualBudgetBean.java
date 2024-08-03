package hrms.model.annualBudget;

public class AnnualBudgetBean {
    
    private String budgetid;
    private String createdDate;
    private String fy;
    private String status;
    private String isLocked;
    private String lockdate;
    
    private String isSubmitted;
    private String submittedDate;
    
    private String isReverted;
    private String revertDate;
    private String revertReason;
    
    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getFy() {
        return fy;
    }

    public void setFy(String fy) {
        this.fy = fy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLockdate() {
        return lockdate;
    }

    public void setLockdate(String lockdate) {
        this.lockdate = lockdate;
    }

    public String getBudgetid() {
        return budgetid;
    }

    public void setBudgetid(String budgetid) {
        this.budgetid = budgetid;
    }

    public String getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(String isLocked) {
        this.isLocked = isLocked;
    }

    public String getIsSubmitted() {
        return isSubmitted;
    }

    public void setIsSubmitted(String isSubmitted) {
        this.isSubmitted = isSubmitted;
    }

    public String getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(String submittedDate) {
        this.submittedDate = submittedDate;
    }

    public String getIsReverted() {
        return isReverted;
    }

    public void setIsReverted(String isReverted) {
        this.isReverted = isReverted;
    }

    public String getRevertDate() {
        return revertDate;
    }

    public void setRevertDate(String revertDate) {
        this.revertDate = revertDate;
    }

    public String getRevertReason() {
        return revertReason;
    }

    public void setRevertReason(String revertReason) {
        this.revertReason = revertReason;
    }
}
