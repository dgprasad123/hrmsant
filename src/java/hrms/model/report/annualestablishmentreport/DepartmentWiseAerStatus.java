/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.report.annualestablishmentreport;

/**
 *
 * @author lenovo pc
 */
public class DepartmentWiseAerStatus {

    String deptCode = null;
    String deptName = null;
    int noAerSubmitted = 0;
    int noOfAerAproved = 0;
    int noOfDDO = 0;
    String offName = null;
    String finYear = null;
    private int aerId;

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public int getNoAerSubmitted() {
        return noAerSubmitted;
    }

    public void setNoAerSubmitted(int noAerSubmitted) {
        this.noAerSubmitted = noAerSubmitted;
    }

    public int getNoOfAerAproved() {
        return noOfAerAproved;
    }

    public void setNoOfAerAproved(int noOfAerAproved) {
        this.noOfAerAproved = noOfAerAproved;
    }

    public int getNoOfDDO() {
        return noOfDDO;
    }

    public void setNoOfDDO(int noOfDDO) {
        this.noOfDDO = noOfDDO;
    }

    public String getOffName() {
        return offName;
    }

    public void setOffName(String offName) {
        this.offName = offName;
    }

    public String getFinYear() {
        return finYear;
    }

    public void setFinYear(String finYear) {
        this.finYear = finYear;
    }

    public int getAerId() {
        return aerId;
    }

    public void setAerId(int aerId) {
        this.aerId = aerId;
    }

}
