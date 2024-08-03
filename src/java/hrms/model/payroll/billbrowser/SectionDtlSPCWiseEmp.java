/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.billbrowser;

import java.util.Date;

/**
 *
 * @author Manas Jena
 */
public class SectionDtlSPCWiseEmp {
    private String spc;
    private String empid;
    private int postslno;
    private String postname;
    private String orderno;
    private Date orderdate;
    private String payscale;
    private Date doegov;
    private int curBasicSalary;
    private int gp;
    private int prevBasicSalary;
    private int prevGp;
    private Date payDate;
    
    private String fname;
    private String mname;
    private String lname;
    private String deptcode;
    private String bankcode;
    private String branchcode;
    private String ifscCode=null;
    private String offcode;
    
    
    private String bankaccno;
    private String isgazetted;   
    private String depcode;
    private String acctype;
    
    private String empname;
    private String gpfaccno;
    private String accountAssume="";
    private int jobtypeid;
     private String payheldup;

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getGpfaccno() {
        return gpfaccno;
    }

    public void setGpfaccno(String gpfaccno) {
        this.gpfaccno = gpfaccno;
    }
    
    

    public String getBankaccno() {
        return bankaccno;
    }

    public void setBankaccno(String bankaccno) {
        this.bankaccno = bankaccno;
    }

    public String getIsgazetted() {
        return isgazetted;
    }

    public void setIsgazetted(String isgazetted) {
        this.isgazetted = isgazetted;
    }

    public String getDepcode() {
        return depcode;
    }

    public void setDepcode(String depcode) {
        this.depcode = depcode;
    }

    public String getAcctype() {
        return acctype;
    }

    public void setAcctype(String acctype) {
        this.acctype = acctype;
    }
    
    

    public String getOffcode() {
        return offcode;
    }

    public void setOffcode(String offcode) {
        this.offcode = offcode;
    }
    
    public String getSpc() {
        return spc;
    }

    public void setSpc(String spc) {
        this.spc = spc;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public int getPostslno() {
        return postslno;
    }

    public void setPostslno(int postslno) {
        this.postslno = postslno;
    }

    public String getPostname() {
        return postname;
    }

    public void setPostname(String postname) {
        this.postname = postname;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public Date getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(Date orderdate) {
        this.orderdate = orderdate;
    }

    public String getPayscale() {
        return payscale;
    }

    public void setPayscale(String payscale) {
        this.payscale = payscale;
    }

    public Date getDoegov() {
        return doegov;
    }

    public void setDoegov(Date doegov) {
        this.doegov = doegov;
    }

    public int getCurBasicSalary() {
        return curBasicSalary;
    }

    public void setCurBasicSalary(int curBasicSalary) {
        this.curBasicSalary = curBasicSalary;
    }

    public int getGp() {
        return gp;
    }

    public void setGp(int gp) {
        this.gp = gp;
    }

    public int getPrevBasicSalary() {
        return prevBasicSalary;
    }

    public void setPrevBasicSalary(int prevBasicSalary) {
        this.prevBasicSalary = prevBasicSalary;
    }

    public int getPrevGp() {
        return prevGp;
    }

    public void setPrevGp(int prevGp) {
        this.prevGp = prevGp;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getDeptcode() {
        return deptcode;
    }

    public void setDeptcode(String deptcode) {
        this.deptcode = deptcode;
    }

    public String getBankcode() {
        return bankcode;
    }

    public void setBankcode(String bankcode) {
        this.bankcode = bankcode;
    }

    public String getBranchcode() {
        return branchcode;
    }

    public void setBranchcode(String branchcode) {
        this.branchcode = branchcode;
    }

    public int getJobtypeid() {
        return jobtypeid;
    }

    public void setJobtypeid(int jobtypeid) {
        this.jobtypeid = jobtypeid;
    }    

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getAccountAssume() {
        return accountAssume;
    }

    public void setAccountAssume(String accountAssume) {
        this.accountAssume = accountAssume;
    }

    public String getPayheldup() {
        return payheldup;
    }

    public void setPayheldup(String payheldup) {
        this.payheldup = payheldup;
    }
    
    
}
