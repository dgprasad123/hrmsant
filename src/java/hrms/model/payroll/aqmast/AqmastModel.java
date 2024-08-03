/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.aqmast;

import java.math.BigDecimal;
import java.util.Date;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Durga
 */
public class AqmastModel {

    private int slno;
    private int grossAmt;
    private int dedAmt;
    private int netAmt;
    private String aqSlNo = null;
    private BigDecimal aqGroup;
    private String aqGroupDesc = null;
    private int payMonth = 0;
    private int payYear = 0;
    private String payType = null;
    private Date aqDate = null;
    private int aqMonth = 0;
    private int aqYear = 0;
    private String aqType = null;
    private String refOrder = null;
    private Date refDate = null;
    private int aqDay = 0;
    private int payDay = 0;
    private String demandNumber=null;
    private String majorHead = null;
    private String majorHeadDesc = null;
    private String subMajorHead = null;
    private String subMajorHeadDesc = null;
    private String minorHead = null;
    private String minorHeadDesc = null;
    private String subMinorHead1 = null;
    private String subMinorHeadDesc1 = null;
    private String subMinorHead2 = null;
    private String subMinorHeadDesc2 = null;
    private String subMinorHead3 = null;
    private String subMinorHeadDesc3 = null;
    private String plan = null;
    private String sector = null;
    private String altUnit = null;
    private String offCode = null;
    private String offDdo = null;
    private int secSlNo = 0;
    private String section = null;
    private int postSlNo = 0;
    private String curDesg = null;
    private String curGrade = null;
    private String cadreType=null;
    private String curLevel = null;
    private String gazetted = null;
    private String payScale = null;
    private long monBasic = 0;
    private String empCode = null;
    private String empName = null;
    private String gpfType = null;
    private String gpfAccNo = null;
    private long curBasic = 0;
    private int billNo = 0;
    private Date billDate = null;
    private String bankName = null;
    private String branchName = null;
    private String bankAccNo = null;
    private String ifscCode=null;
    private int defaultBank = 0;
    private String remark = null;
    private String spcOrdNo = null;
    private Date spcOrdDate = null;
    private String acctType = null;
    private String curSpc = null;
    private String empType = null;
    private String deptCode = null;
    private String ifMaintenanceDeduct=null;
    private String darate="";
    private MultipartFile bankdocProof=null;
    private String bankdocProofFilename="";
    
    private String bankaccUpdatedBy="";
    private String bankaccUpdatedOn="";
    
    private String payCommission;
    private int noempMappedinbill;
    private String isbillVerified;
    private String aqmastEmpNm;
    private String aqmastMobile;
    private String offDist;
    private String billgroupId;
    private String taskid;
    
    public int getGrossAmt() {
        return grossAmt;
    }

    public void setGrossAmt(int grossAmt) {
        this.grossAmt = this.grossAmt + grossAmt;
    }

    public int getSlno() {
        return slno;
    }

    public void setSlno(int slno) {
        this.slno = slno;
    }

    public String getAqSlNo() {
        return aqSlNo;
    }

    public void setAqSlNo(String aqSlNo) {
        this.aqSlNo = aqSlNo;
    }

    public BigDecimal getAqGroup() {
        return aqGroup;
    }

    public void setAqGroup(BigDecimal aqGroup) {
        this.aqGroup = aqGroup;
    }

    public String getAqGroupDesc() {
        return aqGroupDesc;
    }

    public void setAqGroupDesc(String aqGroupDesc) {
        this.aqGroupDesc = aqGroupDesc;
    }

    public int getPayMonth() {
        return payMonth;
    }

    public void setPayMonth(int payMonth) {
        this.payMonth = payMonth;
    }

    public int getPayYear() {
        return payYear;
    }

    public void setPayYear(int payYear) {
        this.payYear = payYear;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Date getAqDate() {
        return aqDate;
    }

    public void setAqDate(Date aqDate) {
        this.aqDate = aqDate;
    }

    public int getAqMonth() {
        return aqMonth;
    }

    public void setAqMonth(int aqMonth) {
        this.aqMonth = aqMonth;
    }

    public int getAqYear() {
        return aqYear;
    }

    public void setAqYear(int aqYear) {
        this.aqYear = aqYear;
    }

    public String getAqType() {
        return aqType;
    }

    public void setAqType(String aqType) {
        this.aqType = aqType;
    }

    public String getRefOrder() {
        return refOrder;
    }

    public void setRefOrder(String refOrder) {
        this.refOrder = refOrder;
    }

    public Date getRefDate() {
        return refDate;
    }

    public void setRefDate(Date refDate) {
        this.refDate = refDate;
    }

    public int getAqDay() {
        return aqDay;
    }

    public void setAqDay(int aqDay) {
        this.aqDay = aqDay;
    }

    public int getPayDay() {
        return payDay;
    }

    public void setPayDay(int payDay) {
        this.payDay = payDay;
    }

    public String getMajorHead() {
        return majorHead;
    }

    public void setMajorHead(String majorHead) {
        this.majorHead = majorHead;
    }

    public String getMajorHeadDesc() {
        return majorHeadDesc;
    }

    public void setMajorHeadDesc(String majorHeadDesc) {
        this.majorHeadDesc = majorHeadDesc;
    }

    public String getSubMajorHead() {
        return subMajorHead;
    }

    public void setSubMajorHead(String subMajorHead) {
        this.subMajorHead = subMajorHead;
    }

    public String getSubMajorHeadDesc() {
        return subMajorHeadDesc;
    }

    public void setSubMajorHeadDesc(String subMajorHeadDesc) {
        this.subMajorHeadDesc = subMajorHeadDesc;
    }

    public String getMinorHead() {
        return minorHead;
    }

    public void setMinorHead(String minorHead) {
        this.minorHead = minorHead;
    }

    public String getMinorHeadDesc() {
        return minorHeadDesc;
    }

    public void setMinorHeadDesc(String minorHeadDesc) {
        this.minorHeadDesc = minorHeadDesc;
    }

    public String getSubMinorHead1() {
        return subMinorHead1;
    }

    public void setSubMinorHead1(String subMinorHead1) {
        this.subMinorHead1 = subMinorHead1;
    }

    public String getSubMinorHeadDesc1() {
        return subMinorHeadDesc1;
    }

    public void setSubMinorHeadDesc1(String subMinorHeadDesc1) {
        this.subMinorHeadDesc1 = subMinorHeadDesc1;
    }

    public String getSubMinorHead2() {
        return subMinorHead2;
    }

    public void setSubMinorHead2(String subMinorHead2) {
        this.subMinorHead2 = subMinorHead2;
    }

    public String getSubMinorHeadDesc2() {
        return subMinorHeadDesc2;
    }

    public void setSubMinorHeadDesc2(String subMinorHeadDesc2) {
        this.subMinorHeadDesc2 = subMinorHeadDesc2;
    }

    public String getSubMinorHead3() {
        return subMinorHead3;
    }

    public void setSubMinorHead3(String subMinorHead3) {
        this.subMinorHead3 = subMinorHead3;
    }

    public String getSubMinorHeadDesc3() {
        return subMinorHeadDesc3;
    }

    public void setSubMinorHeadDesc3(String subMinorHeadDesc3) {
        this.subMinorHeadDesc3 = subMinorHeadDesc3;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getAltUnit() {
        return altUnit;
    }

    public void setAltUnit(String altUnit) {
        this.altUnit = altUnit;
    }

    public String getOffCode() {
        return offCode;
    }

    public void setOffCode(String offCode) {
        this.offCode = offCode;
    }

    public String getOffDdo() {
        return offDdo;
    }

    public void setOffDdo(String offDdo) {
        this.offDdo = offDdo;
    }

    public int getSecSlNo() {
        return secSlNo;
    }

    public void setSecSlNo(int secSlNo) {
        this.secSlNo = secSlNo;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getPostSlNo() {
        return postSlNo;
    }

    public void setPostSlNo(int postSlNo) {
        this.postSlNo = postSlNo;
    }

    public String getCurDesg() {
        return curDesg;
    }

    public void setCurDesg(String curDesg) {
        this.curDesg = curDesg;
    }

    public String getCurGrade() {
        return curGrade;
    }

    public void setCurGrade(String curGrade) {
        this.curGrade = curGrade;
    }

    public String getCurLevel() {
        return curLevel;
    }

    public void setCurLevel(String curLevel) {
        this.curLevel = curLevel;
    }

    public String getGazetted() {
        return gazetted;
    }

    public void setGazetted(String gazetted) {
        this.gazetted = gazetted;
    }

    public String getPayScale() {
        return payScale;
    }

    public void setPayScale(String payScale) {
        this.payScale = payScale;
    }

    public long getMonBasic() {
        return monBasic;
    }

    public void setMonBasic(long monBasic) {
        this.monBasic = monBasic;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getGpfType() {
        return gpfType;
    }

    public void setGpfType(String gpfType) {
        this.gpfType = gpfType;
    }

    public String getGpfAccNo() {
        return gpfAccNo;
    }

    public void setGpfAccNo(String gpfAccNo) {
        this.gpfAccNo = gpfAccNo;
    }

    public long getCurBasic() {
        return curBasic;
    }

    public void setCurBasic(long curBasic) {
        this.curBasic = curBasic;
    }

    public int getBillNo() {
        return billNo;
    }

    public void setBillNo(int billNo) {
        this.billNo = billNo;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBankAccNo() {
        return bankAccNo;
    }

    public void setBankAccNo(String bankAccNo) {
        this.bankAccNo = bankAccNo;
    }

    public int getDefaultBank() {
        return defaultBank;
    }

    public void setDefaultBank(int defaultBank) {
        this.defaultBank = defaultBank;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSpcOrdNo() {
        return spcOrdNo;
    }

    public void setSpcOrdNo(String spcOrdNo) {
        this.spcOrdNo = spcOrdNo;
    }

    public Date getSpcOrdDate() {
        return spcOrdDate;
    }

    public void setSpcOrdDate(Date spcOrdDate) {
        this.spcOrdDate = spcOrdDate;
    }

    public String getAcctType() {
        return acctType;
    }

    public void setAcctType(String acctType) {
        this.acctType = acctType;
    }

    public String getCurSpc() {
        return curSpc;
    }

    public void setCurSpc(String curSpc) {
        this.curSpc = curSpc;
    }

    public String getEmpType() {
        return empType;
    }

    public void setEmpType(String empType) {
        this.empType = empType;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getDemandNumber() {
        return demandNumber;
    }

    public void setDemandNumber(String demandNumber) {
        this.demandNumber = demandNumber;
    }

    public String getIfMaintenanceDeduct() {
        return ifMaintenanceDeduct;
    }

    public void setIfMaintenanceDeduct(String ifMaintenanceDeduct) {
        this.ifMaintenanceDeduct = ifMaintenanceDeduct;
    }

    public String getCadreType() {
        return cadreType;
    }

    public void setCadreType(String cadreType) {
        this.cadreType = cadreType;
    }

    public String getDarate() {
        return darate;
    }

    public void setDarate(String darate) {
        this.darate = darate;
    }

    public MultipartFile getBankdocProof() {
        return bankdocProof;
    }

    public void setBankdocProof(MultipartFile bankdocProof) {
        this.bankdocProof = bankdocProof;
    }

    public String getBankaccUpdatedBy() {
        return bankaccUpdatedBy;
    }

    public void setBankaccUpdatedBy(String bankaccUpdatedBy) {
        this.bankaccUpdatedBy = bankaccUpdatedBy;
    }

    public String getBankaccUpdatedOn() {
        return bankaccUpdatedOn;
    }

    public void setBankaccUpdatedOn(String bankaccUpdatedOn) {
        this.bankaccUpdatedOn = bankaccUpdatedOn;
    }

    public String getBankdocProofFilename() {
        return bankdocProofFilename;
    }

    public void setBankdocProofFilename(String bankdocProofFilename) {
        this.bankdocProofFilename = bankdocProofFilename;
    }

    public String getPayCommission() {
        return payCommission;
    }

    public void setPayCommission(String payCommission) {
        this.payCommission = payCommission;
    }

    public int getNoempMappedinbill() {
        return noempMappedinbill;
    }

    public void setNoempMappedinbill(int noempMappedinbill) {
        this.noempMappedinbill = noempMappedinbill;
    }

    public String getIsbillVerified() {
        return isbillVerified;
    }

    public void setIsbillVerified(String isbillVerified) {
        this.isbillVerified = isbillVerified;
    }

    public String getAqmastEmpNm() {
        return aqmastEmpNm;
    }

    public void setAqmastEmpNm(String aqmastEmpNm) {
        this.aqmastEmpNm = aqmastEmpNm;
    }

    public String getAqmastMobile() {
        return aqmastMobile;
    }

    public void setAqmastMobile(String aqmastMobile) {
        this.aqmastMobile = aqmastMobile;
    }

    public String getOffDist() {
        return offDist;
    }

    public void setOffDist(String offDist) {
        this.offDist = offDist;
    }

    public int getDedAmt() {
        return dedAmt;
    }

    public void setDedAmt(int dedAmt) {
        this.dedAmt = dedAmt;
    }

    public int getNetAmt() {
        return netAmt;
    }

    public void setNetAmt(int netAmt) {
        this.netAmt = netAmt;
    }

    public String getBillgroupId() {
        return billgroupId;
    }

    public void setBillgroupId(String billgroupId) {
        this.billgroupId = billgroupId;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

   
    
    
}
