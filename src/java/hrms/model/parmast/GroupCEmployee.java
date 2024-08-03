/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.parmast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author manisha
 */
public class GroupCEmployee {

    public final static int PARC_PROMOTION_SUBMITTED_BY_REPORTING_AUTHORITY = 84;
    public final static int PARC_PROMOTION_RETURN_BY_REVIEWING_AUTHORITY = 114;
    public final static int PARC_PROMOTION_RETURN_BY_ACCEPTING_AUTHORITY = 115;
    public final static int FORWARDED_TO_ACCEPTING_AUTHORITY = 91;
    private String remarkauthoritytype;
    private String reviewedspc;
    private String reviewedempId;
    private String reviewedempname;
    private String reviewedpost;
    private String reportingondate;
    private String reviewingondate;
    private String acceptingondate;
    private String reviewingempId;
    private String reviewingspc;
    private String reviewingempname;
    private String reviewingpost;
    private String isfitforShoulderingResponsibilityReviewing;
    private String reviewingRemarks;
    private String isfitforShoulderingResponsibilityReporting;
    private String reportingRemarks;
    private byte[] filecontent = null;
    private String diskFileName;
    private String originalFilename;
    private String getContentType;
    private MultipartFile uploadDocument;
    private String reportingempId;
    private String reportingspc;
    private String reportingempname;
    private String reportingpost;
    private String isfitforShoulderingResponsibilityAccepting;
    private String acceptingRemarks;
    private String acceptingempId;
    private String acceptingspc;
    private String acceptingempname;
    private String acceptingpost;
    private int promotionId;
    private int groupCpromotionId;
    private int taskId;
    private String mode;
    private String appraiseRemarks;
    private String fiscalyear;
    private String alreadyAdded;
    private String mobileNo;
    private String periodFromReporting;
    private String periodToReporting;
    private String pendingAtEmpId;
    private int statusId;

    private String revertremarks;
    private String revertdone = null;
    private String spc;
    private String offcode;
    private String offName;
    private String distName;
    private String distCode;
    private String pageNo = null;
    private String gpfno = null;
    private String dob = null;
    private String reviewedEmpCurrentoffice;
    private String mobile;
    private String groupCstatus = null;
    
    private String hidAuthOffCode;
    private String priviligeAuhName;
    private String priviligeAuhDesignation;
    private String groupTypeOfAuthority;
    
    private String periodFromReviewing;
    private String periodToReviewing;
    private String assessmentTypeReporting;
    private String hidpromotionId;
    

    public String getRemarkauthoritytype() {
        return remarkauthoritytype;
    }

    public void setRemarkauthoritytype(String remarkauthoritytype) {
        this.remarkauthoritytype = remarkauthoritytype;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getReviewedspc() {
        return reviewedspc;
    }

    public void setReviewedspc(String reviewedspc) {
        this.reviewedspc = reviewedspc;
    }

    public String getReviewedempId() {
        return reviewedempId;
    }

    public void setReviewedempId(String reviewedempId) {
        this.reviewedempId = reviewedempId;
    }

    public String getReviewedempname() {
        return reviewedempname;
    }

    public void setReviewedempname(String reviewedempname) {
        this.reviewedempname = reviewedempname;
    }

    public String getReviewedpost() {
        return reviewedpost;
    }

    public void setReviewedpost(String reviewedpost) {
        this.reviewedpost = reviewedpost;
    }

    public String getReportingondate() {
        return reportingondate;
    }

    public void setReportingondate(String reportingondate) {
        this.reportingondate = reportingondate;
    }

    public String getReviewingondate() {
        return reviewingondate;
    }

    public void setReviewingondate(String reviewingondate) {
        this.reviewingondate = reviewingondate;
    }

    public String getAcceptingondate() {
        return acceptingondate;
    }

    public void setAcceptingondate(String acceptingondate) {
        this.acceptingondate = acceptingondate;
    }

    public String getReviewingempId() {
        return reviewingempId;
    }

    public void setReviewingempId(String reviewingempId) {
        this.reviewingempId = reviewingempId;
    }

    public String getReviewingspc() {
        return reviewingspc;
    }

    public void setReviewingspc(String reviewingspc) {
        this.reviewingspc = reviewingspc;
    }

    public String getReviewingempname() {
        return reviewingempname;
    }

    public void setReviewingempname(String reviewingempname) {
        this.reviewingempname = reviewingempname;
    }

    public String getReviewingpost() {
        return reviewingpost;
    }

    public void setReviewingpost(String reviewingpost) {
        this.reviewingpost = reviewingpost;
    }

    public String getIsfitforShoulderingResponsibilityReviewing() {
        return isfitforShoulderingResponsibilityReviewing;
    }

    public void setIsfitforShoulderingResponsibilityReviewing(String isfitforShoulderingResponsibilityReviewing) {
        this.isfitforShoulderingResponsibilityReviewing = isfitforShoulderingResponsibilityReviewing;
    }

    public String getReviewingRemarks() {
        return reviewingRemarks;
    }

    public void setReviewingRemarks(String reviewingRemarks) {
        this.reviewingRemarks = reviewingRemarks;
    }

    public String getIsfitforShoulderingResponsibilityReporting() {
        return isfitforShoulderingResponsibilityReporting;
    }

    public void setIsfitforShoulderingResponsibilityReporting(String isfitforShoulderingResponsibilityReporting) {
        this.isfitforShoulderingResponsibilityReporting = isfitforShoulderingResponsibilityReporting;
    }

    public String getReportingRemarks() {
        return reportingRemarks;
    }

    public void setReportingRemarks(String reportingRemarks) {
        this.reportingRemarks = reportingRemarks;
    }

    public byte[] getFilecontent() {
        return filecontent;
    }

    public void setFilecontent(byte[] filecontent) {
        this.filecontent = filecontent;
    }

    public String getDiskFileName() {
        return diskFileName;
    }

    public void setDiskFileName(String diskFileName) {
        this.diskFileName = diskFileName;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getGetContentType() {
        return getContentType;
    }

    public void setGetContentType(String getContentType) {
        this.getContentType = getContentType;
    }

    public MultipartFile getUploadDocument() {
        return uploadDocument;
    }

    public void setUploadDocument(MultipartFile uploadDocument) {
        this.uploadDocument = uploadDocument;
    }

    public String getReportingempId() {
        return reportingempId;
    }

    public void setReportingempId(String reportingempId) {
        this.reportingempId = reportingempId;
    }

    public String getReportingspc() {
        return reportingspc;
    }

    public void setReportingspc(String reportingspc) {
        this.reportingspc = reportingspc;
    }

    public String getReportingempname() {
        return reportingempname;
    }

    public void setReportingempname(String reportingempname) {
        this.reportingempname = reportingempname;
    }

    public String getReportingpost() {
        return reportingpost;
    }

    public void setReportingpost(String reportingpost) {
        this.reportingpost = reportingpost;
    }

    public String getIsfitforShoulderingResponsibilityAccepting() {
        return isfitforShoulderingResponsibilityAccepting;
    }

    public void setIsfitforShoulderingResponsibilityAccepting(String isfitforShoulderingResponsibilityAccepting) {
        this.isfitforShoulderingResponsibilityAccepting = isfitforShoulderingResponsibilityAccepting;
    }

    public String getAcceptingRemarks() {
        return acceptingRemarks;
    }

    public void setAcceptingRemarks(String acceptingRemarks) {
        this.acceptingRemarks = acceptingRemarks;
    }

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public int getGroupCpromotionId() {
        return groupCpromotionId;
    }

    public void setGroupCpromotionId(int groupCpromotionId) {
        this.groupCpromotionId = groupCpromotionId;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getAcceptingempId() {
        return acceptingempId;
    }

    public void setAcceptingempId(String acceptingempId) {
        this.acceptingempId = acceptingempId;
    }

    public String getAcceptingspc() {
        return acceptingspc;
    }

    public void setAcceptingspc(String acceptingspc) {
        this.acceptingspc = acceptingspc;
    }

    public String getAcceptingempname() {
        return acceptingempname;
    }

    public void setAcceptingempname(String acceptingempname) {
        this.acceptingempname = acceptingempname;
    }

    public String getAcceptingpost() {
        return acceptingpost;
    }

    public void setAcceptingpost(String acceptingpost) {
        this.acceptingpost = acceptingpost;
    }

    public String getAppraiseRemarks() {
        return appraiseRemarks;
    }

    public void setAppraiseRemarks(String appraiseRemarks) {
        this.appraiseRemarks = appraiseRemarks;
    }

    public String getFiscalyear() {
        return fiscalyear;
    }

    public void setFiscalyear(String fiscalyear) {
        this.fiscalyear = fiscalyear;
    }

    public String getAlreadyAdded() {
        return alreadyAdded;
    }

    public void setAlreadyAdded(String alreadyAdded) {
        this.alreadyAdded = alreadyAdded;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPeriodFromReporting() {
        return periodFromReporting;
    }

    public void setPeriodFromReporting(String periodFromReporting) {
        this.periodFromReporting = periodFromReporting;
    }

    public String getPeriodToReporting() {
        return periodToReporting;
    }

    public void setPeriodToReporting(String periodToReporting) {
        this.periodToReporting = periodToReporting;
    }

    

    public String getPendingAtEmpId() {
        return pendingAtEmpId;
    }

    public void setPendingAtEmpId(String pendingAtEmpId) {
        this.pendingAtEmpId = pendingAtEmpId;
    }

    public String getRevertremarks() {
        return revertremarks;
    }

    public void setRevertremarks(String revertremarks) {
        this.revertremarks = revertremarks;
    }

    public String getRevertdone() {
        return revertdone;
    }

    public void setRevertdone(String revertdone) {
        this.revertdone = revertdone;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getSpc() {
        return spc;
    }

    public void setSpc(String spc) {
        this.spc = spc;
    }

    public String getOffcode() {
        return offcode;
    }

    public void setOffcode(String offcode) {
        this.offcode = offcode;
    }

    public String getOffName() {
        return offName;
    }

    public void setOffName(String offName) {
        this.offName = offName;
    }

    public String getDistName() {
        return distName;
    }

    public void setDistName(String distName) {
        this.distName = distName;
    }

    public String getDistCode() {
        return distCode;
    }

    public void setDistCode(String distCode) {
        this.distCode = distCode;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
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

    public String getReviewedEmpCurrentoffice() {
        return reviewedEmpCurrentoffice;
    }

    public void setReviewedEmpCurrentoffice(String reviewedEmpCurrentoffice) {
        this.reviewedEmpCurrentoffice = reviewedEmpCurrentoffice;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGroupCstatus() {
        return groupCstatus;
    }

    public void setGroupCstatus(String groupCstatus) {
        this.groupCstatus = groupCstatus;
    }

    public String getHidAuthOffCode() {
        return hidAuthOffCode;
    }

    public void setHidAuthOffCode(String hidAuthOffCode) {
        this.hidAuthOffCode = hidAuthOffCode;
    }

    public String getPriviligeAuhName() {
        return priviligeAuhName;
    }

    public void setPriviligeAuhName(String priviligeAuhName) {
        this.priviligeAuhName = priviligeAuhName;
    }

    public String getPriviligeAuhDesignation() {
        return priviligeAuhDesignation;
    }

    public void setPriviligeAuhDesignation(String priviligeAuhDesignation) {
        this.priviligeAuhDesignation = priviligeAuhDesignation;
    }

    public String getGroupTypeOfAuthority() {
        return groupTypeOfAuthority;
    }

    public void setGroupTypeOfAuthority(String groupTypeOfAuthority) {
        this.groupTypeOfAuthority = groupTypeOfAuthority;
    }

    public String getPeriodFromReviewing() {
        return periodFromReviewing;
    }

    public void setPeriodFromReviewing(String periodFromReviewing) {
        this.periodFromReviewing = periodFromReviewing;
    }

    public String getPeriodToReviewing() {
        return periodToReviewing;
    }

    public void setPeriodToReviewing(String periodToReviewing) {
        this.periodToReviewing = periodToReviewing;
    }

    public String getAssessmentTypeReporting() {
        return assessmentTypeReporting;
    }

    public void setAssessmentTypeReporting(String assessmentTypeReporting) {
        this.assessmentTypeReporting = assessmentTypeReporting;
    }

    public String getHidpromotionId() {
        return hidpromotionId;
    }

    public void setHidpromotionId(String hidpromotionId) {
        this.hidpromotionId = hidpromotionId;
    }

   
}
