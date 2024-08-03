/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.Rent;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Manoj PC
 */
public class QuarterBean {
    private String applicationId = null;
    private String empId = null;
    private String fullName = null;
    private String fatherName = null;
    private String designation = null;
    private String retSPC = null;
    private String dateOfRetirement = null;
    private String retirementDept = null;
    private String pensionSanctioningAuthority = null;
    private String mobile = null;
    private String email = null;  
    private String sltDept;
    private String sltOffice;
    private String sltPost;
    private String isTwinCity;
    private String tcdesignation;
    private String tcoffice;
    private String isGaQtr;
    private String hasOccupied;
    private String pendingAt;
    private String place;
    private String area;
    private String quarterType;
    private String buildingNo;
    private String address;
    private String outstandingAmount;
    private String hasVacated;
    
    private String forwardingAuthority;
    private String remarks;
    private String hasClearedOutstanding;
    private MultipartFile ccFile;
    private MultipartFile ndcFile;
    private boolean isApplied = false;
    private List quarterList = null;
    private List workflowLogList = null;
    private String isForwarded = null;
    private String isRentOfficer = null;
    private String rentOfficer = null;
    private String applicationStatus = null;
    private String issueType = null;
    private String isNDCGenerated = null;
    private String hasFinalNDC = null;
    private String jsNeverOccupied = null;
    private String jsVacated = null;
    private String jsPendingDues = null;
    private String jsNotVacated = null;
    private String hasVerificationReport = null;
    private String sectionName = null;
    private String gratuityRecovery = null;
    private String recoveryAmount = null;
    private String hasRecoveryIntimation = null;
    private String nondcRemark = null;
    private String hrmsId = null;
    private String gpfNo = null;
    private String psaSpc = null;
    private String psaId = null;
    private String msg = null;
    private String quarterUnit = null;
    private String ledgerAmount = "";
    private String otherAmount = "";
    private String dateCreated = "";
    private String userPassword = "";
    private String newpassword = "";
    private String confirmpassword = "";
    
    
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDateOfRetirement() {
        return dateOfRetirement;
    }

    public void setDateOfRetirement(String dateOfRetirement) {
        this.dateOfRetirement = dateOfRetirement;
    }

    public String getRetirementDept() {
        return retirementDept;
    }

    public void setRetirementDept(String retirementDept) {
        this.retirementDept = retirementDept;
    }

    public String getPensionSanctioningAuthority() {
        return pensionSanctioningAuthority;
    }

    public void setPensionSanctioningAuthority(String pensionSanctioningAuthority) {
        this.pensionSanctioningAuthority = pensionSanctioningAuthority;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSltDept() {
        return sltDept;
    }

    public void setSltDept(String sltDept) {
        this.sltDept = sltDept;
    }

    public String getSltOffice() {
        return sltOffice;
    }

    public void setSltOffice(String sltOffice) {
        this.sltOffice = sltOffice;
    }

    public String getSltPost() {
        return sltPost;
    }

    public void setSltPost(String sltPost) {
        this.sltPost = sltPost;
    }

    public String getIsTwinCity() {
        return isTwinCity;
    }

    public void setIsTwinCity(String isTwinCity) {
        this.isTwinCity = isTwinCity;
    }

    public String getTcdesignation() {
        return tcdesignation;
    }

    public void setTcdesignation(String tcdesignation) {
        this.tcdesignation = tcdesignation;
    }

    public String getTcoffice() {
        return tcoffice;
    }

    public void setTcoffice(String tcoffice) {
        this.tcoffice = tcoffice;
    }

    public String getIsGaQtr() {
        return isGaQtr;
    }

    public void setIsGaQtr(String isGaQtr) {
        this.isGaQtr = isGaQtr;
    }

    public String getHasOccupied() {
        return hasOccupied;
    }

    public void setHasOccupied(String hasOccupied) {
        this.hasOccupied = hasOccupied;
    }

    

    public String getHasClearedOutstanding() {
        return hasClearedOutstanding;
    }

    public void setHasClearedOutstanding(String hasClearedOutstanding) {
        this.hasClearedOutstanding = hasClearedOutstanding;
    }

    public MultipartFile getCcFile() {
        return ccFile;
    }

    public void setCcFile(MultipartFile ccFile) {
        this.ccFile = ccFile;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getQuarterType() {
        return quarterType;
    }

    public void setQuarterType(String quarterType) {
        this.quarterType = quarterType;
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public boolean isIsApplied() {
        return isApplied;
    }

    public void setIsApplied(boolean isApplied) {
        this.isApplied = isApplied;
    } 

    public List getQuarterList() {
        return quarterList;
    }

    public void setQuarterList(List quarterList) {
        this.quarterList = quarterList;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getRetSPC() {
        return retSPC;
    }

    public void setRetSPC(String retSPC) {
        this.retSPC = retSPC;
    }

    public String getForwardingAuthority() {
        return forwardingAuthority;
    }

    public void setForwardingAuthority(String forwardingAuthority) {
        this.forwardingAuthority = forwardingAuthority;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List getWorkflowLogList() {
        return workflowLogList;
    }

    public void setWorkflowLogList(List workflowLogList) {
        this.workflowLogList = workflowLogList;
    }

    public String getIsForwarded() {
        return isForwarded;
    }

    public void setIsForwarded(String isForwarded) {
        this.isForwarded = isForwarded;
    }

    public String getPendingAt() {
        return pendingAt;
    }

    public void setPendingAt(String pendingAt) {
        this.pendingAt = pendingAt;
    }

    public String getIsRentOfficer() {
        return isRentOfficer;
    }

    public void setIsRentOfficer(String isRentOfficer) {
        this.isRentOfficer = isRentOfficer;
    }

    public String getRentOfficer() {
        return rentOfficer;
    }

    public void setRentOfficer(String rentOfficer) {
        this.rentOfficer = rentOfficer;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getIsNDCGenerated() {
        return isNDCGenerated;
    }

    public void setIsNDCGenerated(String isNDCGenerated) {
        this.isNDCGenerated = isNDCGenerated;
    }

    public MultipartFile getNdcFile() {
        return ndcFile;
    }

    public void setNdcFile(MultipartFile ndcFile) {
        this.ndcFile = ndcFile;
    }

    public String getHasFinalNDC() {
        return hasFinalNDC;
    }

    public void setHasFinalNDC(String hasFinalNDC) {
        this.hasFinalNDC = hasFinalNDC;
    }

    public String getJsNeverOccupied() {
        return jsNeverOccupied;
    }

    public void setJsNeverOccupied(String jsNeverOccupied) {
        this.jsNeverOccupied = jsNeverOccupied;
    }

    public String getJsVacated() {
        return jsVacated;
    }

    public void setJsVacated(String jsVacated) {
        this.jsVacated = jsVacated;
    }

    public String getJsPendingDues() {
        return jsPendingDues;
    }

    public void setJsPendingDues(String jsPendingDues) {
        this.jsPendingDues = jsPendingDues;
    }

    public String getJsNotVacated() {
        return jsNotVacated;
    }

    public void setJsNotVacated(String jsNotVacated) {
        this.jsNotVacated = jsNotVacated;
    }

    public String getHasVerificationReport() {
        return hasVerificationReport;
    }

    public void setHasVerificationReport(String hasVerificationReport) {
        this.hasVerificationReport = hasVerificationReport;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getGratuityRecovery() {
        return gratuityRecovery;
    }

    public void setGratuityRecovery(String gratuityRecovery) {
        this.gratuityRecovery = gratuityRecovery;
    }

    public String getRecoveryAmount() {
        return recoveryAmount;
    }

    public void setRecoveryAmount(String recoveryAmount) {
        this.recoveryAmount = recoveryAmount;
    }

    public String getHasRecoveryIntimation() {
        return hasRecoveryIntimation;
    }

    public void setHasRecoveryIntimation(String hasRecoveryIntimation) {
        this.hasRecoveryIntimation = hasRecoveryIntimation;
    }

    public String getNondcRemark() {
        return nondcRemark;
    }

    public void setNondcRemark(String nondcRemark) {
        this.nondcRemark = nondcRemark;
    }

    public String getHrmsId() {
        return hrmsId;
    }

    public void setHrmsId(String hrmsId) {
        this.hrmsId = hrmsId;
    }

    public String getGpfNo() {
        return gpfNo;
    }

    public void setGpfNo(String gpfNo) {
        this.gpfNo = gpfNo;
    }

    public String getPsaSpc() {
        return psaSpc;
    }

    public void setPsaSpc(String psaSpc) {
        this.psaSpc = psaSpc;
    }

    public String getPsaId() {
        return psaId;
    }

    public void setPsaId(String psaId) {
        this.psaId = psaId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(String outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getQuarterUnit() {
        return quarterUnit;
    }

    public void setQuarterUnit(String quarterUnit) {
        this.quarterUnit = quarterUnit;
    }

    public String getHasVacated() {
        return hasVacated;
    }

    public void setHasVacated(String hasVacated) {
        this.hasVacated = hasVacated;
    }

    public String getLedgerAmount() {
        return ledgerAmount;
    }

    public void setLedgerAmount(String ledgerAmount) {
        this.ledgerAmount = ledgerAmount;
    }

    public String getOtherAmount() {
        return otherAmount;
    }

    public void setOtherAmount(String otherAmount) {
        this.otherAmount = otherAmount;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }
}
