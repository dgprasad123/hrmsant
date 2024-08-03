/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.parmast;

import hrms.common.CommonFunctions;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DurgaPrasad
 */
public class ParAdminProperties {

    private String encParId;
    private String encTaskId;
    private String empId = null;
    private String gpfno = null;
    private String empName = null;
    private String postName = null;
    private String dob = null;
    private String groupName = null;
    private String cadreName = null;
    private String cadrecode = null;
    private String currentoffice = null;
    private String mobile = null;
    private String parstatus = null;
    private String pageNo = null;
    private int Parslno;
    private String spc;
    private int parId;
    private String PrdFrmDate;
    private String PrdToDate;
    private String taskId;
    private String parstatusid;
    private String isreview = null;
    private String Parmasterstatus;
    private String iseditable;
    private String Par_master_status;
    private String IsNRCAttchPresent;
    private String fiscalyear;
    private String hrmsId;
    private String apprisespc;
    private int refid;
    private String appriseOffice;
    private String specialcontribution;
    private String selfappraisal;
    private String reason;
    private String place;
    private String submittedon;
    private String empService;
    private String empGroup;
    private String sltAdminRemark;
    private ArrayList achivementList = null;
    private ArrayList reportingdata = null;
    private ArrayList reviewingdata = null;
    private ArrayList acceptingdata = null;
    private ArrayList leaveAbsentee = null;
    private ArrayList absenteeList = null;
    private ArrayList reportingauth = null;
    private ArrayList Reviewingauth = null;
    private ArrayList acceptingauth = null;
    private String isadversed;
    private String currentOfficeName;
    private String Authorityname;
    private String postGroupType;
    private String hasAppraiseeReplyAdverse;
    private String hasAuthorityReplyAdverse;
    private String adverseCommunicationStatusId;
    private String hasSendAppraiseeAdverse;
    private String hasSendAuthorityAdverse;
    private String cadreUpdatedByAdminOnDate;

    private String totalNopostgrouptype;
    private String totalNumberEmployee;
    private String totalParSubmitted;

    private String initiatedByEmpId;
    private String pendingAtEmpId;
    private String pendingAtAuthName;
    private String pendingAtSpc;
    private String parPendingDateFrom;

    private String cadreUpdatedByIp;
    private String cadreUpdatedBy;
    private String nrcDetails;
    private List siParInnerDataList;

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getGpfno() {
        return gpfno;
    }

    public void setGpfno(String gpfno) {
        this.gpfno = gpfno;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCadreName() {
        return cadreName;
    }

    public void setCadreName(String cadreName) {
        this.cadreName = cadreName;
    }

    public String getCadrecode() {
        return cadrecode;
    }

    public void setCadrecode(String cadrecode) {
        this.cadrecode = cadrecode;
    }

    public String getCurrentoffice() {
        return currentoffice;
    }

    public void setCurrentoffice(String currentoffice) {
        this.currentoffice = currentoffice;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getParstatus() {
        return parstatus;
    }

    public void setParstatus(String parstatus) {
        this.parstatus = parstatus;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public int getParslno() {
        return Parslno;
    }

    public void setParslno(int Parslno) {
        this.Parslno = Parslno;
    }

    public String getSpc() {
        return spc;
    }

    public void setSpc(String spc) {
        this.spc = spc;
    }

    public int getParId() {
        return parId;
    }

    public void setParId(int parId) {
        this.parId = parId;
    }

    public String getPrdFrmDate() {
        return PrdFrmDate;
    }

    public void setPrdFrmDate(String PrdFrmDate) {
        this.PrdFrmDate = PrdFrmDate;
    }

    public String getPrdToDate() {
        return PrdToDate;
    }

    public void setPrdToDate(String PrdToDate) {
        this.PrdToDate = PrdToDate;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getParstatusid() {
        return parstatusid;
    }

    public void setParstatusid(String parstatusid) {
        this.parstatusid = parstatusid;
    }

    public String getIsreview() {
        return isreview;
    }

    public void setIsreview(String isreview) {
        this.isreview = isreview;
    }

    public String getParmasterstatus() {
        return Parmasterstatus;
    }

    public void setParmasterstatus(String Parmasterstatus) {
        this.Parmasterstatus = Parmasterstatus;
    }

    public String getIseditable() {
        return iseditable;
    }

    public void setIseditable(String iseditable) {
        this.iseditable = iseditable;
    }

    public String getPar_master_status() {
        return Par_master_status;
    }

    public void setPar_master_status(String Par_master_status) {
        this.Par_master_status = Par_master_status;
    }

    public String getIsNRCAttchPresent() {
        return IsNRCAttchPresent;
    }

    public void setIsNRCAttchPresent(String IsNRCAttchPresent) {
        this.IsNRCAttchPresent = IsNRCAttchPresent;
    }

    public String getFiscalyear() {
        return fiscalyear;
    }

    public void setFiscalyear(String fiscalyear) {
        this.fiscalyear = fiscalyear;
    }

    public String getHrmsId() {
        return hrmsId;
    }

    public void setHrmsId(String hrmsId) {
        this.hrmsId = hrmsId;
    }

    public String getApprisespc() {
        return apprisespc;
    }

    public void setApprisespc(String apprisespc) {
        this.apprisespc = apprisespc;
    }

    public int getRefid() {
        return refid;
    }

    public void setRefid(int refid) {
        this.refid = refid;
    }

    public String getAppriseOffice() {
        return appriseOffice;
    }

    public void setAppriseOffice(String appriseOffice) {
        this.appriseOffice = appriseOffice;
    }

    public String getSpecialcontribution() {
        return specialcontribution;
    }

    public void setSpecialcontribution(String specialcontribution) {
        this.specialcontribution = specialcontribution;
    }

    public String getSelfappraisal() {
        return selfappraisal;
    }

    public void setSelfappraisal(String selfappraisal) {
        this.selfappraisal = selfappraisal;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getSubmittedon() {
        return submittedon;
    }

    public void setSubmittedon(String submittedon) {
        this.submittedon = submittedon;
    }

    public String getEmpService() {
        return empService;
    }

    public void setEmpService(String empService) {
        this.empService = empService;
    }

    public String getEmpGroup() {
        return empGroup;
    }

    public void setEmpGroup(String empGroup) {
        this.empGroup = empGroup;
    }

    public String getSltAdminRemark() {
        return sltAdminRemark;
    }

    public void setSltAdminRemark(String sltAdminRemark) {
        this.sltAdminRemark = sltAdminRemark;
    }

    public ArrayList getAchivementList() {
        return achivementList;
    }

    public void setAchivementList(ArrayList achivementList) {
        this.achivementList = achivementList;
    }

    public ArrayList getReportingdata() {
        return reportingdata;
    }

    public void setReportingdata(ArrayList reportingdata) {
        this.reportingdata = reportingdata;
    }

    public ArrayList getReviewingdata() {
        return reviewingdata;
    }

    public void setReviewingdata(ArrayList reviewingdata) {
        this.reviewingdata = reviewingdata;
    }

    public ArrayList getAcceptingdata() {
        return acceptingdata;
    }

    public void setAcceptingdata(ArrayList acceptingdata) {
        this.acceptingdata = acceptingdata;
    }

    public ArrayList getLeaveAbsentee() {
        return leaveAbsentee;
    }

    public void setLeaveAbsentee(ArrayList leaveAbsentee) {
        this.leaveAbsentee = leaveAbsentee;
    }

    public ArrayList getAbsenteeList() {
        return absenteeList;
    }

    public void setAbsenteeList(ArrayList absenteeList) {
        this.absenteeList = absenteeList;
    }

    public ArrayList getReportingauth() {
        return reportingauth;
    }

    public void setReportingauth(ArrayList reportingauth) {
        this.reportingauth = reportingauth;
    }

    public ArrayList getReviewingauth() {
        return Reviewingauth;
    }

    public void setReviewingauth(ArrayList Reviewingauth) {
        this.Reviewingauth = Reviewingauth;
    }

    public ArrayList getAcceptingauth() {
        return acceptingauth;
    }

    public void setAcceptingauth(ArrayList acceptingauth) {
        this.acceptingauth = acceptingauth;
    }

    public String getIsadversed() {
        return isadversed;
    }

    public void setIsadversed(String isadversed) {
        this.isadversed = isadversed;
    }

    public String getCurrentOfficeName() {
        return currentOfficeName;
    }

    public void setCurrentOfficeName(String currentOfficeName) {
        this.currentOfficeName = currentOfficeName;
    }

    public String getAuthorityname() {
        return Authorityname;
    }

    public void setAuthorityname(String Authorityname) {
        this.Authorityname = Authorityname;
    }

    public String getPostGroupType() {
        return postGroupType;
    }

    public void setPostGroupType(String postGroupType) {
        this.postGroupType = postGroupType;
    }

    public String getHasAppraiseeReplyAdverse() {
        return hasAppraiseeReplyAdverse;
    }

    public void setHasAppraiseeReplyAdverse(String hasAppraiseeReplyAdverse) {
        this.hasAppraiseeReplyAdverse = hasAppraiseeReplyAdverse;
    }

    public String getHasAuthorityReplyAdverse() {
        return hasAuthorityReplyAdverse;
    }

    public void setHasAuthorityReplyAdverse(String hasAuthorityReplyAdverse) {
        this.hasAuthorityReplyAdverse = hasAuthorityReplyAdverse;
    }

    public String getAdverseCommunicationStatusId() {
        return adverseCommunicationStatusId;
    }

    public void setAdverseCommunicationStatusId(String adverseCommunicationStatusId) {
        this.adverseCommunicationStatusId = adverseCommunicationStatusId;
    }

    public String getHasSendAppraiseeAdverse() {
        return hasSendAppraiseeAdverse;
    }

    public void setHasSendAppraiseeAdverse(String hasSendAppraiseeAdverse) {
        this.hasSendAppraiseeAdverse = hasSendAppraiseeAdverse;
    }

    public String getHasSendAuthorityAdverse() {
        return hasSendAuthorityAdverse;
    }

    public void setHasSendAuthorityAdverse(String hasSendAuthorityAdverse) {
        this.hasSendAuthorityAdverse = hasSendAuthorityAdverse;
    }

    public String getCadreUpdatedByAdminOnDate() {
        return cadreUpdatedByAdminOnDate;
    }

    public void setCadreUpdatedByAdminOnDate(String cadreUpdatedByAdminOnDate) {
        this.cadreUpdatedByAdminOnDate = cadreUpdatedByAdminOnDate;
    }

    public String getTotalNopostgrouptype() {
        return totalNopostgrouptype;
    }

    public void setTotalNopostgrouptype(String totalNopostgrouptype) {
        this.totalNopostgrouptype = totalNopostgrouptype;
    }

    public String getTotalNumberEmployee() {
        return totalNumberEmployee;
    }

    public void setTotalNumberEmployee(String totalNumberEmployee) {
        this.totalNumberEmployee = totalNumberEmployee;
    }

    public String getTotalParSubmitted() {
        return totalParSubmitted;
    }

    public void setTotalParSubmitted(String totalParSubmitted) {
        this.totalParSubmitted = totalParSubmitted;
    }

    public String getInitiatedByEmpId() {
        return initiatedByEmpId;
    }

    public void setInitiatedByEmpId(String initiatedByEmpId) {
        this.initiatedByEmpId = initiatedByEmpId;
    }

    public String getPendingAtEmpId() {
        return pendingAtEmpId;
    }

    public void setPendingAtEmpId(String pendingAtEmpId) {
        this.pendingAtEmpId = pendingAtEmpId;
    }

    public String getPendingAtAuthName() {
        return pendingAtAuthName;
    }

    public void setPendingAtAuthName(String pendingAtAuthName) {
        this.pendingAtAuthName = pendingAtAuthName;
    }

    public String getPendingAtSpc() {
        return pendingAtSpc;
    }

    public void setPendingAtSpc(String pendingAtSpc) {
        this.pendingAtSpc = pendingAtSpc;
    }

    public String getParPendingDateFrom() {
        return parPendingDateFrom;
    }

    public void setParPendingDateFrom(String parPendingDateFrom) {
        this.parPendingDateFrom = parPendingDateFrom;
    }

    public String getCadreUpdatedByIp() {
        return cadreUpdatedByIp;
    }

    public void setCadreUpdatedByIp(String cadreUpdatedByIp) {
        this.cadreUpdatedByIp = cadreUpdatedByIp;
    }

    public String getCadreUpdatedBy() {
        return cadreUpdatedBy;
    }

    public void setCadreUpdatedBy(String cadreUpdatedBy) {
        this.cadreUpdatedBy = cadreUpdatedBy;
    }

    public String getEncParId() {
        return encParId;
    }

    public void setEncParId(String encParId) throws Exception {
        this.encParId = CommonFunctions.encodedTxt(encParId + "");
    }

    public String getEncTaskId() {
        return encTaskId;
    }

    public void setEncTaskId(String encTaskId) throws Exception {
        this.encTaskId = CommonFunctions.encodedTxt(encTaskId + "");
    }

    public String getNrcDetails() {
        return nrcDetails;
    }

    public void setNrcDetails(String nrcDetails) {
        this.nrcDetails = nrcDetails;
    }

    public List getSiParInnerDataList() {
        return siParInnerDataList;
    }

    public void setSiParInnerDataList(List siParInnerDataList) {
        this.siParInnerDataList = siParInnerDataList;
    }

}
