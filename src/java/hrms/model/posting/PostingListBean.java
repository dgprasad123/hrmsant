package hrms.model.posting;

public class PostingListBean {
    
    private String doe;
    private String ordno;
    private String ordDate;
    private String hnotid;
    private String notType;
    
    private String isValidated;
    
    public String getDoe() {
        return doe;
    }

    public void setDoe(String doe) {
        this.doe = doe;
    }

    public String getOrdno() {
        return ordno;
    }

    public void setOrdno(String ordno) {
        this.ordno = ordno;
    }

    public String getOrdDate() {
        return ordDate;
    }

    public void setOrdDate(String ordDate) {
        this.ordDate = ordDate;
    }

    public String getHnotid() {
        return hnotid;
    }

    public void setHnotid(String hnotid) {
        this.hnotid = hnotid;
    }

    public String getNotType() {
        return notType;
    }

    public void setNotType(String notType) {
        this.notType = notType;
    }

    public String getIsValidated() {
        return isValidated;
    }

    public void setIsValidated(String isValidated) {
        this.isValidated = isValidated;
    }
    
}
