/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.incrementsanction;

/**
 *
 * @author Surendra
 */
public class ContractualEmployeeIncrementForm {
    
    private int incrId=0;
    private String empId="";
    private String ordno="";
    private String orddate="";
    private int curBasic=0;
    private int newBasic=0;
    private String offCode="";

    public int getIncrId() {
        return incrId;
    }

    public void setIncrId(int incrId) {
        this.incrId = incrId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getOrdno() {
        return ordno;
    }

    public void setOrdno(String ordno) {
        this.ordno = ordno;
    }

    public String getOrddate() {
        return orddate;
    }

    public void setOrddate(String orddate) {
        this.orddate = orddate;
    }

    public int getCurBasic() {
        return curBasic;
    }

    public void setCurBasic(int curBasic) {
        this.curBasic = curBasic;
    }

    public int getNewBasic() {
        return newBasic;
    }

    public void setNewBasic(int newBasic) {
        this.newBasic = newBasic;
    }

    public String getOffCode() {
        return offCode;
    }

    public void setOffCode(String offCode) {
        this.offCode = offCode;
    }
    
    
    
}
