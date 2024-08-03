/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.payroll.schedule;

import java.util.List;

public class PtScheduleBean extends ScheduleHelper{
    
    public String empTaxOnProffesion;
    public String totalTax;
    public String totalGross;
    public String empGrossSal;
    private List PTScheduleDtls = null;
    public String monthYear;
    private String deptName;
    private String officeName;
    private String ddoDegn;
    private String billNo;
    private String billDesc;
    private int basicSal=0;
    private String pagebreakPT = null;
    private String pageHeaderPT = null;
    
    public int getBasicSal() {
        return basicSal;
    }

    public void setBasicSal(int basicSal) {
        this.basicSal = basicSal;
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

    public List getPTScheduleDtls() {
        return PTScheduleDtls;
    }

    public void setPTScheduleDtls(List PTScheduleDtls) {
        this.PTScheduleDtls = PTScheduleDtls;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getDdoDegn() {
        return ddoDegn;
    }

    public void setDdoDegn(String ddoDegn) {
        this.ddoDegn = ddoDegn;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillDesc() {
        return billDesc;
    }

    public void setBillDesc(String billDesc) {
        this.billDesc = billDesc;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public String getPagebreakPT() {
        return pagebreakPT;
    }

    public void setPagebreakPT(String pagebreakPT) {
        this.pagebreakPT = pagebreakPT;
    }

    public String getPageHeaderPT() {
        return pageHeaderPT;
    }

    public void setPageHeaderPT(String pageHeaderPT) {
        this.pageHeaderPT = pageHeaderPT;
    }
    
    
    
}
