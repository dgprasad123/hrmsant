/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll;

import java.util.ArrayList;

/**
 *
 * @author Manas
 */
public class AnnualIncomeTax {
    private String accountnumber;
    private String empId;
    private String name;
    private String designation;
    private String pancard;
    private ArrayList annualIncomeTaxDetailList;

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPancard() {
        return pancard;
    }

    public void setPancard(String pancard) {
        this.pancard = pancard;
    }

    public ArrayList getAnnualIncomeTaxDetailList() {
        return annualIncomeTaxDetailList;
    }

    public void setAnnualIncomeTaxDetailList(ArrayList annualIncomeTaxDetailList) {
        this.annualIncomeTaxDetailList = annualIncomeTaxDetailList;
    }
    
    
}
