package hrms.model.emppayrecord;

public class EmpPayRecordForm {
    
    private int payid ;
    private String not_type = null;
    private int not_id ;
    private String empid = null;
    private String wefDt = null;
    private String wefTime = null;
    private String payscale = null;
    private String payLevel=null;
    private String payCell=null;
    private String basic = null;
    private String gp = null;
    private String s_pay = null;
    private String p_pay = null;
    private String oth_pay = null;
    private String oth_desc = null;
    private String reasonpayfixation;
    private String ifRemuneration=null;
    
    private int refcorrectionid;
    
    public String getNot_type() {
        return not_type;
    }

    public void setNot_type(String not_type) {
        this.not_type = not_type;
    }

    

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getWefDt() {
        return wefDt;
    }

    public void setWefDt(String wefDt) {
        this.wefDt = wefDt;
    }

    public String getWefTime() {
        return wefTime;
    }

    public void setWefTime(String wefTime) {
        this.wefTime = wefTime;
    }

    public String getPayscale() {
        return payscale;
    }

    public void setPayscale(String payscale) {
        this.payscale = payscale;
    }

    public String getPayLevel() {
        return payLevel;
    }

    public void setPayLevel(String payLevel) {
        this.payLevel = payLevel;
    }

    public String getPayCell() {
        return payCell;
    }

    public void setPayCell(String payCell) {
        this.payCell = payCell;
    }

    public String getBasic() {
        return basic;
    }

    public void setBasic(String basic) {
        this.basic = basic;
    }

    public String getGp() {
        return gp;
    }

    public void setGp(String gp) {
        this.gp = gp;
    }

    public String getS_pay() {
        return s_pay;
    }

    public void setS_pay(String s_pay) {
        this.s_pay = s_pay;
    }

    public String getP_pay() {
        return p_pay;
    }

    public void setP_pay(String p_pay) {
        this.p_pay = p_pay;
    }

    public String getOth_pay() {
        return oth_pay;
    }

    public void setOth_pay(String oth_pay) {
        this.oth_pay = oth_pay;
    }

    public String getOth_desc() {
        return oth_desc;
    }

    public void setOth_desc(String oth_desc) {
        this.oth_desc = oth_desc;
    }

    

    public int getNot_id() {
        return not_id;
    }

    public void setNot_id(int not_id) {
        this.not_id = not_id;
    }

    public int getPayid() {
        return payid;
    }

    public void setPayid(int payid) {
        this.payid = payid;
    }

    public String getReasonpayfixation() {
        return reasonpayfixation;
    }

    public void setReasonpayfixation(String reasonpayfixation) {
        this.reasonpayfixation = reasonpayfixation;
    }

    public String getIfRemuneration() {
        return ifRemuneration;
    }

    public void setIfRemuneration(String ifRemuneration) {
        this.ifRemuneration = ifRemuneration;
    }

    public int getRefcorrectionid() {
        return refcorrectionid;
    }

    public void setRefcorrectionid(int refcorrectionid) {
        this.refcorrectionid = refcorrectionid;
    }
}
