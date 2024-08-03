/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.Budget;

/**
 *
 * @author Madhusmita
 */
public class BudgetForm {
    private String sltDeptCode=null;
    private String deptCode=null;
    private String deptName=null;
    private String sltMonth=null;
    private String sltYear=null;

    public String getSltDeptCode() {
        return sltDeptCode;
    }

    public void setSltDeptCode(String sltDeptCode) {
        this.sltDeptCode = sltDeptCode;
    }

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

   
    public String getSltMonth() {
        return sltMonth;
    }

    public void setSltMonth(String sltMonth) {
        this.sltMonth = sltMonth;
    }

    public String getSltYear() {
        return sltYear;
    }

    public void setSltYear(String sltYear) {
        this.sltYear = sltYear;
    }
    
    
    
    
}
