package hrms.model.parmast;

import hrms.model.master.LeaveTypeBean;
import hrms.model.master.Training;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ParAbsentee {
    
    private int pabid;
    
    private Date fromdate;
    
    private Date todate;
    
    private ParLeaveCause parleavecause;
    
    private LeaveTypeBean leave;
    
    private Training training;
    
    private String mode;
    
    private int hidparid;
    
    private int hidpabid;
    
    private String fiscalyear;
    
    private String hidparfrmdt;
    
    private String hidpartodt;
    
    private String hidleaveortrainingtype;
    
    public int getPabid() {
        return pabid;
    }

    public void setPabid(int pabid) {
        this.pabid = pabid;
    }

    public ParLeaveCause getParleavecause() {
        return parleavecause;
    }

    public void setParleavecause(ParLeaveCause parleavecause) {
        this.parleavecause = parleavecause;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Date getFromdate() {
        return fromdate;
    }

    public void setFromdate(Date fromdate) {
        this.fromdate = fromdate;
    }

    public Date getTodate() {
        return todate;
    }

    public void setTodate(Date todate) {
        this.todate = todate;
    }

    public int getHidparid() {
        return hidparid;
    }

    public void setHidparid(int hidparid) {
        this.hidparid = hidparid;
    }

    public LeaveTypeBean getLeave() {
        return leave;
    }

    public void setLeave(LeaveTypeBean leave) {
        this.leave = leave;
    }

    public int getHidpabid() {
        return hidpabid;
    }

    public String getFiscalyear() {
        return fiscalyear;
    }

    public void setFiscalyear(String fiscalyear) {
        this.fiscalyear = fiscalyear;
    }

    public void setHidpabid(int hidpabid) {
        this.hidpabid = hidpabid;
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

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    public String getHidleaveortrainingtype() {
        return hidleaveortrainingtype;
    }

    public void setHidleaveortrainingtype(String hidleaveortrainingtype) {
        this.hidleaveortrainingtype = hidleaveortrainingtype;
    }
}
