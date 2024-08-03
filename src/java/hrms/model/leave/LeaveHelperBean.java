
package hrms.model.leave;
import java.util.Date;

public class LeaveHelperBean {
    private String tolName = null;
    private Date initiatedOn = null;
    private Date applyFromDate = null;
    private Date applyToDate = null;
    private Date approveFromDate = null;
     private Date approveToDate = null;
    private String sanctionDays = null;
    private String submittedAuthority = null;

    public Date getApplyFromDate() {
        return applyFromDate;
    }

    public void setApplyFromDate(Date applyFromDate) {
        this.applyFromDate = applyFromDate;
    }

    public Date getApplyToDate() {
        return applyToDate;
    }

    public void setApplyToDate(Date applyToDate) {
        this.applyToDate = applyToDate;
    }

    public Date getApproveFromDate() {
        return approveFromDate;
    }

    public void setApproveFromDate(Date approveFromDate) {
        this.approveFromDate = approveFromDate;
    }

    public Date getApproveToDate() {
        return approveToDate;
    }

    public void setApproveToDate(Date approveToDate) {
        this.approveToDate = approveToDate;
    }

    public String getSubmittedAuthority() {
        return submittedAuthority;
    }

    public void setSubmittedAuthority(String submittedAuthority) {
        this.submittedAuthority = submittedAuthority;
    }

    public String getSanctionDays() {
        return sanctionDays;
    }

    public void setSanctionDays(String sanctionDays) {
        this.sanctionDays = sanctionDays;
    }

   

    public Date getInitiatedOn() {
        return initiatedOn;
    }

    public void setInitiatedOn(Date initiatedOn) {
        this.initiatedOn = initiatedOn;
    }

    public String getTolName() {
        return tolName;
    }

    public void setTolName(String tolName) {
        this.tolName = tolName;
    }

}
