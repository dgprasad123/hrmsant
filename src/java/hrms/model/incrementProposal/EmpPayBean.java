/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.incrementProposal;

import java.io.Serializable;

/**
 *
 * @author Manoj PC
 */

public class EmpPayBean implements Serializable{
    private String empId;
    private String aqMonth;
    private String aqYear;
    private String salaryDate;
    private String grossAmount;
    private String deduction;
    private String pvtDeduction;
    private String netAmount;

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getAqMonth() {
        return aqMonth;
    }

    public void setAqMonth(String aqMonth) {
        this.aqMonth = aqMonth;
    }

    public String getAqYear() {
        return aqYear;
    }

    public void setAqYear(String aqYear) {
        this.aqYear = aqYear;
    }

    public String getSalaryDate() {
        return salaryDate;
    }

    public void setSalaryDate(String salaryDate) {
        this.salaryDate = salaryDate;
    }

    public String getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(String grossAmount) {
        this.grossAmount = grossAmount;
    }

    public String getDeduction() {
        return deduction;
    }

    public void setDeduction(String deduction) {
        this.deduction = deduction;
    }

    public String getPvtDeduction() {
        return pvtDeduction;
    }

    public void setPvtDeduction(String pvtDeduction) {
        this.pvtDeduction = pvtDeduction;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }
    
}
