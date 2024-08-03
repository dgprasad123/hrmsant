/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.loanworkflow;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author lenovo
 */
public class LoanGPFForm {

    private String designation = null;
    private String empName = null;
    private String accountNo = null;
    private String pay = null;
    private String gp = null;
    private String doj = null;
    private String supperannuation = null;
    private String balanceCredit = null;
    private String closingbalance = null;
    private String creditForm = null;
    private String creditTo = null;
    private String creditAmount = null;
    private String refund = null;
    private String withdrawfrom = null;
    private String withdrawto = null;
    private String withdrawalAmount = null;
    private String netbalance = null;
    private String withdrawalreq = null;
    private String purpose = null;
    private String requestcovered = null;
    private String withdrawaltaken = null;
    private String accountOfficer = null;
    private String gpfno = null;
    private String gpftype = null;
    private int cyear = 0;
    private int previousYear = 0;
    private int cmonth = 0;
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
    private String sanNo=null;
    private String sanDate=null;
    private String fileno=null;
    private String issueNo=null;
    
    
     private String samount = null;
    private String ramount = null;
    private int emiPrincipal;

    private String principalAmount = null;
    private int emiInterest;
    private String interestAmount = null;
    private String interestType = null;
    private String rateInterest = null;
    private String lastInstallment = null;

    private String penalRate = null;
    private String moratoriumRequired = null;
    private String moratoriumPeriod = null;
    private String recDate = null;
    private String insuranceFlag = null;
    private String sanOperator = null;

    private String sanOperatorDDO = null;
    private String sanOperatorLoginId = null;

   // private String accountNo = null;
    private String dob = null;
    private String dos = null;
    private String empType = null;
    private String address = null;
    private String loanType = null;
    private String pranNo = null;
    private String releaseAmount = null;

    private int principalInstallment;
    private int principalInstAmount;
    private String accountType = null;

    private String sanctionNo = null;
    private String sanctionDate = null;
    private String totalInterestamount = null;
    private String billDescription = null;
    private String billno = null;
    private String billdate = null;
    private String billAmount = null;
    private String chatofAccount = null;
    
    private String billid;
    private String demandNo;
    private String majorhead;


    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }
    

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

    public String getSupperannuation() {
        return supperannuation;
    }

    public void setSupperannuation(String supperannuation) {
        this.supperannuation = supperannuation;
    }

    public String getBalanceCredit() {
        return balanceCredit;
    }

    public void setBalanceCredit(String balanceCredit) {
        this.balanceCredit = balanceCredit;
    }

    public String getClosingbalance() {
        return closingbalance;
    }

    public void setClosingbalance(String closingbalance) {
        this.closingbalance = closingbalance;
    }

    public String getCreditForm() {
        return creditForm;
    }

    public void setCreditForm(String creditForm) {
        this.creditForm = creditForm;
    }

    public String getCreditTo() {
        return creditTo;
    }

    public void setCreditTo(String creditTo) {
        this.creditTo = creditTo;
    }

    public String getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(String creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getRefund() {
        return refund;
    }

    public void setRefund(String refund) {
        this.refund = refund;
    }

    public String getWithdrawfrom() {
        return withdrawfrom;
    }

    public void setWithdrawfrom(String withdrawfrom) {
        this.withdrawfrom = withdrawfrom;
    }

    public String getWithdrawto() {
        return withdrawto;
    }

    public void setWithdrawto(String withdrawto) {
        this.withdrawto = withdrawto;
    }

    public String getWithdrawalAmount() {
        return withdrawalAmount;
    }

    public void setWithdrawalAmount(String withdrawalAmount) {
        this.withdrawalAmount = withdrawalAmount;
    }

    public String getNetbalance() {
        return netbalance;
    }

    public void setNetbalance(String netbalance) {
        this.netbalance = netbalance;
    }

    public String getWithdrawalreq() {
        return withdrawalreq;
    }

    public void setWithdrawalreq(String withdrawalreq) {
        this.withdrawalreq = withdrawalreq;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getRequestcovered() {
        return requestcovered;
    }

    public void setRequestcovered(String requestcovered) {
        this.requestcovered = requestcovered;
    }

    public String getWithdrawaltaken() {
        return withdrawaltaken;
    }

    public void setWithdrawaltaken(String withdrawaltaken) {
        this.withdrawaltaken = withdrawaltaken;
    }

    public String getAccountOfficer() {
        return accountOfficer;
    }

    public void setAccountOfficer(String accountOfficer) {
        this.accountOfficer = accountOfficer;
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

    public int getCyear() {
        return cyear;
    }

    public void setCyear(int cyear) {
        this.cyear = cyear;
    }

    public int getPreviousYear() {
        return previousYear;
    }

    public void setPreviousYear(int previousYear) {
        this.previousYear = previousYear;
    }

    public int getCmonth() {
        return cmonth;
    }

    public void setCmonth(int cmonth) {
        this.cmonth = cmonth;
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

    public String getSanNo() {
        return sanNo;
    }

    public void setSanNo(String sanNo) {
        this.sanNo = sanNo;
    }

    public String getSanDate() {
        return sanDate;
    }

    public void setSanDate(String sanDate) {
        this.sanDate = sanDate;
    }

    public String getFileno() {
        return fileno;
    }

    public void setFileno(String fileno) {
        this.fileno = fileno;
    }

    public String getIssueNo() {
        return issueNo;
    }

    public void setIssueNo(String issueNo) {
        this.issueNo = issueNo;
    }

    public String getSamount() {
        return samount;
    }

    public void setSamount(String samount) {
        this.samount = samount;
    }

    public String getRamount() {
        return ramount;
    }

    public void setRamount(String ramount) {
        this.ramount = ramount;
    }

    public int getEmiPrincipal() {
        return emiPrincipal;
    }

    public void setEmiPrincipal(int emiPrincipal) {
        this.emiPrincipal = emiPrincipal;
    }

    public String getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(String principalAmount) {
        this.principalAmount = principalAmount;
    }

    public int getEmiInterest() {
        return emiInterest;
    }

    public void setEmiInterest(int emiInterest) {
        this.emiInterest = emiInterest;
    }

    public String getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(String interestAmount) {
        this.interestAmount = interestAmount;
    }

    public String getInterestType() {
        return interestType;
    }

    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }

    public String getRateInterest() {
        return rateInterest;
    }

    public void setRateInterest(String rateInterest) {
        this.rateInterest = rateInterest;
    }

    public String getLastInstallment() {
        return lastInstallment;
    }

    public void setLastInstallment(String lastInstallment) {
        this.lastInstallment = lastInstallment;
    }

    public String getPenalRate() {
        return penalRate;
    }

    public void setPenalRate(String penalRate) {
        this.penalRate = penalRate;
    }

    public String getMoratoriumRequired() {
        return moratoriumRequired;
    }

    public void setMoratoriumRequired(String moratoriumRequired) {
        this.moratoriumRequired = moratoriumRequired;
    }

    public String getMoratoriumPeriod() {
        return moratoriumPeriod;
    }

    public void setMoratoriumPeriod(String moratoriumPeriod) {
        this.moratoriumPeriod = moratoriumPeriod;
    }

    public String getRecDate() {
        return recDate;
    }

    public void setRecDate(String recDate) {
        this.recDate = recDate;
    }

    public String getInsuranceFlag() {
        return insuranceFlag;
    }

    public void setInsuranceFlag(String insuranceFlag) {
        this.insuranceFlag = insuranceFlag;
    }

    public String getSanOperator() {
        return sanOperator;
    }

    public void setSanOperator(String sanOperator) {
        this.sanOperator = sanOperator;
    }

    public String getSanOperatorDDO() {
        return sanOperatorDDO;
    }

    public void setSanOperatorDDO(String sanOperatorDDO) {
        this.sanOperatorDDO = sanOperatorDDO;
    }

    public String getSanOperatorLoginId() {
        return sanOperatorLoginId;
    }

    public void setSanOperatorLoginId(String sanOperatorLoginId) {
        this.sanOperatorLoginId = sanOperatorLoginId;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDos() {
        return dos;
    }

    public void setDos(String dos) {
        this.dos = dos;
    }

    public String getEmpType() {
        return empType;
    }

    public void setEmpType(String empType) {
        this.empType = empType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getPranNo() {
        return pranNo;
    }

    public void setPranNo(String pranNo) {
        this.pranNo = pranNo;
    }

    public String getReleaseAmount() {
        return releaseAmount;
    }

    public void setReleaseAmount(String releaseAmount) {
        this.releaseAmount = releaseAmount;
    }

    public int getPrincipalInstallment() {
        return principalInstallment;
    }

    public void setPrincipalInstallment(int principalInstallment) {
        this.principalInstallment = principalInstallment;
    }

    public int getPrincipalInstAmount() {
        return principalInstAmount;
    }

    public void setPrincipalInstAmount(int principalInstAmount) {
        this.principalInstAmount = principalInstAmount;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getSanctionNo() {
        return sanctionNo;
    }

    public void setSanctionNo(String sanctionNo) {
        this.sanctionNo = sanctionNo;
    }

    public String getSanctionDate() {
        return sanctionDate;
    }

    public void setSanctionDate(String sanctionDate) {
        this.sanctionDate = sanctionDate;
    }

    public String getTotalInterestamount() {
        return totalInterestamount;
    }

    public void setTotalInterestamount(String totalInterestamount) {
        this.totalInterestamount = totalInterestamount;
    }

    public String getBillDescription() {
        return billDescription;
    }

    public void setBillDescription(String billDescription) {
        this.billDescription = billDescription;
    }

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }

    public String getBilldate() {
        return billdate;
    }

    public void setBilldate(String billdate) {
        this.billdate = billdate;
    }

    public String getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(String billAmount) {
        this.billAmount = billAmount;
    }

    public String getChatofAccount() {
        return chatofAccount;
    }

    public void setChatofAccount(String chatofAccount) {
        this.chatofAccount = chatofAccount;
    }

    public String getBillid() {
        return billid;
    }

    public void setBillid(String billid) {
        this.billid = billid;
    }

    public String getDemandNo() {
        return demandNo;
    }

    public void setDemandNo(String demandNo) {
        this.demandNo = demandNo;
    }

    public String getMajorhead() {
        return majorhead;
    }

    public void setMajorhead(String majorhead) {
        this.majorhead = majorhead;
    }

}
