/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.policemodule;

import java.io.Serializable;

/**
 *
 * @author Surendra
 */
public class EmployeeDetailsForRank implements Serializable {

    private String empName = "";
    private String empId = "";
    private String gpfno = "";
    private String dob = "";
    private String dos = "";
    private String doj = "";
    private String formCompletionStatus = "";
    private String recommendationStatus = "";
    private String fathersName = "";

    private String sltActionName = "";

    private String sltpostName = "";
    private String sltNominationForPost = "";
    private String createdOn = "";
    private String submittedOn = "";
     
    private String submittedToOffice = "";
    private String submittedStatusForFieldOffice = "";

    private String submittedByOfficeCode = "";
    private String submittedByOffice = "";
    private String forwardedToOffice = "";
    private String forwardedOn = "";
    private String forwardedStatus = "";
    private String approvedBy = "";
    private String approvedOn = "";
    private String approvedStatus = "";

    private String nominationMasterId = "";
    private String nominationDetailId = "";
    private String sltRangeOffice = "";
    private String nominationId = "";

    private String nominatedEmployeeCount;
    private String cadreName;
    private String cadreCode;
    private int nominationFormId;
    private String fiscalyear;

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

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

    public String getDoj() {
        return doj;
    }

    public void setDoj(String doj) {
        this.doj = doj;
    }

    public String getSltpostName() {
        return sltpostName;
    }

    public void setSltpostName(String sltpostName) {
        this.sltpostName = sltpostName;
    }

    public String getSltNominationForPost() {
        return sltNominationForPost;
    }

    public void setSltNominationForPost(String sltNominationForPost) {
        this.sltNominationForPost = sltNominationForPost;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getSubmittedOn() {
        return submittedOn;
    }

    public void setSubmittedOn(String submittedOn) {
        this.submittedOn = submittedOn;
    }

    public String getSubmittedByOfficeCode() {
        return submittedByOfficeCode;
    }

    public void setSubmittedByOfficeCode(String submittedByOfficeCode) {
        this.submittedByOfficeCode = submittedByOfficeCode;
    }

    
    

    public String getSubmittedToOffice() {
        return submittedToOffice;
    }

    public void setSubmittedToOffice(String submittedToOffice) {
        this.submittedToOffice = submittedToOffice;
    }

    public String getNominationMasterId() {
        return nominationMasterId;
    }

    public void setNominationMasterId(String nominationMasterId) {
        this.nominationMasterId = nominationMasterId;
    }

    public String getNominationDetailId() {
        return nominationDetailId;
    }

    public void setNominationDetailId(String nominationDetailId) {
        this.nominationDetailId = nominationDetailId;
    }

    public String getFormCompletionStatus() {
        return formCompletionStatus;
    }

    public void setFormCompletionStatus(String formCompletionStatus) {
        this.formCompletionStatus = formCompletionStatus;
    }

    public String getSltRangeOffice() {
        return sltRangeOffice;
    }

    public void setSltRangeOffice(String sltRangeOffice) {
        this.sltRangeOffice = sltRangeOffice;
    }

    public String getNominationId() {
        return nominationId;
    }

    public void setNominationId(String nominationId) {
        this.nominationId = nominationId;
    }

    public String getSubmittedStatusForFieldOffice() {
        return submittedStatusForFieldOffice;
    }

    public void setSubmittedStatusForFieldOffice(String submittedStatusForFieldOffice) {
        this.submittedStatusForFieldOffice = submittedStatusForFieldOffice;
    }

    public String getSubmittedByOffice() {
        return submittedByOffice;
    }

    public void setSubmittedByOffice(String submittedByOffice) {
        this.submittedByOffice = submittedByOffice;
    }

    public String getForwardedToOffice() {
        return forwardedToOffice;
    }

    public void setForwardedToOffice(String forwardedToOffice) {
        this.forwardedToOffice = forwardedToOffice;
    }

    public String getForwardedOn() {
        return forwardedOn;
    }

    public void setForwardedOn(String forwardedOn) {
        this.forwardedOn = forwardedOn;
    }

    public String getForwardedStatus() {
        return forwardedStatus;
    }

    public void setForwardedStatus(String forwardedStatus) {
        this.forwardedStatus = forwardedStatus;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getApprovedOn() {
        return approvedOn;
    }

    public void setApprovedOn(String approvedOn) {
        this.approvedOn = approvedOn;
    }

    public String getApprovedStatus() {
        return approvedStatus;
    }

    public void setApprovedStatus(String approvedStatus) {
        this.approvedStatus = approvedStatus;
    }

    public String getRecommendationStatus() {
        return recommendationStatus;
    }

    public void setRecommendationStatus(String recommendationStatus) {
        this.recommendationStatus = recommendationStatus;
    }

    public String getNominatedEmployeeCount() {
        return nominatedEmployeeCount;
    }

    public void setNominatedEmployeeCount(String nominatedEmployeeCount) {
        this.nominatedEmployeeCount = nominatedEmployeeCount;
    }

    public String getFathersName() {
        return fathersName;
    }

    public void setFathersName(String fathersName) {
        this.fathersName = fathersName;
    }

    public String getSltActionName() {
        return sltActionName;
    }

    public void setSltActionName(String sltActionName) {
        this.sltActionName = sltActionName;
    }

    public String getCadreName() {
        return cadreName;
    }

    public void setCadreName(String cadreName) {
        this.cadreName = cadreName;
    }

    public String getCadreCode() {
        return cadreCode;
    }

    public void setCadreCode(String cadreCode) {
        this.cadreCode = cadreCode;
    }

    public int getNominationFormId() {
        return nominationFormId;
    }

    public void setNominationFormId(int nominationFormId) {
        this.nominationFormId = nominationFormId;
    }

    public String getFiscalyear() {
        return fiscalyear;
    }

    public void setFiscalyear(String fiscalyear) {
        this.fiscalyear = fiscalyear;
    }

   
}
