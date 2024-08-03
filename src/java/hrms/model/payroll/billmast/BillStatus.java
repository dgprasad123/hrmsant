/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.billmast;

/**
 *
 * @author Manas
 */
public class BillStatus {

    private String departmentname = null;
    private String deptCode = null;
    private String ddoCode = null;
    private int totalDDO;
    private int ddoPrepared;
    private int billPrepared;
    private int ddoSubmitted;
    private int billSubmitted;
    private int tokenPrepared;
    private int noofError;

    private int totalEmp;
    private int totalCompleted;
    private int totalVerified;

    private String empName = null;
    private String empPost = null;
    private String isCompleted = null;
    private String isVerified = null;
    private String isRegular = null;
    private String empId = null;

    private int operatorSubmitted;
    private int approverSubmitted;
    private int reviewerSubmitted;
    private int verifierSubmitted;
    private int acceptorSubmitted;

    private String offCode = null;
    
     private int voucherPrepared;
    private int month;
    private int year;
    private String billType;
    private String strMonth;
    private String strYear;
    

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getDepartmentname() {
        return departmentname;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public int getTotalDDO() {
        return totalDDO;
    }

    public void setTotalDDO(int totalDDO) {
        this.totalDDO = totalDDO;
    }

    public int getDdoPrepared() {
        return ddoPrepared;
    }

    public void setDdoPrepared(int ddoPrepared) {
        this.ddoPrepared = ddoPrepared;
    }

    public int getBillPrepared() {
        return billPrepared;
    }

    public void setBillPrepared(int billPrepared) {
        this.billPrepared = billPrepared;
    }

    public int getDdoSubmitted() {
        return ddoSubmitted;
    }

    public void setDdoSubmitted(int ddoSubmitted) {
        this.ddoSubmitted = ddoSubmitted;
    }

    public int getBillSubmitted() {
        return billSubmitted;
    }

    public void setBillSubmitted(int billSubmitted) {
        this.billSubmitted = billSubmitted;
    }

    public int getTokenPrepared() {
        return tokenPrepared;
    }

    public void setTokenPrepared(int tokenPrepared) {
        this.tokenPrepared = tokenPrepared;
    }

    public String getDdoCode() {
        return ddoCode;
    }

    public void setDdoCode(String ddoCode) {
        this.ddoCode = ddoCode;
    }

    public int getTotalEmp() {
        return totalEmp;
    }

    public void setTotalEmp(int totalEmp) {
        this.totalEmp = totalEmp;
    }

    public int getTotalCompleted() {
        return totalCompleted;
    }

    public void setTotalCompleted(int totalCompleted) {
        this.totalCompleted = totalCompleted;
    }

    public int getTotalVerified() {
        return totalVerified;
    }

    public void setTotalVerified(int totalVerified) {
        this.totalVerified = totalVerified;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpPost() {
        return empPost;
    }

    public void setEmpPost(String empPost) {
        this.empPost = empPost;
    }

    public String getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(String isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public String getIsRegular() {
        return isRegular;
    }

    public void setIsRegular(String isRegular) {
        this.isRegular = isRegular;
    }

    public int getOperatorSubmitted() {
        return operatorSubmitted;
    }

    public void setOperatorSubmitted(int operatorSubmitted) {
        this.operatorSubmitted = operatorSubmitted;
    }

    public int getApproverSubmitted() {
        return approverSubmitted;
    }

    public void setApproverSubmitted(int approverSubmitted) {
        this.approverSubmitted = approverSubmitted;
    }

    public int getReviewerSubmitted() {
        return reviewerSubmitted;
    }

    public void setReviewerSubmitted(int reviewerSubmitted) {
        this.reviewerSubmitted = reviewerSubmitted;
    }

    public int getVerifierSubmitted() {
        return verifierSubmitted;
    }

    public void setVerifierSubmitted(int verifierSubmitted) {
        this.verifierSubmitted = verifierSubmitted;
    }

    public int getAcceptorSubmitted() {
        return acceptorSubmitted;
    }

    public void setAcceptorSubmitted(int acceptorSubmitted) {
        this.acceptorSubmitted = acceptorSubmitted;
    }

    public String getOffCode() {
        return offCode;
    }

    public void setOffCode(String offCode) {
        this.offCode = offCode;
    }

    public int getVoucherPrepared() {
        return voucherPrepared;
    }

    public void setVoucherPrepared(int voucherPrepared) {
        this.voucherPrepared = voucherPrepared;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public int getNoofError() {
        return noofError;
    }

    public void setNoofError(int noofError) {
        this.noofError = noofError;
    }

    public String getStrMonth() {
        return strMonth;
    }

    public void setStrMonth(String strMonth) {
        this.strMonth = strMonth;
    }

    public String getStrYear() {
        return strYear;
    }

    public void setStrYear(String strYear) {
        this.strYear = strYear;
    }

}
