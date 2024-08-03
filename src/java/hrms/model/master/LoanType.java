/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.master;



/**
 *
 * @author lenovo pc
 */
public class LoanType {
    public String loanType=null;
    public String loanName=null;
    private String haveInt=null;

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getHaveInt() {
        return haveInt;
    }

    public void setHaveInt(String haveInt) {
        this.haveInt = haveInt;
    }
    
   
}
