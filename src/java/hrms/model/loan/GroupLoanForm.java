/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.loan;

import java.util.ArrayList;

/**
 *
 * @author Manoj PC
 */
public class GroupLoanForm {
        private String offCode = null;
    private String sltLoanlist = null;
    private String sltbillname = null;
    private String hidselectedemp = null;
    private String hidselectedempforUpdt = null;
    private String empid = null;
    
    private ArrayList loanlist = null;
    private ArrayList billnamelist = null;
    private ArrayList empList = null;
    
    private String[] chkemp = null;
    private String chkupdtdemp = null;
    private String chkNowDeduct = null;
    private String orgloanamt = null;
    private String[] totalinstlno = null;
    private String[] instlamt = null;
    private String[] lastpaidinstlno = null;
    private String[] cumulativeamtpaid = null;
    
    private String[] chkStopStart = null;
    private String hidselectedemptoStop = null;
    private String hidselectedemptoStart = null;
    
    public void setLoanlist(ArrayList loanlist) {
        this.loanlist = loanlist;
    }

    public ArrayList getLoanlist() {
        return loanlist;
    }

    public void setSltLoanlist(String sltLoanlist) {
        this.sltLoanlist = sltLoanlist;
    }

    public String getSltLoanlist() {
        return sltLoanlist;
    }

    public void setEmpList(ArrayList empList) {
        this.empList = empList;
    }

    public ArrayList getEmpList() {
        return empList;
    }

    public void setOffCode(String offCode) {
        this.offCode = offCode;
    }

    public String getOffCode() {
        return offCode;
    }

    public void setChkemp(String[] chkemp) {
        this.chkemp = chkemp;
    }

    public String[] getChkemp() {
        return chkemp;
    }

    public void setSltbillname(String sltbillname) {
        this.sltbillname = sltbillname;
    }

    public String getSltbillname() {
        return sltbillname;
    }

    public void setBillnamelist(ArrayList billnamelist) {
        this.billnamelist = billnamelist;
    }

    public ArrayList getBillnamelist() {
        return billnamelist;
    }

    public void setOrgloanamt(String orgloanamt) {
        this.orgloanamt = orgloanamt;
    }

    public String getOrgloanamt() {
        return orgloanamt;
    }

    public void setTotalinstlno(String[] totalinstlno) {
        this.totalinstlno = totalinstlno;
    }

    public String[] getTotalinstlno() {
        return totalinstlno;
    }

    public void setInstlamt(String[] instlamt) {
        this.instlamt = instlamt;
    }

    public String[] getInstlamt() {
        return instlamt;
    }

    public void setLastpaidinstlno(String[] lastpaidinstlno) {
        this.lastpaidinstlno = lastpaidinstlno;
    }

    public String[] getLastpaidinstlno() {
        return lastpaidinstlno;
    }

    public void setCumulativeamtpaid(String[] cumulativeamtpaid) {
        this.cumulativeamtpaid = cumulativeamtpaid;
    }

    public String[] getCumulativeamtpaid() {
        return cumulativeamtpaid;
    }

   

    public void setHidselectedemp(String hidselectedemp) {
        this.hidselectedemp = hidselectedemp;
    }

    public String getHidselectedemp() {
        return hidselectedemp;
    }

    public void setChkupdtdemp(String chkupdtdemp) {
        this.chkupdtdemp = chkupdtdemp;
    }

    public String getChkupdtdemp() {
        return chkupdtdemp;
    }

    public void setHidselectedempforUpdt(String hidselectedempforUpdt) {
        this.hidselectedempforUpdt = hidselectedempforUpdt;
    }

    public String getHidselectedempforUpdt() {
        return hidselectedempforUpdt;
    }

    public void setChkStopStart(String[] chkStopStart) {
        this.chkStopStart = chkStopStart;
    }

    public String[] getChkStopStart() {
        return chkStopStart;
    }

    public void setHidselectedemptoStop(String hidselectedemptoStop) {
        this.hidselectedemptoStop = hidselectedemptoStop;
    }

    public String getHidselectedemptoStop() {
        return hidselectedemptoStop;
    }

    public void setHidselectedemptoStart(String hidselectedemptoStart) {
        this.hidselectedemptoStart = hidselectedemptoStart;
    }

    public String getHidselectedemptoStart() {
        return hidselectedemptoStart;
    }

    public String getChkNowDeduct() {
        return chkNowDeduct;
    }

    public void setChkNowDeduct(String chkNowDeduct) {
        this.chkNowDeduct = chkNowDeduct;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }
}
