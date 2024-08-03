package hrms.model.parmast;

public class Parauthorityhelperbean {
    
    public int parId;
    public int taskId;
    public int authid;
    public String authorityempid;
    public String authorityspc;
    public String authorityname;
    public String authorityspn;
    public String fromdt;
    public String todt;
    
    public String isreportingCompleted;
    public String isreviewingcompleted;
     public String isacceptingcompleted;

    public String isPendingReportingAuthority = "N";
    public String isPendingReviewingAuthority = "N";
    public String isPendingAcceptingAuthority = "N";

    public int getParId() {
        return parId;
    }

    public void setParId(int parId) {
        this.parId = parId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getAuthid() {
        return authid;
    }

    public void setAuthid(int authid) {
        this.authid = authid;
    }
    
     public String getAuthorityempid() {
        return authorityempid;
    }

    public void setAuthorityempid(String authorityempid) {
        this.authorityempid = authorityempid;
    }

    public String getAuthorityspc() {
        return authorityspc;
    }

    public void setAuthorityspc(String authorityspc) {
        this.authorityspc = authorityspc;
    }

    public String getAuthorityname() {
        return authorityname;
    }

    public void setAuthorityname(String authorityname) {
        this.authorityname = authorityname;
    }

    public String getAuthorityspn() {
        return authorityspn;
    }

    public void setAuthorityspn(String authorityspn) {
        this.authorityspn = authorityspn;
    }

    public String getFromdt() {
        return fromdt;
    }

    public void setFromdt(String fromdt) {
        this.fromdt = fromdt;
    }

    public String getTodt() {
        return todt;
    }

    public void setTodt(String todt) {
        this.todt = todt;
    }

    public String getIsreportingCompleted() {
        return isreportingCompleted;
    }

    public void setIsreportingCompleted(String isreportingCompleted) {
        this.isreportingCompleted = isreportingCompleted;
    }

    public String getIsreviewingcompleted() {
        return isreviewingcompleted;
    }

    public void setIsreviewingcompleted(String isreviewingcompleted) {
        this.isreviewingcompleted = isreviewingcompleted;
    }

    public String getIsacceptingcompleted() {
        return isacceptingcompleted;
    }

    public void setIsacceptingcompleted(String isacceptingcompleted) {
        this.isacceptingcompleted = isacceptingcompleted;
    }
    
    

    public String getIsPendingReportingAuthority() {
        return isPendingReportingAuthority;
    }

    public void setIsPendingReportingAuthority(String isPendingReportingAuthority) {
        this.isPendingReportingAuthority = isPendingReportingAuthority;
    }

    public String getIsPendingReviewingAuthority() {
        return isPendingReviewingAuthority;
    }

    public void setIsPendingReviewingAuthority(String isPendingReviewingAuthority) {
        this.isPendingReviewingAuthority = isPendingReviewingAuthority;
    }

    public String getIsPendingAcceptingAuthority() {
        return isPendingAcceptingAuthority;
    }

    public void setIsPendingAcceptingAuthority(String isPendingAcceptingAuthority) {
        this.isPendingAcceptingAuthority = isPendingAcceptingAuthority;
    }
    
    
}
