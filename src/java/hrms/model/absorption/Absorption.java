package hrms.model.absorption;

public class Absorption {
      private String notId=null;
    private String notType=null;
    private String doe=null;
    private String ordno=null;
    private String ordDate=null;
    private String hCadId=null;
    private String hpayid=null;

    public String gethCadId() {
        return hCadId;
    }

    public void sethCadId(String hCadId) {
        this.hCadId = hCadId;
    }

    public String getHpayid() {
        return hpayid;
    }

    public void setHpayid(String hpayid) {
        this.hpayid = hpayid;
    }

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
    
    
}
