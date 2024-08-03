/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.schedule;

/**
 *
 * @author Manas
 */
public class PrivateDeduction {

    private String loanaccno = null;
    private String bankifsccode = null;
    private String bankmicrcode = null;
    private String acctype;
    private int loanamt;

    public PrivateDeduction() {
    }

    public void setLoanaccno(String loanaccno) {
        this.loanaccno = loanaccno;
    }

    public String getLoanaccno() {
        return loanaccno;
    }

    public void setBankifsccode(String bankifsccode) {
        this.bankifsccode = bankifsccode;
    }

    public String getBankifsccode() {
        return bankifsccode;
    }

    public void setBankmicrcode(String bankmicrcode) {
        this.bankmicrcode = bankmicrcode;
    }

    public String getBankmicrcode() {
        return bankmicrcode;
    }

    public void setAcctype(String acctype) {
        this.acctype = acctype;
    }

    public String getAcctype() {
        return acctype;
    }

    public void setLoanamt(int loanamt) {
        this.loanamt = loanamt;
    }

    public int getLoanamt() {
        return loanamt;
    }
}
