package hrms.model.payroll.thirdschedule;

import java.util.ArrayList;
import java.util.Date;

public class ThirdScheduleBean {
    
    private String empId = null;
    private String empname = null;
    private String gpfno = null;
    private String designation = null;
    private String payscale = null;
    private int gp = 0;
    
    private String incrDt = null;
    private int revisedbasic = 0;
    private String level;
    private int cell = 0;
    private int pp = 0;
    
    private String isApproved;
    private String isCheckingAuthSubmitted;
    private String chkAuthName;
    
    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }
    
    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getGpfno() {
        return gpfno;
    }

    public void setGpfno(String gpfno) {
        this.gpfno = gpfno;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPayscale() {
        return payscale;
    }

    public void setPayscale(String payscale) {
        this.payscale = payscale;
    }

    public int getGp() {
        return gp;
    }

    public void setGp(int gp) {
        this.gp = gp;
    }

    public String getIncrDt() {
        return incrDt;
    }

    public void setIncrDt(String incrDt) {
        this.incrDt = incrDt;
    }

    public int getRevisedbasic() {
        return revisedbasic;
    }

    public void setRevisedbasic(int revisedbasic) {
        this.revisedbasic = revisedbasic;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    

    public int getCell() {
        return cell;
    }

    public void setCell(int cell) {
        this.cell = cell;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public String getIsCheckingAuthSubmitted() {
        return isCheckingAuthSubmitted;
    }

    public void setIsCheckingAuthSubmitted(String isCheckingAuthSubmitted) {
        this.isCheckingAuthSubmitted = isCheckingAuthSubmitted;
    }

    public String getChkAuthName() {
        return chkAuthName;
    }

    public void setChkAuthName(String chkAuthName) {
        this.chkAuthName = chkAuthName;
    }

    public int getPp() {
        return pp;
    }

    public void setPp(int pp) {
        this.pp = pp;
    }

    
}
