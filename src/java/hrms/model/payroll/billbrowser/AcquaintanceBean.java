/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.billbrowser;

/**
 *
 * @author Manas Jena
 */
public class AcquaintanceBean {

    private String aqslno;
    private String empcode;
    private String empname;
    private String gpfaccno;
    private String curdesg;
    private String bankaccno;
    private String payscale;
    private int curbasic;
    private int monbasic;
    private int grossamt;
    private int dedamt;
    private int prvdedamt;
    private int aqday;
    private int payday;
    private String employeeDepStatus="";
    private String depcode="";
    private String emptype;
    
    
    

    public int getGrossamt() {
        return grossamt;
    }

    public void setGrossamt(int grossamt) {
        this.grossamt = grossamt;
    }

    public int getDedamt() {
        return dedamt;
    }

    public void setDedamt(int dedamt) {
        this.dedamt = dedamt;
    }

    public int getPrvdedamt() {
        return prvdedamt;
    }

    public void setPrvdedamt(int prvdedamt) {
        this.prvdedamt = prvdedamt;
    }        

    public String getAqslno() {
        return aqslno;
    }

    public void setAqslno(String aqslno) {
        this.aqslno = aqslno;
    }

    public String getEmpcode() {
        return empcode;
    }

    public void setEmpcode(String empcode) {
        this.empcode = empcode;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getGpfaccno() {
        return gpfaccno;
    }

    public void setGpfaccno(String gpfaccno) {
        this.gpfaccno = gpfaccno;
    }

    public String getCurdesg() {
        return curdesg;
    }

    public void setCurdesg(String curdesg) {
        this.curdesg = curdesg;
    }

    public String getBankaccno() {
        return bankaccno;
    }

    public void setBankaccno(String bankaccno) {
        this.bankaccno = bankaccno;
    }

    public String getPayscale() {
        return payscale;
    }

    public void setPayscale(String payscale) {
        this.payscale = payscale;
    }

    public int getCurbasic() {
        return curbasic;
    }

    public void setCurbasic(int curbasic) {
        this.curbasic = curbasic;
    }

    public int getMonbasic() {
        return monbasic;
    }

    public void setMonbasic(int monbasic) {
        this.monbasic = monbasic;
    }

    public int getAqday() {
        return aqday;
    }

    public void setAqday(int aqday) {
        this.aqday = aqday;
    }

    public int getPayday() {
        return payday;
    }

    public void setPayday(int payday) {
        this.payday = payday;
    }

    public String getEmployeeDepStatus() {
        return employeeDepStatus;
    }

    public void setEmployeeDepStatus(String employeeDepStatus) {
        this.employeeDepStatus = employeeDepStatus;
    }

    public String getDepcode() {
        return depcode;
    }

    public void setDepcode(String depcode) {
        this.depcode = depcode;
    }

    public String getEmptype() {
        return emptype;
    }

    public void setEmptype(String emptype) {
        this.emptype = emptype;
    }
    
    
}
