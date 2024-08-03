package hrms.model.parmast;

public class ParAbsenteeBean {
    
    private int pabid;
    private int hidparid;
    private String fromdate = null;
    private String todate = null;
    private String leavecause = null;
    private String leaveortrainingtype = null;
     private String absenceCauseOth;
    
    private String mode;
    private int hidpabid;
    private String fiscalyear;
    private String hidparfrmdt;
    private String hidpartodt;
    private int pageno;
    
    public int getPabid() {
        return pabid;
    }

    public void setPabid(int pabid) {
        this.pabid = pabid;
    }

    public int getHidparid() {
        return hidparid;
    }

    public void setHidparid(int hidparid) {
        this.hidparid = hidparid;
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public String getLeavecause() {
        return leavecause;
    }

    public void setLeavecause(String leavecause) {
        this.leavecause = leavecause;
    }

    public String getLeaveortrainingtype() {
        return leaveortrainingtype;
    }

    public void setLeaveortrainingtype(String leaveortrainingtype) {
        this.leaveortrainingtype = leaveortrainingtype;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getHidpabid() {
        return hidpabid;
    }

    public void setHidpabid(int hidpabid) {
        this.hidpabid = hidpabid;
    }

    public String getFiscalyear() {
        return fiscalyear;
    }

    public void setFiscalyear(String fiscalyear) {
        this.fiscalyear = fiscalyear;
    }

    public String getHidparfrmdt() {
        return hidparfrmdt;
    }

    public void setHidparfrmdt(String hidparfrmdt) {
        this.hidparfrmdt = hidparfrmdt;
    }

    public String getHidpartodt() {
        return hidpartodt;
    }

    public void setHidpartodt(String hidpartodt) {
        this.hidpartodt = hidpartodt;
    }

    public int getPageno() {
        return pageno;
    }

    public void setPageno(int pageno) {
        this.pageno = pageno;
    }

    public String getAbsenceCauseOth() {
        return absenceCauseOth;
    }

    public void setAbsenceCauseOth(String absenceCauseOth) {
        this.absenceCauseOth = absenceCauseOth;
    }
    
    
}
