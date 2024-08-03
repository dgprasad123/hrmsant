package hrms.model.loan;

import java.util.Date;
import org.springframework.web.multipart.MultipartFile;

public class Loan {
    
    private String empid = null;
    private String loanid = null;
    private Date doe;
    private String hidEmpId = null;
    private String orderno = null;
    private String orderdate = null;
    private String sltloan = null;
    private double txtamount = 0;
    private String authority = null;
    private String dept = null;
    private String office = null;
    private String spc = null;
    private String note = null;
    private String accountNo = null;
    private String bank = null;
    private String branch = null;
    private String voucherNo = null;
    private String voucherDate = null;
    private String treasuryname = null;
    private String nowDeduct = null;
    private Date deductionStartDate = null;
    private int originalAmt = 0;
    private int totalNoOfInsl = 0;
    private int instalmentAmount = 0;
    private int lastPaidInstalNo = 0;
    private int monthlyinstlno = 0;
    private int cumulativeAmtPaid = 0;
    private int completedRecovery;
    private String txtpvtloan = null;
    private String btid = null;
    private int notid ;
    private int hidNotId ;
    private String notType = null;
    private String hidAuthEmpId = null;
    private String hidSpcAuthCode = null;
    private String hidAuthDeptCode = null;
    private String hidAuthOffCode = null;
    private String isverified="";

    private int repcol = 0;
    private int rowno = 0;
    private String haveInt = null;
    private String nowdedn = null;
    private String loanName = null;

    private String empName = null;
    private String gpfNo = null;
    
    private String refDesc = null;
    private int cummrecovered;
    private int refCount;
    private String month;
    private String year;
    private String loanTp;
    
    private String agcalculationid;
    private String ag1loanid;
    private String agAdrefid;
    private String ag2loanid;
    
    private String agInterestAmt;
    
    private String prinloanid;
    
    private String hidSancDeptCode;
    private String hidSancDistCode;
    private String hidSancOffCode;
    private String hidSancGenericPost;
    private String sancSpc;
    
    private String isValidated;
    
    private String chkNotSBPrint;
    
    private String interestdetailreportgpfno;
    
    private String sltGovernmentContr;
    
    private MultipartFile gcfile;
    private String originalFileNameGcfileDoc = "";
    private String diskFilenameGcfileDoc = "";
    
    private String cpfAmtGc;
    private String gcPercent;
    
    private String correctionid;
    private String entryTypeSBCorrection;
    
    private String completedRecoverySBCorrection;
    
    public int getCummrecovered() {
        return cummrecovered;
    }

    public void setCummrecovered(int cummrecovered) {
        this.cummrecovered = cummrecovered;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getGpfNo() {
        return gpfNo;
    }

    public void setGpfNo(String gpfNo) {
        this.gpfNo = gpfNo;
    }

    public int getRepcol() {
        return repcol;
    }

    public void setRepcol(int repcol) {
        this.repcol = repcol;
    }

    public int getRowno() {
        return rowno;
    }

    public void setRowno(int rowno) {
        this.rowno = rowno;
    }

    public String getHaveInt() {
        return haveInt;
    }

    public void setHaveInt(String haveInt) {
        this.haveInt = haveInt;
    }

    public String getNowdedn() {
        return nowdedn;
    }

    public void setNowdedn(String nowdedn) {
        this.nowdedn = nowdedn;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getHidEmpId() {
        return hidEmpId;
    }

    public void setHidEmpId(String hidEmpId) {
        this.hidEmpId = hidEmpId;
    }

    

    public int getCompletedRecovery() {
        return completedRecovery;
    }

    public void setCompletedRecovery(int completedRecovery) {
        this.completedRecovery = completedRecovery;
    }

    public String getHidAuthDeptCode() {
        return hidAuthDeptCode;
    }

    public void setHidAuthDeptCode(String hidAuthDeptCode) {
        this.hidAuthDeptCode = hidAuthDeptCode;
    }

    public String getHidAuthOffCode() {
        return hidAuthOffCode;
    }

    public void setHidAuthOffCode(String hidAuthOffCode) {
        this.hidAuthOffCode = hidAuthOffCode;
    }

    public String getHidAuthEmpId() {
        return hidAuthEmpId;
    }

    public void setHidAuthEmpId(String hidAuthEmpId) {
        this.hidAuthEmpId = hidAuthEmpId;
    }

    public String getHidSpcAuthCode() {
        return hidSpcAuthCode;
    }

    public void setHidSpcAuthCode(String hidSpcAuthCode) {
        this.hidSpcAuthCode = hidSpcAuthCode;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    

    public String getNotType() {
        return notType;
    }

    public void setNotType(String notType) {
        this.notType = notType;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getLoanid() {
        return loanid;
    }

    public void setLoanid(String loanid) {
        this.loanid = loanid;
    }

    public Date getDoe() {
        return doe;
    }

    public void setDoe(Date doe) {
        this.doe = doe;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getSpc() {
        return spc;
    }

    public void setSpc(String spc) {
        this.spc = spc;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getNowDeduct() {
        return nowDeduct;
    }

    public void setNowDeduct(String nowDeduct) {
        this.nowDeduct = nowDeduct;
    }

    public Date getDeductionStartDate() {
        return deductionStartDate;
    }

    public void setDeductionStartDate(Date deductionStartDate) {
        this.deductionStartDate = deductionStartDate;
    }

    public int getOriginalAmt() {
        return originalAmt;
    }

    public void setOriginalAmt(int originalAmt) {
        this.originalAmt = originalAmt;
    }

    public int getTotalNoOfInsl() {
        return totalNoOfInsl;
    }

    public void setTotalNoOfInsl(int totalNoOfInsl) {
        this.totalNoOfInsl = totalNoOfInsl;
    }

    public int getInstalmentAmount() {
        return instalmentAmount;
    }

    public void setInstalmentAmount(int instalmentAmount) {
        this.instalmentAmount = instalmentAmount;
    }

    public int getLastPaidInstalNo() {
        return lastPaidInstalNo;
    }

    public void setLastPaidInstalNo(int lastPaidInstalNo) {
        this.lastPaidInstalNo = lastPaidInstalNo;
    }

    public int getMonthlyinstlno() {
        return monthlyinstlno;
    }

    public void setMonthlyinstlno(int monthlyinstlno) {
        this.monthlyinstlno = monthlyinstlno;
    }

    public int getCumulativeAmtPaid() {
        return cumulativeAmtPaid;
    }

    public void setCumulativeAmtPaid(int cumulativeAmtPaid) {
        this.cumulativeAmtPaid = cumulativeAmtPaid;
    }

    public String getTxtpvtloan() {
        return txtpvtloan;
    }

    public void setTxtpvtloan(String txtpvtloan) {
        this.txtpvtloan = txtpvtloan;
    }

    public String getBtid() {
        return btid;
    }

    public void setBtid(String btid) {
        this.btid = btid;
    }

    public String getSltloan() {
        return sltloan;
    }

    public void setSltloan(String sltloan) {
        this.sltloan = sltloan;
    }

    public double getTxtamount() {
        return txtamount;
    }

    public void setTxtamount(double txtamount) {
        this.txtamount = txtamount;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getTreasuryname() {
        return treasuryname;
    }

    public void setTreasuryname(String treasuryname) {
        this.treasuryname = treasuryname;
    }

    public String getRefDesc() {
        return refDesc;
    }

    public void setRefDesc(String refDesc) {
        this.refDesc = refDesc;
    }

    public int getRefCount() {
        return refCount;
    }

    public void setRefCount(int refCount) {
        this.refCount = refCount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLoanTp() {
        return loanTp;
    }

    public void setLoanTp(String loanTp) {
        this.loanTp = loanTp;
    }

    public String getIsverified() {
        return isverified;
    }

    public void setIsverified(String isverified) {
        this.isverified = isverified;
    }

    public int getNotid() {
        return notid;
    }

    public void setNotid(int notid) {
        this.notid = notid;
    }

    public int getHidNotId() {
        return hidNotId;
    }

    public void setHidNotId(int hidNotId) {
        this.hidNotId = hidNotId;
    }

    public String getAg1loanid() {
        return ag1loanid;
    }

    public void setAg1loanid(String ag1loanid) {
        this.ag1loanid = ag1loanid;
    }

    public String getAg2loanid() {
        return ag2loanid;
    }

    public void setAg2loanid(String ag2loanid) {
        this.ag2loanid = ag2loanid;
    }

    public String getAgcalculationid() {
        return agcalculationid;
    }

    public void setAgcalculationid(String agcalculationid) {
        this.agcalculationid = agcalculationid;
    }

    public String getAgAdrefid() {
        return agAdrefid;
    }

    public void setAgAdrefid(String agAdrefid) {
        this.agAdrefid = agAdrefid;
    }

    public String getAgInterestAmt() {
        return agInterestAmt;
    }

    public void setAgInterestAmt(String agInterestAmt) {
        this.agInterestAmt = agInterestAmt;
    }

    public String getPrinloanid() {
        return prinloanid;
    }

    public void setPrinloanid(String prinloanid) {
        this.prinloanid = prinloanid;
    }

    public String getHidSancDeptCode() {
        return hidSancDeptCode;
    }

    public void setHidSancDeptCode(String hidSancDeptCode) {
        this.hidSancDeptCode = hidSancDeptCode;
    }

    public String getHidSancDistCode() {
        return hidSancDistCode;
    }

    public void setHidSancDistCode(String hidSancDistCode) {
        this.hidSancDistCode = hidSancDistCode;
    }

    public String getHidSancOffCode() {
        return hidSancOffCode;
    }

    public void setHidSancOffCode(String hidSancOffCode) {
        this.hidSancOffCode = hidSancOffCode;
    }

    public String getHidSancGenericPost() {
        return hidSancGenericPost;
    }

    public void setHidSancGenericPost(String hidSancGenericPost) {
        this.hidSancGenericPost = hidSancGenericPost;
    }

    public String getSancSpc() {
        return sancSpc;
    }

    public void setSancSpc(String sancSpc) {
        this.sancSpc = sancSpc;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getIsValidated() {
        return isValidated;
    }

    public void setIsValidated(String isValidated) {
        this.isValidated = isValidated;
    }

    public String getChkNotSBPrint() {
        return chkNotSBPrint;
    }

    public void setChkNotSBPrint(String chkNotSBPrint) {
        this.chkNotSBPrint = chkNotSBPrint;
    }

    public String getInterestdetailreportgpfno() {
        return interestdetailreportgpfno;
    }

    public String getSltGovernmentContr() {
        return sltGovernmentContr;
    }

    public void setSltGovernmentContr(String sltGovernmentContr) {
        this.sltGovernmentContr = sltGovernmentContr;
    }

    public void setInterestdetailreportgpfno(String interestdetailreportgpfno) {
        this.interestdetailreportgpfno = interestdetailreportgpfno;
    }

    public MultipartFile getGcfile() {
        return gcfile;
    }

    public void setGcfile(MultipartFile gcfile) {
        this.gcfile = gcfile;
    }

    public String getOriginalFileNameGcfileDoc() {
        return originalFileNameGcfileDoc;
    }

    public void setOriginalFileNameGcfileDoc(String originalFileNameGcfileDoc) {
        this.originalFileNameGcfileDoc = originalFileNameGcfileDoc;
    }

    public String getDiskFilenameGcfileDoc() {
        return diskFilenameGcfileDoc;
    }

    public void setDiskFilenameGcfileDoc(String diskFilenameGcfileDoc) {
        this.diskFilenameGcfileDoc = diskFilenameGcfileDoc;
    }

    public String getCpfAmtGc() {
        return cpfAmtGc;
    }

    public void setCpfAmtGc(String cpfAmtGc) {
        this.cpfAmtGc = cpfAmtGc;
    }

    public String getGcPercent() {
        return gcPercent;
    }

    public void setGcPercent(String gcPercent) {
        this.gcPercent = gcPercent;
    }    

    public String getCorrectionid() {
        return correctionid;
    }

    public void setCorrectionid(String correctionid) {
        this.correctionid = correctionid;
    }

    public String getEntryTypeSBCorrection() {
        return entryTypeSBCorrection;
    }

    public void setEntryTypeSBCorrection(String entryTypeSBCorrection) {
        this.entryTypeSBCorrection = entryTypeSBCorrection;
    }

    public String getCompletedRecoverySBCorrection() {
        return completedRecoverySBCorrection;
    }

    public void setCompletedRecoverySBCorrection(String completedRecoverySBCorrection) {
        this.completedRecoverySBCorrection = completedRecoverySBCorrection;
    }
}
