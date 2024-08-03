/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.loan;

/**
 *
 * @author Manoj PC
 */
public class GroupLoanBean {
    private int slno;
    private String empname = null;
    private String empid = null;
    private String gpfno = null;
    private String orgloanamt = null;
    private String totalinstlno = null;
    private String instlamt = null;
    private String lastpaidinstlno = null;
    private String cumulativeamtpaid = null;
    private String chkNowDeduct = null;
    private String stoploan = null;
    private String loanId = null;

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getEmpid() {
        return empid;
    }

    public void setGpfno(String gpfno) {
        this.gpfno = gpfno;
    }

    public String getGpfno() {
        return gpfno;
    }

    public void setInstlamt(String instlamt) {
        this.instlamt = instlamt;
    }

    public String getInstlamt() {
        return instlamt;
    }

    public void setSlno(int slno) {
        this.slno = slno;
    }

    public int getSlno() {
        return slno;
    }

    public void setOrgloanamt(String orgloanamt) {
        this.orgloanamt = orgloanamt;
    }

    public String getOrgloanamt() {
        return orgloanamt;
    }

    public void setTotalinstlno(String totalinstlno) {
        this.totalinstlno = totalinstlno;
    }

    public String getTotalinstlno() {
        return totalinstlno;
    }

    public void setLastpaidinstlno(String lastpaidinstlno) {
        this.lastpaidinstlno = lastpaidinstlno;
    }

    public String getLastpaidinstlno() {
        return lastpaidinstlno;
    }

    public void setCumulativeamtpaid(String cumulativeamtpaid) {
        this.cumulativeamtpaid = cumulativeamtpaid;
    }

    public String getCumulativeamtpaid() {
        return cumulativeamtpaid;
    }

    public void setChkNowDeduct(String chkNowDeduct) {
        this.chkNowDeduct = chkNowDeduct;
    }

    public String getChkNowDeduct() {
        return chkNowDeduct;
    }

    public void setStoploan(String stoploan) {
        this.stoploan = stoploan;
    }

    public String getStoploan() {
        return stoploan;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }
}
