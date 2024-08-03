/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.loanworkflow;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author lenovo
 */
public class LoanTempGPFForm {
     private String designation = null;
    private String empName = null;
    private String accountNo = null;
    private String pay = null;
    private String gp = null;
    private String doj = null;
    private String amount_credit = null;
    private String amount_subscription = null;
    private String deduct_adv = null;
    private String advance_taken = null;
    private String amount_drawal = null;
    private String date_drawal = null;
    private String purpose_drawal = null;
    private String date_repaid = null;
    private String date_drawal_sanction = null;
    private String date_drawal_cadvance = null;
    private String balance_outstanding = null;
    private String rate_recovery = null;
    private String final_payment = null;
    private String amount_adv = null;
    private String purpose = null;
    private String total_advance = null;
    private String gpfno = null;
    private String gpftype = null;
    private String  noofinst = null;
    
    private String empSPC = null;
    private String forwardtoHrmsid = null;
    private int taskid = 0;
    private int loanId = 0;
    private String approvedBy = null;
    private String approvedSpc = null;
    private String diskFileName = null;
    private String loanapplyfor = null;
    private String fileView = null;
    private String hidOffCode = null;
    private String hidOffName = null;
    private String hidSPC = null;
    private String antprice = null;
    private MultipartFile file_att = null;
    private String ddocode=null;
     private String loancomments=null;
     private int loan_status = 0;
     private String empId=null;
    private int statusId=0;
     private String letterNo=null;
    private String letterDate=null;
    private String memoNo=null;
    private String memoDate=null;
    private String letterformName=null;
    private String letterformdesignation=null;
    private String letterto=null;
    private String officeAddress=null;
    private String accountOfficer=null;

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getGp() {
        return gp;
    }

    public void setGp(String gp) {
        this.gp = gp;
    }

    public String getDoj() {
        return doj;
    }

    public void setDoj(String doj) {
        this.doj = doj;
    }

    public String getAmount_credit() {
        return amount_credit;
    }

    public void setAmount_credit(String amount_credit) {
        this.amount_credit = amount_credit;
    }

    public String getAmount_subscription() {
        return amount_subscription;
    }

    public void setAmount_subscription(String amount_subscription) {
        this.amount_subscription = amount_subscription;
    }

    public String getDeduct_adv() {
        return deduct_adv;
    }

    public void setDeduct_adv(String deduct_adv) {
        this.deduct_adv = deduct_adv;
    }

    public String getAdvance_taken() {
        return advance_taken;
    }

    public void setAdvance_taken(String advance_taken) {
        this.advance_taken = advance_taken;
    }

    public String getAmount_drawal() {
        return amount_drawal;
    }

    public void setAmount_drawal(String amount_drawal) {
        this.amount_drawal = amount_drawal;
    }

    public String getDate_drawal() {
        return date_drawal;
    }

    public void setDate_drawal(String date_drawal) {
        this.date_drawal = date_drawal;
    }

    public String getPurpose_drawal() {
        return purpose_drawal;
    }

    public void setPurpose_drawal(String purpose_drawal) {
        this.purpose_drawal = purpose_drawal;
    }

    public String getDate_repaid() {
        return date_repaid;
    }

    public void setDate_repaid(String date_repaid) {
        this.date_repaid = date_repaid;
    }

    public String getDate_drawal_sanction() {
        return date_drawal_sanction;
    }

    public void setDate_drawal_sanction(String date_drawal_sanction) {
        this.date_drawal_sanction = date_drawal_sanction;
    }

    public String getDate_drawal_cadvance() {
        return date_drawal_cadvance;
    }

    public void setDate_drawal_cadvance(String date_drawal_cadvance) {
        this.date_drawal_cadvance = date_drawal_cadvance;
    }

    public String getBalance_outstanding() {
        return balance_outstanding;
    }

    public void setBalance_outstanding(String balance_outstanding) {
        this.balance_outstanding = balance_outstanding;
    }

    public String getRate_recovery() {
        return rate_recovery;
    }

    public void setRate_recovery(String rate_recovery) {
        this.rate_recovery = rate_recovery;
    }

    public String getFinal_payment() {
        return final_payment;
    }

    public void setFinal_payment(String final_payment) {
        this.final_payment = final_payment;
    }

    public String getAmount_adv() {
        return amount_adv;
    }

    public void setAmount_adv(String amount_adv) {
        this.amount_adv = amount_adv;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getTotal_advance() {
        return total_advance;
    }

    public void setTotal_advance(String total_advance) {
        this.total_advance = total_advance;
    }

    public String getGpfno() {
        return gpfno;
    }

    public void setGpfno(String gpfno) {
        this.gpfno = gpfno;
    }

    public String getGpftype() {
        return gpftype;
    }

    public void setGpftype(String gpftype) {
        this.gpftype = gpftype;
    }

   

    public String getEmpSPC() {
        return empSPC;
    }

    public void setEmpSPC(String empSPC) {
        this.empSPC = empSPC;
    }

    public String getForwardtoHrmsid() {
        return forwardtoHrmsid;
    }

    public void setForwardtoHrmsid(String forwardtoHrmsid) {
        this.forwardtoHrmsid = forwardtoHrmsid;
    }

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getApprovedSpc() {
        return approvedSpc;
    }

    public void setApprovedSpc(String approvedSpc) {
        this.approvedSpc = approvedSpc;
    }

    public String getDiskFileName() {
        return diskFileName;
    }

    public void setDiskFileName(String diskFileName) {
        this.diskFileName = diskFileName;
    }

    public String getLoanapplyfor() {
        return loanapplyfor;
    }

    public void setLoanapplyfor(String loanapplyfor) {
        this.loanapplyfor = loanapplyfor;
    }

    public String getFileView() {
        return fileView;
    }

    public void setFileView(String fileView) {
        this.fileView = fileView;
    }

    public String getHidOffCode() {
        return hidOffCode;
    }

    public void setHidOffCode(String hidOffCode) {
        this.hidOffCode = hidOffCode;
    }

    public String getHidOffName() {
        return hidOffName;
    }

    public void setHidOffName(String hidOffName) {
        this.hidOffName = hidOffName;
    }

    public String getHidSPC() {
        return hidSPC;
    }

    public void setHidSPC(String hidSPC) {
        this.hidSPC = hidSPC;
    }

    public String getAntprice() {
        return antprice;
    }

    public void setAntprice(String antprice) {
        this.antprice = antprice;
    }

    public MultipartFile getFile_att() {
        return file_att;
    }

    public void setFile_att(MultipartFile file_att) {
        this.file_att = file_att;
    }

    public String getDdocode() {
        return ddocode;
    }

    public void setDdocode(String ddocode) {
        this.ddocode = ddocode;
    }

    public String getLoancomments() {
        return loancomments;
    }

    public void setLoancomments(String loancomments) {
        this.loancomments = loancomments;
    }

    public int getLoan_status() {
        return loan_status;
    }

    public void setLoan_status(int loan_status) {
        this.loan_status = loan_status;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getLetterNo() {
        return letterNo;
    }

    public void setLetterNo(String letterNo) {
        this.letterNo = letterNo;
    }

    public String getLetterDate() {
        return letterDate;
    }

    public void setLetterDate(String letterDate) {
        this.letterDate = letterDate;
    }

    public String getMemoNo() {
        return memoNo;
    }

    public void setMemoNo(String memoNo) {
        this.memoNo = memoNo;
    }

    public String getMemoDate() {
        return memoDate;
    }

    public void setMemoDate(String memoDate) {
        this.memoDate = memoDate;
    }

    public String getLetterformName() {
        return letterformName;
    }

    public void setLetterformName(String letterformName) {
        this.letterformName = letterformName;
    }

    public String getLetterformdesignation() {
        return letterformdesignation;
    }

    public void setLetterformdesignation(String letterformdesignation) {
        this.letterformdesignation = letterformdesignation;
    }

    public String getLetterto() {
        return letterto;
    }

    public void setLetterto(String letterto) {
        this.letterto = letterto;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public String getNoofinst() {
        return noofinst;
    }

    public void setNoofinst(String noofinst) {
        this.noofinst = noofinst;
    }

    public String getAccountOfficer() {
        return accountOfficer;
    }

    public void setAccountOfficer(String accountOfficer) {
        this.accountOfficer = accountOfficer;
    }

   

   
    
}
