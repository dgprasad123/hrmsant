/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.payroll.schedule;

public class ExcessPayBean extends ScheduleHelper{
    
 // HEADER PROPERTIES   
    private String monthYear;
    private String billNo;
    private String deptName;
    private String offName;
    private String ddoDegn;
    private String billDesc;
    
 // EMP LIST PROPERTIES   
    private String empName;
    private String empDegn;
    private String empTaxOnProffesion;
    private String totalTax;
    private String totalGross;
    private String empGrossSal;
    
    private String pagebreakEP = null;
    private String pageHeaderEP = null;
    
    
    
    
    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getOffName() {
        return offName;
    }

    public void setOffName(String offName) {
        this.offName = offName;
    }

    public String getDdoDegn() {
        return ddoDegn;
    }

    public void setDdoDegn(String ddoDegn) {
        this.ddoDegn = ddoDegn;
    }

    public String getBillDesc() {
        return billDesc;
    }

    public void setBillDesc(String billDesc) {
        this.billDesc = billDesc;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpDegn() {
        return empDegn;
    }

    public void setEmpDegn(String empDegn) {
        this.empDegn = empDegn;
    }

    public String getEmpTaxOnProffesion() {
        return empTaxOnProffesion;
    }

    public void setEmpTaxOnProffesion(String empTaxOnProffesion) {
        this.empTaxOnProffesion = empTaxOnProffesion;
    }

    public String getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(String totalTax) {
        this.totalTax = totalTax;
    }

    public String getTotalGross() {
        return totalGross;
    }

    public void setTotalGross(String totalGross) {
        this.totalGross = totalGross;
    }

    public String getEmpGrossSal() {
        return empGrossSal;
    }

    public void setEmpGrossSal(String empGrossSal) {
        this.empGrossSal = empGrossSal;
    }

    public String getPagebreakEP() {
        return pagebreakEP;
    }

    public void setPagebreakEP(String pagebreakEP) {
        this.pagebreakEP = pagebreakEP;
    }

    public String getPageHeaderEP() {
        return pageHeaderEP;
    }

    public void setPageHeaderEP(String pageHeaderEP) {
        this.pageHeaderEP = pageHeaderEP;
    }
    
    
    
    
}
