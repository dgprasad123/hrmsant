/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.report.annualestablishmentreport;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Surendra
 */
public class AnnualEstablishment {

    private String roleType;
    private String trCode;
    private String trName;
    private int totalOffice;
    private int totpostinAerreport = 0;
    private String showApproveLink = "";
    private int reportId = 0;
    private int serialno = 0;
    private String postname = "";
    private String gpc = "";
    private String group = "";
    private String scaleofPay = "";
    private String level = "";
    private int sanctionedStrength = 0;
    private int meninPosition = 0;
    private int vacancyPosition = 0;
    private String scaleofPay7th = "";
    private String financialYear = "";
    private String offCode = "";
    private String gp = "";
    private int aerId = 0;
    private String controllingSpc = "";
    private String fileName = "";
    private String status = "";
    private String fy = "";
    private int taskid = 0;
    private String submittedDate = "";

    private String hidDeptCode;

    private String hidOffCode;
    private String singleCO;

    private String groupAData;
    private String groupBData;
    private String groupCData;
    private String groupDData;
    private String grantInAid;
    private int total;

    private String groupADataSystem;
    private String groupBDataSystem;
    private String groupCDataSystem;
    private String groupDDataSystem;
    private String grantInAidSystem;

    private String otherCategory;
    private String otherPost;
    private int otherSS;
    private int otherVacancy;
    private String otherRemarks;
    private String other6thPay;
    private String other7thPay;
    private String partType;
    private String cadreType = "";
    private String operatoroffName = "";

    private int aerOtherId;

    private BigDecimal[] billGrpId;

    private String revertReason;

    private String postgrp;
    private String paylevel;
    private String hidPostGrp;

    private String departmentname = null;
    private String deptCode = null;
    private String ddoCode = null;
    private int totalDDO;
    private int ddoPrepared;
    private int billPrepared;
    private int ddoSubmitted;
    private int billSubmitted;
    private int tokenPrepared;

    private int totalEmp;
    private int totalCompleted;
    private int totalVerified;

    private String empName = null;
    private String empPost = null;
    private String isCompleted = null;
    private String isVerified = null;
    private String isRegular = null;
    private String empId = null;

    private String coStatus = null;

    private int operatorSubmitted;
    private int approverSubmitted;
    private int reviewerSubmitted;
    private int verifierSubmitted;
    private int acceptorSubmitted;
    private String offName = null;

    private String groupNameA;
    private String groupNameB;
    private String groupNameC;
    private String groupNameD;
    private String groupNameO;
    private int totalACnt;
    private int totalBCnt;
    private int totalCCnt;
    private int totalDCnt;
    private int totalOCnt;
    private int totalallCnt;
    private int grantinAid;
    private int grandTotal;
    private int demandNo;

    private int termId;

    private String chkGpc;
    private String hiddenGPC;
    private String goNo;
    private String goDate;
    private String postTerminated;
    private String dateTerminated;
    private String remarks;

    private String hiddenpostName;
    private String hiddengp;
    private String hiddePayScale;

    private String chkReportId;

    private String notermPOst;

//    private String coStatus=null;
    private List aerWiseDDO;
    private int proposalId;
    private String isAoApproved;

    private int partD;
    private int partE;
    
    private String sltCadre;
    
    private String coOffCode;
    private String coaerid;
    
    private String aoOffCode;
    private String aoaerid;
    
    private String coCode;
    
    private String showDisApproveLink;
    private String sectionName;
    private String billGrpName;
    private String dupGpc=null;
    private String dupPostname=null;
    private String gpfNo=null;
    
    public List getAerWiseDDO() {
        return aerWiseDDO;
    }

    public void setAerWiseDDO(List aerWiseDDO) {
        this.aerWiseDDO = aerWiseDDO;
    }

    public String getCoStatus() {
        return coStatus;
    }

    public void setCoStatus(String coStatus) {
        this.coStatus = coStatus;
    }

    public int getDemandNo() {
        return demandNo;
    }

    public void setDemandNo(int demandNo) {
        this.demandNo = demandNo;
    }

    public String getGroupNameA() {
        return groupNameA;
    }

    public void setGroupNameA(String groupNameA) {
        this.groupNameA = groupNameA;
    }

    public String getGroupNameB() {
        return groupNameB;
    }

    public void setGroupNameB(String groupNameB) {
        this.groupNameB = groupNameB;
    }

    public String getGroupNameC() {
        return groupNameC;
    }

    public void setGroupNameC(String groupNameC) {
        this.groupNameC = groupNameC;
    }

    public String getGroupNameD() {
        return groupNameD;
    }

    public void setGroupNameD(String groupNameD) {
        this.groupNameD = groupNameD;
    }

    public int getTotalACnt() {
        return totalACnt;
    }

    public void setTotalACnt(int totalACnt) {
        this.totalACnt = totalACnt;
    }

    public int getTotalBCnt() {
        return totalBCnt;
    }

    public void setTotalBCnt(int totalBCnt) {
        this.totalBCnt = totalBCnt;
    }

    public int getTotalCCnt() {
        return totalCCnt;
    }

    public void setTotalCCnt(int totalCCnt) {
        this.totalCCnt = totalCCnt;
    }

    public int getTotalDCnt() {
        return totalDCnt;
    }

    public void setTotalDCnt(int totalDCnt) {
        this.totalDCnt = totalDCnt;
    }

    public int getTotalallCnt() {
        return totalallCnt;
    }

    public void setTotalallCnt(int totalallCnt) {
        this.totalallCnt = totalallCnt;
    }

    public int getGrantinAid() {
        return grantinAid;
    }

    public void setGrantinAid(int grantinAid) {
        this.grantinAid = grantinAid;
    }

    public int getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(int grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getTrCode() {
        return trCode;
    }

    public void setTrCode(String trCode) {
        this.trCode = trCode;
    }

    public String getTrName() {
        return trName;
    }

    public void setTrName(String trName) {
        this.trName = trName;
    }

    public int getTotalOffice() {
        return totalOffice;
    }

    public void setTotalOffice(int totalOffice) {
        this.totalOffice = totalOffice;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getOffName() {
        return offName;
    }

    public void setOffName(String offName) {
        this.offName = offName;
    }

    public String getGp() {
        return gp;
    }

    public void setGp(String gp) {
        this.gp = gp;
    }

    public int getSerialno() {
        return serialno;
    }

    public void setSerialno(int serialno) {
        this.serialno = serialno;
    }

    public String getPostname() {
        return postname;
    }

    public void setPostname(String postname) {
        this.postname = postname;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getScaleofPay() {
        return scaleofPay;
    }

    public void setScaleofPay(String scaleofPay) {
        this.scaleofPay = scaleofPay;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getSanctionedStrength() {
        return sanctionedStrength;
    }

    public void setSanctionedStrength(int sanctionedStrength) {
        this.sanctionedStrength = sanctionedStrength;
    }

    public int getMeninPosition() {
        return meninPosition;
    }

    public void setMeninPosition(int meninPosition) {
        this.meninPosition = meninPosition;
    }

    public int getVacancyPosition() {
        return vacancyPosition;
    }

    public void setVacancyPosition(int vacancyPosition) {
        this.vacancyPosition = vacancyPosition;
    }

    public String getScaleofPay7th() {
        return scaleofPay7th;
    }

    public void setScaleofPay7th(String scaleofPay7th) {
        this.scaleofPay7th = scaleofPay7th;
    }

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getGpc() {
        return gpc;
    }

    public void setGpc(String gpc) {
        this.gpc = gpc;
    }

    public String getOffCode() {
        return offCode;
    }

    public void setOffCode(String offCode) {
        this.offCode = offCode;
    }

    public int getAerId() {
        return aerId;
    }

    public void setAerId(int aerId) {
        this.aerId = aerId;
    }

    public String getControllingSpc() {
        return controllingSpc;
    }

    public void setControllingSpc(String controllingSpc) {
        this.controllingSpc = controllingSpc;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFy() {
        return fy;
    }

    public void setFy(String fy) {
        this.fy = fy;
    }

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public String getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(String submittedDate) {
        this.submittedDate = submittedDate;
    }

    public String getHidDeptCode() {
        return hidDeptCode;
    }

    public void setHidDeptCode(String hidDeptCode) {
        this.hidDeptCode = hidDeptCode;
    }

    public String getHidOffCode() {
        return hidOffCode;
    }

    public void setHidOffCode(String hidOffCode) {
        this.hidOffCode = hidOffCode;
    }

    public String getGroupAData() {
        return groupAData;
    }

    public void setGroupAData(String groupAData) {
        this.groupAData = groupAData;
    }

    public String getGroupBData() {
        return groupBData;
    }

    public void setGroupBData(String groupBData) {
        this.groupBData = groupBData;
    }

    public String getGroupCData() {
        return groupCData;
    }

    public void setGroupCData(String groupCData) {
        this.groupCData = groupCData;
    }

    public String getGroupDData() {
        return groupDData;
    }

    public void setGroupDData(String groupDData) {
        this.groupDData = groupDData;
    }

    public String getGrantInAid() {
        return grantInAid;
    }

    public void setGrantInAid(String grantInAid) {
        this.grantInAid = grantInAid;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public BigDecimal[] getBillGrpId() {
        return billGrpId;
    }

    public void setBillGrpId(BigDecimal[] billGrpId) {
        this.billGrpId = billGrpId;
    }

    public String getSingleCO() {
        return singleCO;
    }

    public void setSingleCO(String singleCO) {
        this.singleCO = singleCO;
    }

    public String getRevertReason() {
        return revertReason;
    }

    public void setRevertReason(String revertReason) {
        this.revertReason = revertReason;
    }

    public String getGroupADataSystem() {
        return groupADataSystem;
    }

    public void setGroupADataSystem(String groupADataSystem) {
        this.groupADataSystem = groupADataSystem;
    }

    public String getGroupBDataSystem() {
        return groupBDataSystem;
    }

    public void setGroupBDataSystem(String groupBDataSystem) {
        this.groupBDataSystem = groupBDataSystem;
    }

    public String getGroupCDataSystem() {
        return groupCDataSystem;
    }

    public void setGroupCDataSystem(String groupCDataSystem) {
        this.groupCDataSystem = groupCDataSystem;
    }

    public String getGroupDDataSystem() {
        return groupDDataSystem;
    }

    public void setGroupDDataSystem(String groupDDataSystem) {
        this.groupDDataSystem = groupDDataSystem;
    }

    public String getGrantInAidSystem() {
        return grantInAidSystem;
    }

    public void setGrantInAidSystem(String grantInAidSystem) {
        this.grantInAidSystem = grantInAidSystem;
    }

    public String getOtherCategory() {
        return otherCategory;
    }

    public void setOtherCategory(String otherCategory) {
        this.otherCategory = otherCategory;
    }

    public String getOtherPost() {
        return otherPost;
    }

    public void setOtherPost(String otherPost) {
        this.otherPost = otherPost;
    }

    public int getOtherSS() {
        return otherSS;
    }

    public void setOtherSS(int otherSS) {
        this.otherSS = otherSS;
    }

    public int getOtherVacancy() {
        return otherVacancy;
    }

    public void setOtherVacancy(int otherVacancy) {
        this.otherVacancy = otherVacancy;
    }

    public String getOtherRemarks() {
        return otherRemarks;
    }

    public void setOtherRemarks(String otherRemarks) {
        this.otherRemarks = otherRemarks;
    }

    public String getOther6thPay() {
        return other6thPay;
    }

    public void setOther6thPay(String other6thPay) {
        this.other6thPay = other6thPay;
    }

    public String getOther7thPay() {
        return other7thPay;
    }

    public void setOther7thPay(String other7thPay) {
        this.other7thPay = other7thPay;
    }

    public int getAerOtherId() {
        return aerOtherId;
    }

    public void setAerOtherId(int aerOtherId) {
        this.aerOtherId = aerOtherId;
    }

    public String getPartType() {
        return partType;
    }

    public void setPartType(String partType) {
        this.partType = partType;
    }

    public String getPostgrp() {
        return postgrp;
    }

    public void setPostgrp(String postgrp) {
        this.postgrp = postgrp;
    }

    public String getPaylevel() {
        return paylevel;
    }

    public void setPaylevel(String paylevel) {
        this.paylevel = paylevel;
    }

    public String getHidPostGrp() {
        return hidPostGrp;
    }

    public void setHidPostGrp(String hidPostGrp) {
        this.hidPostGrp = hidPostGrp;
    }

    public int getTotpostinAerreport() {
        return totpostinAerreport;
    }

    public void setTotpostinAerreport(int totpostinAerreport) {
        this.totpostinAerreport = totpostinAerreport;
    }

    public String getShowApproveLink() {
        return showApproveLink;
    }

    public void setShowApproveLink(String showApproveLink) {
        this.showApproveLink = showApproveLink;
    }

    public String getCadreType() {
        return cadreType;
    }

    public void setCadreType(String cadreType) {
        this.cadreType = cadreType;
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

    public String getDdoCode() {
        return ddoCode;
    }

    public void setDdoCode(String ddoCode) {
        this.ddoCode = ddoCode;
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

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
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

    public String getOperatoroffName() {
        return operatoroffName;
    }

    public void setOperatoroffName(String operatoroffName) {
        this.operatoroffName = operatoroffName;
    }

    public String getChkGpc() {
        return chkGpc;
    }

    public void setChkGpc(String chkGpc) {
        this.chkGpc = chkGpc;
    }

    public String getHiddenGPC() {
        return hiddenGPC;
    }

    public void setHiddenGPC(String hiddenGPC) {
        this.hiddenGPC = hiddenGPC;
    }

    public String getGoNo() {
        return goNo;
    }

    public void setGoNo(String goNo) {
        this.goNo = goNo;
    }

    public String getGoDate() {
        return goDate;
    }

    public void setGoDate(String goDate) {
        this.goDate = goDate;
    }

    public String getPostTerminated() {
        return postTerminated;
    }

    public void setPostTerminated(String postTerminated) {
        this.postTerminated = postTerminated;
    }

    public String getDateTerminated() {
        return dateTerminated;
    }

    public void setDateTerminated(String dateTerminated) {
        this.dateTerminated = dateTerminated;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getChkReportId() {
        return chkReportId;
    }

    public void setChkReportId(String chkReportId) {
        this.chkReportId = chkReportId;
    }

    public String getHiddenpostName() {
        return hiddenpostName;
    }

    public void setHiddenpostName(String hiddenpostName) {
        this.hiddenpostName = hiddenpostName;
    }

    public String getHiddengp() {
        return hiddengp;
    }

    public void setHiddengp(String hiddengp) {
        this.hiddengp = hiddengp;
    }

    public String getHiddePayScale() {
        return hiddePayScale;
    }

    public void setHiddePayScale(String hiddePayScale) {
        this.hiddePayScale = hiddePayScale;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public String getNotermPOst() {
        return notermPOst;
    }

    public void setNotermPOst(String notermPOst) {
        this.notermPOst = notermPOst;
    }

    public int getProposalId() {
        return proposalId;
    }

    public void setProposalId(int proposalId) {
        this.proposalId = proposalId;
    }

    public String getIsAoApproved() {
        return isAoApproved;
    }

    public void setIsAoApproved(String isAoApproved) {
        this.isAoApproved = isAoApproved;
    }

    public String getGroupNameO() {
        return groupNameO;
    }

    public void setGroupNameO(String groupNameO) {
        this.groupNameO = groupNameO;
    }

    public int getTotalOCnt() {
        return totalOCnt;
    }

    public void setTotalOCnt(int totalOCnt) {
        this.totalOCnt = totalOCnt;
    }

    public int getPartD() {
        return partD;
    }

    public void setPartD(int partD) {
        this.partD = partD;
    }

    public int getPartE() {
        return partE;
    }

    public void setPartE(int partE) {
        this.partE = partE;
    }

    public String getSltCadre() {
        return sltCadre;
    }

    public void setSltCadre(String sltCadre) {
        this.sltCadre = sltCadre;
    }

    public String getCoOffCode() {
        return coOffCode;
    }

    public void setCoOffCode(String coOffCode) {
        this.coOffCode = coOffCode;
    }

    public String getCoaerid() {
        return coaerid;
    }

    public void setCoaerid(String coaerid) {
        this.coaerid = coaerid;
    }

    public String getAoOffCode() {
        return aoOffCode;
    }

    public void setAoOffCode(String aoOffCode) {
        this.aoOffCode = aoOffCode;
    }

    public String getAoaerid() {
        return aoaerid;
    }

    public void setAoaerid(String aoaerid) {
        this.aoaerid = aoaerid;
    }

    public String getCoCode() {
        return coCode;
    }

    public void setCoCode(String coCode) {
        this.coCode = coCode;
    }

    public String getShowDisApproveLink() {
        return showDisApproveLink;
    }

    public void setShowDisApproveLink(String showDisApproveLink) {
        this.showDisApproveLink = showDisApproveLink;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getBillGrpName() {
        return billGrpName;
    }

    public void setBillGrpName(String billGrpName) {
        this.billGrpName = billGrpName;
    }

    public String getDupGpc() {
        return dupGpc;
    }

    public void setDupGpc(String dupGpc) {
        this.dupGpc = dupGpc;
    }

    public String getDupPostname() {
        return dupPostname;
    }

    public void setDupPostname(String dupPostname) {
        this.dupPostname = dupPostname;
    }

    public String getGpfNo() {
        return gpfNo;
    }

    public void setGpfNo(String gpfNo) {
        this.gpfNo = gpfNo;
    }

   
    
}
