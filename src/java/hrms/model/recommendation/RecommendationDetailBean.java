/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.recommendation;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Manisha
 */
public class RecommendationDetailBean {

    private int recommendationId;
    private int recommendeddetailId;
    private int noofnominations;
    private String foryear;
    private String uploadedPath;
    private String recommendedspc;
    private String recommendedempId;
    private String recommendedoffcode;
    private String recommendeddeptcode;
    private String recommendedpostgrp;
    private String recommendedempGpfNo;
    private String recommendedempname;
    private String recommendedpost;
    private String recommendedempgroup;
    private String recommendedempofficename;
    private String nominatedbyemployee;
    private String approvedbyemployee;
    private String createdondate;
    private String submittedondate;
    private String initiatedByspc;
    private String initiatedByempId;
    private String initiatedByempname;
    private String initiatedBypost;
    private String recommenadationType;
    private String criminalStatus;
    private String recommendationandCommendation;
    private String empExceptionalWork;
    private String empActivitiesandIssue;
    private String reasonforrecommendation;
    private String overallviews;
    private MultipartFile overallviewsdocument;
    private MultipartFile recommendationandCommendationdocument;
    private MultipartFile empExceptionalWorkdocument;
    private MultipartFile otherActivitiesDocument;
    private MultipartFile reasonforrecommendationDocument;
    private String authoritiesoriginalfilename;
    private String exceptionalworkoriginalfilename;
    private String otheractivitiesoriginalfilename;
    private String overallviewsoriginalfilename;
    private String reasonforrecommendationoriginalfilename;
    private String authoritiesdiskfilename;
    private String exceptionalworkdiskfilename;
    private String otheractivitiesdiskfilename;
    private String overallviewsdiskfilename;
    private String reasonforrecommendationdiskfilename;
    private String alreadyAdded;
    private String authoffcode;
    private String authspc;
    private String pendingAtempId;
    private String pendingAtSPC;
    private String submittedtoofftype;
    private String documentTypeName;
    private String isApproved;
    private String isSubmittedToDept;
    private int taskId;
    private String offCode;
    private String departmentName;
    private String dataintegrity;
    private String vigilancediskFileName;
    private String cbdiskFileName;
    private String vigilancenocstatus;
    private String cbranchnocstatus;
    private String cbnocreason;
    private String vigilancenocreason;
    private int nocId;

    public String getForyear() {
        return foryear;
    }

    public void setForyear(String foryear) {
        this.foryear = foryear;
    }

    public int getRecommendationId() {
        return recommendationId;
    }

    public void setRecommendationId(int recommendationId) {
        this.recommendationId = recommendationId;
    }

    public int getRecommendeddetailId() {
        return recommendeddetailId;
    }

    public void setRecommendeddetailId(int recommendeddetailId) {
        this.recommendeddetailId = recommendeddetailId;
    }

    public int getNoofnominations() {
        return noofnominations;
    }

    public void setNoofnominations(int noofnominations) {
        this.noofnominations = noofnominations;
    }

    public String getUploadedPath() {
        return uploadedPath;
    }

    public void setUploadedPath(String uploadedPath) {
        this.uploadedPath = uploadedPath;
    }

    public String getRecommendedspc() {
        return recommendedspc;
    }

    public void setRecommendedspc(String recommendedspc) {
        this.recommendedspc = recommendedspc;
    }

    public String getRecommendedempId() {
        return recommendedempId;
    }

    public void setRecommendedempId(String recommendedempId) {
        this.recommendedempId = recommendedempId;
    }

    public String getRecommendedoffcode() {
        return recommendedoffcode;
    }

    public void setRecommendedoffcode(String recommendedoffcode) {
        this.recommendedoffcode = recommendedoffcode;
    }

    public String getRecommendeddeptcode() {
        return recommendeddeptcode;
    }

    public void setRecommendeddeptcode(String recommendeddeptcode) {
        this.recommendeddeptcode = recommendeddeptcode;
    }

    public String getRecommendedpostgrp() {
        return recommendedpostgrp;
    }

    public void setRecommendedpostgrp(String recommendedpostgrp) {
        this.recommendedpostgrp = recommendedpostgrp;
    }

    public String getRecommendedempGpfNo() {
        return recommendedempGpfNo;
    }

    public void setRecommendedempGpfNo(String recommendedempGpfNo) {
        this.recommendedempGpfNo = recommendedempGpfNo;
    }

    public String getRecommendedempname() {
        return recommendedempname;
    }

    public void setRecommendedempname(String recommendedempname) {
        this.recommendedempname = recommendedempname;
    }

    public String getRecommendedpost() {
        return recommendedpost;
    }

    public void setRecommendedpost(String recommendedpost) {
        this.recommendedpost = recommendedpost;
    }

    public String getRecommendedempgroup() {
        return recommendedempgroup;
    }

    public void setRecommendedempgroup(String recommendedempgroup) {
        this.recommendedempgroup = recommendedempgroup;
    }

    public String getRecommendedempofficename() {
        return recommendedempofficename;
    }

    public void setRecommendedempofficename(String recommendedempofficename) {
        this.recommendedempofficename = recommendedempofficename;
    }

    public String getNominatedbyemployee() {
        return nominatedbyemployee;
    }

    public void setNominatedbyemployee(String nominatedbyemployee) {
        this.nominatedbyemployee = nominatedbyemployee;
    }

    public String getApprovedbyemployee() {
        return approvedbyemployee;
    }

    public void setApprovedbyemployee(String approvedbyemployee) {
        this.approvedbyemployee = approvedbyemployee;
    }

    public String getCreatedondate() {
        return createdondate;
    }

    public void setCreatedondate(String createdondate) {
        this.createdondate = createdondate;
    }

    public String getSubmittedondate() {
        return submittedondate;
    }

    public void setSubmittedondate(String submittedondate) {
        this.submittedondate = submittedondate;
    }

    public String getInitiatedByspc() {
        return initiatedByspc;
    }

    public void setInitiatedByspc(String initiatedByspc) {
        this.initiatedByspc = initiatedByspc;
    }

    public String getInitiatedByempId() {
        return initiatedByempId;
    }

    public void setInitiatedByempId(String initiatedByempId) {
        this.initiatedByempId = initiatedByempId;
    }

    public String getInitiatedByempname() {
        return initiatedByempname;
    }

    public void setInitiatedByempname(String initiatedByempname) {
        this.initiatedByempname = initiatedByempname;
    }

    public String getInitiatedBypost() {
        return initiatedBypost;
    }

    public void setInitiatedBypost(String initiatedBypost) {
        this.initiatedBypost = initiatedBypost;
    }

    public String getRecommenadationType() {
        return recommenadationType;
    }

    public void setRecommenadationType(String recommenadationType) {
        this.recommenadationType = recommenadationType;
    }

    public String getCriminalStatus() {
        return criminalStatus;
    }

    public void setCriminalStatus(String criminalStatus) {
        this.criminalStatus = criminalStatus;
    }

    public String getRecommendationandCommendation() {
        return recommendationandCommendation;
    }

    public void setRecommendationandCommendation(String recommendationandCommendation) {
        this.recommendationandCommendation = recommendationandCommendation;
    }

    public String getEmpExceptionalWork() {
        return empExceptionalWork;
    }

    public void setEmpExceptionalWork(String empExceptionalWork) {
        this.empExceptionalWork = empExceptionalWork;
    }

    public String getEmpActivitiesandIssue() {
        return empActivitiesandIssue;
    }

    public void setEmpActivitiesandIssue(String empActivitiesandIssue) {
        this.empActivitiesandIssue = empActivitiesandIssue;
    }

    public String getReasonforrecommendation() {
        return reasonforrecommendation;
    }

    public void setReasonforrecommendation(String reasonforrecommendation) {
        this.reasonforrecommendation = reasonforrecommendation;
    }

    public String getOverallviews() {
        return overallviews;
    }

    public void setOverallviews(String overallviews) {
        this.overallviews = overallviews;
    }

    public MultipartFile getOverallviewsdocument() {
        return overallviewsdocument;
    }

    public void setOverallviewsdocument(MultipartFile overallviewsdocument) {
        this.overallviewsdocument = overallviewsdocument;
    }

    public MultipartFile getRecommendationandCommendationdocument() {
        return recommendationandCommendationdocument;
    }

    public void setRecommendationandCommendationdocument(MultipartFile recommendationandCommendationdocument) {
        this.recommendationandCommendationdocument = recommendationandCommendationdocument;
    }

    public MultipartFile getEmpExceptionalWorkdocument() {
        return empExceptionalWorkdocument;
    }

    public void setEmpExceptionalWorkdocument(MultipartFile empExceptionalWorkdocument) {
        this.empExceptionalWorkdocument = empExceptionalWorkdocument;
    }

    public MultipartFile getOtherActivitiesDocument() {
        return otherActivitiesDocument;
    }

    public void setOtherActivitiesDocument(MultipartFile otherActivitiesDocument) {
        this.otherActivitiesDocument = otherActivitiesDocument;
    }

    public MultipartFile getReasonforrecommendationDocument() {
        return reasonforrecommendationDocument;
    }

    public void setReasonforrecommendationDocument(MultipartFile reasonforrecommendationDocument) {
        this.reasonforrecommendationDocument = reasonforrecommendationDocument;
    }

    public String getAuthoritiesoriginalfilename() {
        return authoritiesoriginalfilename;
    }

    public void setAuthoritiesoriginalfilename(String authoritiesoriginalfilename) {
        this.authoritiesoriginalfilename = authoritiesoriginalfilename;
    }

    public String getExceptionalworkoriginalfilename() {
        return exceptionalworkoriginalfilename;
    }

    public void setExceptionalworkoriginalfilename(String exceptionalworkoriginalfilename) {
        this.exceptionalworkoriginalfilename = exceptionalworkoriginalfilename;
    }

    public String getOtheractivitiesoriginalfilename() {
        return otheractivitiesoriginalfilename;
    }

    public void setOtheractivitiesoriginalfilename(String otheractivitiesoriginalfilename) {
        this.otheractivitiesoriginalfilename = otheractivitiesoriginalfilename;
    }

    public String getOverallviewsoriginalfilename() {
        return overallviewsoriginalfilename;
    }

    public void setOverallviewsoriginalfilename(String overallviewsoriginalfilename) {
        this.overallviewsoriginalfilename = overallviewsoriginalfilename;
    }

    public String getReasonforrecommendationoriginalfilename() {
        return reasonforrecommendationoriginalfilename;
    }

    public void setReasonforrecommendationoriginalfilename(String reasonforrecommendationoriginalfilename) {
        this.reasonforrecommendationoriginalfilename = reasonforrecommendationoriginalfilename;
    }

    public String getAuthoritiesdiskfilename() {
        return authoritiesdiskfilename;
    }

    public void setAuthoritiesdiskfilename(String authoritiesdiskfilename) {
        this.authoritiesdiskfilename = authoritiesdiskfilename;
    }

    public String getExceptionalworkdiskfilename() {
        return exceptionalworkdiskfilename;
    }

    public void setExceptionalworkdiskfilename(String exceptionalworkdiskfilename) {
        this.exceptionalworkdiskfilename = exceptionalworkdiskfilename;
    }

    public String getOtheractivitiesdiskfilename() {
        return otheractivitiesdiskfilename;
    }

    public void setOtheractivitiesdiskfilename(String otheractivitiesdiskfilename) {
        this.otheractivitiesdiskfilename = otheractivitiesdiskfilename;
    }

    public String getOverallviewsdiskfilename() {
        return overallviewsdiskfilename;
    }

    public void setOverallviewsdiskfilename(String overallviewsdiskfilename) {
        this.overallviewsdiskfilename = overallviewsdiskfilename;
    }

    public String getReasonforrecommendationdiskfilename() {
        return reasonforrecommendationdiskfilename;
    }

    public void setReasonforrecommendationdiskfilename(String reasonforrecommendationdiskfilename) {
        this.reasonforrecommendationdiskfilename = reasonforrecommendationdiskfilename;
    }

    public String getAlreadyAdded() {
        return alreadyAdded;
    }

    public void setAlreadyAdded(String alreadyAdded) {
        this.alreadyAdded = alreadyAdded;
    }

    public String getAuthoffcode() {
        return authoffcode;
    }

    public void setAuthoffcode(String authoffcode) {
        this.authoffcode = authoffcode;
    }

    public String getAuthspc() {
        return authspc;
    }

    public void setAuthspc(String authspc) {
        this.authspc = authspc;
    }

    public String getPendingAtempId() {
        return pendingAtempId;
    }

    public void setPendingAtempId(String pendingAtempId) {
        this.pendingAtempId = pendingAtempId;
    }

    public String getPendingAtSPC() {
        return pendingAtSPC;
    }

    public void setPendingAtSPC(String pendingAtSPC) {
        this.pendingAtSPC = pendingAtSPC;
    }

    public String getSubmittedtoofftype() {
        return submittedtoofftype;
    }

    public void setSubmittedtoofftype(String submittedtoofftype) {
        this.submittedtoofftype = submittedtoofftype;
    }

    public String getDocumentTypeName() {
        return documentTypeName;
    }

    public void setDocumentTypeName(String documentTypeName) {
        this.documentTypeName = documentTypeName;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public String getIsSubmittedToDept() {
        return isSubmittedToDept;
    }

    public void setIsSubmittedToDept(String isSubmittedToDept) {
        this.isSubmittedToDept = isSubmittedToDept;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getOffCode() {
        return offCode;
    }

    public void setOffCode(String offCode) {
        this.offCode = offCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDataintegrity() {
        return dataintegrity;
    }

    public void setDataintegrity(String dataintegrity) {
        this.dataintegrity = dataintegrity;
    }

    public String getVigilancediskFileName() {
        return vigilancediskFileName;
    }

    public void setVigilancediskFileName(String vigilancediskFileName) {
        this.vigilancediskFileName = vigilancediskFileName;
    }

    public String getCbdiskFileName() {
        return cbdiskFileName;
    }

    public void setCbdiskFileName(String cbdiskFileName) {
        this.cbdiskFileName = cbdiskFileName;
    }

    public int getNocId() {
        return nocId;
    }

    public void setNocId(int nocId) {
        this.nocId = nocId;
    }

    public String getVigilancenocstatus() {
        return vigilancenocstatus;
    }

    public void setVigilancenocstatus(String vigilancenocstatus) {
        this.vigilancenocstatus = vigilancenocstatus;
    }

    public String getCbranchnocstatus() {
        return cbranchnocstatus;
    }

    public void setCbranchnocstatus(String cbranchnocstatus) {
        this.cbranchnocstatus = cbranchnocstatus;
    }

    public String getCbnocreason() {
        return cbnocreason;
    }

    public void setCbnocreason(String cbnocreason) {
        this.cbnocreason = cbnocreason;
    }

    public String getVigilancenocreason() {
        return vigilancenocreason;
    }

    public void setVigilancenocreason(String vigilancenocreason) {
        this.vigilancenocreason = vigilancenocreason;
    }

}
