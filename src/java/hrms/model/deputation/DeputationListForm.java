package hrms.model.deputation;

public class DeputationListForm {
    
    private String deptId = null;
    private String notId = null;
    private String doe = null;
    private String notOrdNo = null;
    private String notOrdDt = null;
    private String notType = null;
    
    private String postingOffice;
    private String postingPost;
    private String periodFrom;
    private String periodTo;
    
    private String relieveId;
    
    private String isValidated;
    
    public String getDoe() {
        return doe;
    }

    public void setDoe(String doe) {
        this.doe = doe;
    }

    public String getNotOrdNo() {
        return notOrdNo;
    }

    public void setNotOrdNo(String notOrdNo) {
        this.notOrdNo = notOrdNo;
    }

    public String getNotOrdDt() {
        return notOrdDt;
    }

    public void setNotOrdDt(String notOrdDt) {
        this.notOrdDt = notOrdDt;
    }

    public String getNotType() {
        return notType;
    }

    public void setNotType(String notType) {
        this.notType = notType;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getNotId() {
        return notId;
    }

    public void setNotId(String notId) {
        this.notId = notId;
    }

    public String getPostingOffice() {
        return postingOffice;
    }

    public void setPostingOffice(String postingOffice) {
        this.postingOffice = postingOffice;
    }

    public String getPostingPost() {
        return postingPost;
    }

    public void setPostingPost(String postingPost) {
        this.postingPost = postingPost;
    }

    public String getPeriodFrom() {
        return periodFrom;
    }

    public void setPeriodFrom(String periodFrom) {
        this.periodFrom = periodFrom;
    }

    public String getPeriodTo() {
        return periodTo;
    }

    public void setPeriodTo(String periodTo) {
        this.periodTo = periodTo;
    }

    public String getRelieveId() {
        return relieveId;
    }

    public void setRelieveId(String relieveId) {
        this.relieveId = relieveId;
    }

    public String getIsValidated() {
        return isValidated;
    }

    public void setIsValidated(String isValidated) {
        this.isValidated = isValidated;
    }
}
