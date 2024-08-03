/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.misreport;

/**
 *
 * @author Madhusmita
 */
public class DeptWiseReportForm {
    private String deptName;
    private String deptCode;
    private int cntRegular;
    private int cntContractual;
    private int cntCont6Yr;
    private int cntWages;
    private int cntWorkcharged;
    private int cntlvlVExcadre;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public int getCntRegular() {
        return cntRegular;
    }

    public void setCntRegular(int cntRegular) {
        this.cntRegular = cntRegular;
    }

    public int getCntContractual() {
        return cntContractual;
    }

    public void setCntContractual(int cntContractual) {
        this.cntContractual = cntContractual;
    }

    public int getCntCont6Yr() {
        return cntCont6Yr;
    }

    public void setCntCont6Yr(int cntCont6Yr) {
        this.cntCont6Yr = cntCont6Yr;
    }

    public int getCntWages() {
        return cntWages;
    }

    public void setCntWages(int cntWages) {
        this.cntWages = cntWages;
    }

    public int getCntWorkcharged() {
        return cntWorkcharged;
    }

    public void setCntWorkcharged(int cntWorkcharged) {
        this.cntWorkcharged = cntWorkcharged;
    }

    public int getCntlvlVExcadre() {
        return cntlvlVExcadre;
    }

    public void setCntlvlVExcadre(int cntlvlVExcadre) {
        this.cntlvlVExcadre = cntlvlVExcadre;
    }
    
    
    
    
}
