package hrms.model.additionalCharge;

public class AdditionalChargeList {

    private String notId = null;
    private String notType = null;
    private String doe = null;
    private String ordno = null;
    private String ordDate = null;
    private String ifadcharge=null;
    private String addlspc;

    private String isValidated;

    public String getNotId() {
        return notId;
    }

    public void setNotId(String notId) {
        this.notId = notId;
    }

    public String getNotType() {
        return notType;
    }

    public void setNotType(String notType) {
        this.notType = notType;
    }

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

    public String getIsValidated() {
        return isValidated;
    }

    public void setIsValidated(String isValidated) {
        this.isValidated = isValidated;
    }

    public String getIfadcharge() {
        return ifadcharge;
    }

    public void setIfadcharge(String ifadcharge) {
        this.ifadcharge = ifadcharge;
    }

    public String getAddlspc() {
        return addlspc;
    }

    public void setAddlspc(String addlspc) {
        this.addlspc = addlspc;
    }
    
}
