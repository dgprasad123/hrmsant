/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.IncomeTax;

/**
 *
 * @author Manoj PC
 */
public class IncomeTaxList {
    private String officeName = null;
    private String officeCode = null;
    private String aqslNos = null;
    private String totalEmployees = null;
    private String totalAmount = null;

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public String getAqslNos() {
        return aqslNos;
    }

    public void setAqslNos(String aqslNos) {
        this.aqslNos = aqslNos;
    }

    public String getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(String totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
    
}
