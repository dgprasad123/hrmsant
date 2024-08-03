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
public class LoanForm {

    private String designation = null;
    private String basicsalary = null;
    private String netsalary = null;
    private String hidOffCode = null;
    private String hidOffName = null;
    private String hidSPC = null;
    private String antprice = null;
    private String purtype = null;
    private String amountadv = null;
    private int instalments = 0;
    private String previousAvail = null;
    private String preAdvPur = null;
    private String amounpretadv = null;
    private String dateofdrawal = null;
    private String intpaidfull = null;
    private String amountstanding = null;
    private String officerleave = null;
    private String datecommleave = null;
    private String dateexpireleave = null;
    private MultipartFile file_att = null;
    private String forwardto = null;
    private String EmpName = null;
    private String offaddress = null;
    private String name = null;
    private String JobType = null;
    private String Empdob = null;
    private String loancomments = null;
    private String Superannuation = null;
    private int loan_status = 0;

    private String empSPC = null;
    private String forwardtoHrmsid = null;
    private String loanstatus = null;
    private int taskid = 0;
    private int loanId = 0;
    private String approvedBy = null;
    private String approvedSpc = null;
    private String gpfno = null;
    private String gp = null;
    private String diskFileName = null;
    private String loanapplyfor = null;
    private String letterNo = null;
    private String letterDate = null;
    private String memoNo = null;
    private String memoDate = null;
    private String letterformName = null;
    private String letterformdesignation = null;
    private String letterto = null;
    private String fileView = null;
    private String empID = null;
    private int statusId = 0;
    private String notes = null;

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

    private String accountNo = null;
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

    

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public String getLoancomments() {
        return loancomments;
    }

    public void setLoancomments(String loancomments) {
        this.loancomments = loancomments;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getBasicsalary() {
        return basicsalary;
    }

    public void setBasicsalary(String basicsalary) {
        this.basicsalary = basicsalary;
    }

    public String getNetsalary() {
        return netsalary;
    }

    public void setNetsalary(String netsalary) {
        this.netsalary = netsalary;
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

    public String getPurtype() {
        return purtype;
    }

    public void setPurtype(String purtype) {
        this.purtype = purtype;
    }

    public String getAmountadv() {
        return amountadv;
    }

    public void setAmountadv(String amountadv) {
        this.amountadv = amountadv;
    }

    public int getInstalments() {
        return instalments;
    }

    public void setInstalments(int instalments) {
        this.instalments = instalments;
    }

    public String getPreviousAvail() {
        return previousAvail;
    }

    public void setPreviousAvail(String previousAvail) {
        this.previousAvail = previousAvail;
    }

    public String getPreAdvPur() {
        return preAdvPur;
    }

    public void setPreAdvPur(String preAdvPur) {
        this.preAdvPur = preAdvPur;
    }

    public String getAmounpretadv() {
        return amounpretadv;
    }

    public void setAmounpretadv(String amounpretadv) {
        this.amounpretadv = amounpretadv;
    }

    public String getDateofdrawal() {
        return dateofdrawal;
    }

    public void setDateofdrawal(String dateofdrawal) {
        this.dateofdrawal = dateofdrawal;
    }

    public String getIntpaidfull() {
        return intpaidfull;
    }

    public void setIntpaidfull(String intpaidfull) {
        this.intpaidfull = intpaidfull;
    }

    public String getAmountstanding() {
        return amountstanding;
    }

    public void setAmountstanding(String amountstanding) {
        this.amountstanding = amountstanding;
    }

    public String getOfficerleave() {
        return officerleave;
    }

    public void setOfficerleave(String officerleave) {
        this.officerleave = officerleave;
    }

    public String getDatecommleave() {
        return datecommleave;
    }

    public void setDatecommleave(String datecommleave) {
        this.datecommleave = datecommleave;
    }

    public String getDateexpireleave() {
        return dateexpireleave;
    }

    public void setDateexpireleave(String dateexpireleave) {
        this.dateexpireleave = dateexpireleave;
    }

    public String getForwardto() {
        return forwardto;
    }

    public void setForwardto(String forwardto) {
        this.forwardto = forwardto;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String EmpName) {
        this.EmpName = EmpName;
    }

    public String getJobType() {
        return JobType;
    }

    public void setJobType(String JobType) {
        this.JobType = JobType;
    }

    public String getEmpdob() {
        return Empdob;
    }

    public void setEmpdob(String Empdob) {
        this.Empdob = Empdob;
    }

    public String getSuperannuation() {
        return Superannuation;
    }

    public void setSuperannuation(String Superannuation) {
        this.Superannuation = Superannuation;
    }

    public int getLoan_status() {
        return loan_status;
    }

    public void setLoan_status(int loan_status) {
        this.loan_status = loan_status;
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

    public String getLoanstatus() {
        return loanstatus;
    }

    public void setLoanstatus(String loanstatus) {
        this.loanstatus = loanstatus;
    }

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public MultipartFile getFile_att() {
        return file_att;
    }

    public void setFile_att(MultipartFile file_att) {
        this.file_att = file_att;
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

    public String getGpfno() {
        return gpfno;
    }

    public void setGpfno(String gpfno) {
        this.gpfno = gpfno;
    }

    public String getGp() {
        return gp;
    }

    public void setGp(String gp) {
        this.gp = gp;
    }

    public String getFileView() {
        return fileView;
    }

    public void setFileView(String fileView) {
        this.fileView = fileView;
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

    public String getOffaddress() {
        return offaddress;
    }

    public void setOffaddress(String offaddress) {
        this.offaddress = offaddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEmpID() {
        return empID;
    }

    public void setEmpID(String empID) {
        this.empID = empID;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
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

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getPranNo() {
        return pranNo;
    }

    public void setPranNo(String pranNo) {
        this.pranNo = pranNo;
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

    public String getReleaseAmount() {
        return releaseAmount;
    }

    public void setReleaseAmount(String releaseAmount) {
        this.releaseAmount = releaseAmount;
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
