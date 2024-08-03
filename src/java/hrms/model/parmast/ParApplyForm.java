/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.parmast;

import hrms.common.CommonFunctions;
import java.util.ArrayList;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author manisha
 */
public class ParApplyForm {

    private int parId;
    private String encryptedParid;
    private Integer taskId;
    private String encryptedTaskid;
    private int refid;
    private int parstatus;
    private String ishideremark = "N";
    private String applicantempid = null;
    private String applicant = null;
    private String apprisespn = null;
    private String apprisespc = null;
    private String loginId = null;
    private String authNote = null;
    private ArrayList actionTypeArrList = null;
    private String txtSancAuthority = null;
    private String sltActionType = null;
    private String fiscalYear = null;
    private ArrayList leaveAbsentee = null;
    private ArrayList achivementList = null;
    private String selfappraisal = null;
    private String specialcontribution = null;
    private String reasonId;
    private String reason = null;
    private String place = null;
    private String dob = null;
    private String submitted_on = null;
    private String empService = null;
    private String empGroup = null;
    private String empDesg = null;
    private String empOffice = null;
    private String isreportingcompleted = null;
    private String reportingempid = null;
    private String reviewingNote = null;
    private String isreviewingcompleted = null;
    private String acceptingNote = null;
    private String isacceptingcompleted = null;
    private String urlName = null;
    private String gradingNote = null;
    private ArrayList gradingArr = null;
    private String sltHeadQuarter = null;

    private int ratingattitude;
    private int ratingcoordination;
    private int ratingresponsibility;
    private int teamworkrating;
    private int ratingcomskill;
    private int ratingitskill;
    private int ratingleadership;
    private int ratinginitiative;
    private int ratingdecisionmaking;
    private int ratequalityofwork;
    private String inadequaciesNote = null;
    private String integrityNote = null;
    private String sltGrading = null;
    private String reportinggradename = null;
    private String hasadminPriv = null;
    private String sltReviewGrading = null;
    private ArrayList reportingauth = null;
    private ArrayList reviewingauth = null;
    private ArrayList acceptingauth = null;

    private ArrayList reportingdata = null;
    private ArrayList reviewingdata = null;
    private ArrayList acceptingdata = null;

    private String sltAdminRemark = null;
    private String adminRemark = null;

    private String txtOrdNo = null;
    private String txtOrdDate = null;
    private String txtareaNote = null;
    private String rdAuthority = null;

    private String adversecommunicationsent = null;

    private String isparadmin = null;

    private String empId = null;
    private String empName = null;
    private String appriseOffice;
    private String submittedon;

    private String PrdFrmDate;
    private String PrdToDate;
    private String cadrecode;
    private String cadrename;
    private String postGroup;
    private String spc;
    private String roleId;
    private String offcode;

    private String isreviewed;
    private String reviewedondate;
    private String reviewedby;
    private String reviewedbyspc;

    private String nrcreason = null;
    private String nrcremarks = null;

    private String fiveTChartertenpercent;
    private String fiveTCharterfivePercent;
    private String fiveTComponentmoSarkar;

    private String fiveTComponentappraise;
    private String submitedonNRC;
    private String sltAcceptingGrading;
    private String acceptingempid;
    private String acceptingSpc;
    private int pactid;
    private String isadversed;
    private int adverseCommunicationStatusId;
    private String offcodeforofficewise;
    private String authorizationType;
    private String isForceForward;
    private String isForceForwardFromReviewing;
    private String isCompletedReviewing;
    
    private String parType;
    private String placeOfPostingSi;
    private String siType;
    
    private int ratingAttitudeStScSection;
    private int ratingQualityOfOutput;
    private int ratingeffectivenessHandlingWork;
    
    private String penPictureOfOficerNote;
    private String stateOfHealth;
    private String sickReportOnDate;
    private String sickDetails;
    
    private int totalofA;
    private int totalofB;
    private int totalofC;
    private int totalofD;
    
    private String originalFileNamegradingDocumentReporting;
    private String diskFileNameforgradingDocumentReporting;
    private String fileTypeforgradingDocumentReporting;
    
    private String originalFileNamegradingDocumentReviewing;
    private String diskFileNameforgradingDocumentReviewing;
    private String fileTypeforgradingDocumentReviewing;
    
    private String originalFileNamegradingDocumentAccepting;
    private String diskFileNameforgradingDocumentAccepting;
    private String fileTypeforgradingDocumentAccepting;
    
    private String loginUserType;
    private String downloadedById;
    private String DownloadedByIp;
    private String isPARClosed;
    private String isAuthRemarksClosed;
    private String downloadedByName;
    
    private String NrcReasonDetail;
    
    private String loginByIp;
    private String loginById;
    
    

    public String getIshideremark() {
        return ishideremark;
    }

    public void setIshideremark(String ishideremark) {
        this.ishideremark = ishideremark;
    }

    public int getParId() {
        return parId;
    }

    public void setParId(int parId) {
        this.parId = parId;
    }

    public int getRefid() {
        return refid;
    }

    public void setRefid(int refid) {
        this.refid = refid;
    }

    public int getParstatus() {
        return parstatus;
    }

    public void setParstatus(int parstatus) {
        this.parstatus = parstatus;
    }

    public String getApplicantempid() {
        return applicantempid;
    }

    public void setApplicantempid(String applicantempid) {
        this.applicantempid = applicantempid;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getApprisespn() {
        return apprisespn;
    }

    public void setApprisespn(String apprisespn) {
        this.apprisespn = apprisespn;
    }

    public String getApprisespc() {
        return apprisespc;
    }

    public void setApprisespc(String apprisespc) {
        this.apprisespc = apprisespc;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getAuthNote() {
        return authNote;
    }

    public void setAuthNote(String authNote) {
        this.authNote = authNote;
    }

    public ArrayList getActionTypeArrList() {
        return actionTypeArrList;
    }

    public void setActionTypeArrList(ArrayList actionTypeArrList) {
        this.actionTypeArrList = actionTypeArrList;
    }

    public String getTxtSancAuthority() {
        return txtSancAuthority;
    }

    public void setTxtSancAuthority(String txtSancAuthority) {
        this.txtSancAuthority = txtSancAuthority;
    }

    public String getSltActionType() {
        return sltActionType;
    }

    public void setSltActionType(String sltActionType) {
        this.sltActionType = sltActionType;
    }

    public String getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(String fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public ArrayList getLeaveAbsentee() {
        return leaveAbsentee;
    }

    public void setLeaveAbsentee(ArrayList leaveAbsentee) {
        this.leaveAbsentee = leaveAbsentee;
    }

    public ArrayList getAchivementList() {
        return achivementList;
    }

    public void setAchivementList(ArrayList achivementList) {
        this.achivementList = achivementList;
    }

    public String getSelfappraisal() {
        return selfappraisal;
    }

    public void setSelfappraisal(String selfappraisal) {
        this.selfappraisal = selfappraisal;
    }

    public String getSpecialcontribution() {
        return specialcontribution;
    }

    public void setSpecialcontribution(String specialcontribution) {
        this.specialcontribution = specialcontribution;
    }

    public String getReasonId() {
        return reasonId;
    }

    public void setReasonId(String reasonId) {
        this.reasonId = reasonId;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getSubmitted_on() {
        return submitted_on;
    }

    public void setSubmitted_on(String submitted_on) {
        this.submitted_on = submitted_on;
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

    public String getEmpDesg() {
        return empDesg;
    }

    public void setEmpDesg(String empDesg) {
        this.empDesg = empDesg;
    }

    public String getEmpOffice() {
        return empOffice;
    }

    public void setEmpOffice(String empOffice) {
        this.empOffice = empOffice;
    }

    public String getIsreportingcompleted() {
        return isreportingcompleted;
    }

    public void setIsreportingcompleted(String isreportingcompleted) {
        this.isreportingcompleted = isreportingcompleted;
    }

    public String getReportingempid() {
        return reportingempid;
    }

    public void setReportingempid(String reportingempid) {
        this.reportingempid = reportingempid;
    }

    public String getReviewingNote() {
        return reviewingNote;
    }

    public void setReviewingNote(String reviewingNote) {
        this.reviewingNote = reviewingNote;
    }

    public String getIsreviewingcompleted() {
        return isreviewingcompleted;
    }

    public void setIsreviewingcompleted(String isreviewingcompleted) {
        this.isreviewingcompleted = isreviewingcompleted;
    }

    public String getAcceptingNote() {
        return acceptingNote;
    }

    public void setAcceptingNote(String acceptingNote) {
        this.acceptingNote = acceptingNote;
    }

    public String getIsacceptingcompleted() {
        return isacceptingcompleted;
    }

    public void setIsacceptingcompleted(String isacceptingcompleted) {
        this.isacceptingcompleted = isacceptingcompleted;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    public String getGradingNote() {
        return gradingNote;
    }

    public void setGradingNote(String gradingNote) {
        this.gradingNote = gradingNote;
    }

    public ArrayList getGradingArr() {
        return gradingArr;
    }

    public void setGradingArr(ArrayList gradingArr) {
        this.gradingArr = gradingArr;
    }

    public String getSltHeadQuarter() {
        return sltHeadQuarter;
    }

    public void setSltHeadQuarter(String sltHeadQuarter) {
        this.sltHeadQuarter = sltHeadQuarter;
    }

    public int getRatingattitude() {
        return ratingattitude;
    }

    public void setRatingattitude(int ratingattitude) {
        this.ratingattitude = ratingattitude;
    }

    public int getRatingcoordination() {
        return ratingcoordination;
    }

    public void setRatingcoordination(int ratingcoordination) {
        this.ratingcoordination = ratingcoordination;
    }

    public int getRatingresponsibility() {
        return ratingresponsibility;
    }

    public void setRatingresponsibility(int ratingresponsibility) {
        this.ratingresponsibility = ratingresponsibility;
    }

    public int getTeamworkrating() {
        return teamworkrating;
    }

    public void setTeamworkrating(int teamworkrating) {
        this.teamworkrating = teamworkrating;
    }

    public int getRatingcomskill() {
        return ratingcomskill;
    }

    public void setRatingcomskill(int ratingcomskill) {
        this.ratingcomskill = ratingcomskill;
    }

    public int getRatingitskill() {
        return ratingitskill;
    }

    public void setRatingitskill(int ratingitskill) {
        this.ratingitskill = ratingitskill;
    }

    public int getRatingleadership() {
        return ratingleadership;
    }

    public void setRatingleadership(int ratingleadership) {
        this.ratingleadership = ratingleadership;
    }

    public int getRatinginitiative() {
        return ratinginitiative;
    }

    public void setRatinginitiative(int ratinginitiative) {
        this.ratinginitiative = ratinginitiative;
    }

    public int getRatingdecisionmaking() {
        return ratingdecisionmaking;
    }

    public void setRatingdecisionmaking(int ratingdecisionmaking) {
        this.ratingdecisionmaking = ratingdecisionmaking;
    }

    public int getRatequalityofwork() {
        return ratequalityofwork;
    }

    public void setRatequalityofwork(int ratequalityofwork) {
        this.ratequalityofwork = ratequalityofwork;
    }

    public String getInadequaciesNote() {
        return inadequaciesNote;
    }

    public void setInadequaciesNote(String inadequaciesNote) {
        this.inadequaciesNote = inadequaciesNote;
    }

    public String getIntegrityNote() {
        return integrityNote;
    }

    public void setIntegrityNote(String integrityNote) {
        this.integrityNote = integrityNote;
    }

    public String getSltGrading() {
        return sltGrading;
    }

    public void setSltGrading(String sltGrading) {
        this.sltGrading = sltGrading;
    }

    public String getReportinggradename() {
        return reportinggradename;
    }

    public void setReportinggradename(String reportinggradename) {
        this.reportinggradename = reportinggradename;
    }

    public String getHasadminPriv() {
        return hasadminPriv;
    }

    public void setHasadminPriv(String hasadminPriv) {
        this.hasadminPriv = hasadminPriv;
    }

    public String getSltReviewGrading() {
        return sltReviewGrading;
    }

    public void setSltReviewGrading(String sltReviewGrading) {
        this.sltReviewGrading = sltReviewGrading;
    }

    public ArrayList getReportingauth() {
        return reportingauth;
    }

    public void setReportingauth(ArrayList reportingauth) {
        this.reportingauth = reportingauth;
    }

    public ArrayList getReviewingauth() {
        return reviewingauth;
    }

    public void setReviewingauth(ArrayList reviewingauth) {
        this.reviewingauth = reviewingauth;
    }

    public ArrayList getAcceptingauth() {
        return acceptingauth;
    }

    public void setAcceptingauth(ArrayList acceptingauth) {
        this.acceptingauth = acceptingauth;
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

    public String getSltAdminRemark() {
        return sltAdminRemark;
    }

    public void setSltAdminRemark(String sltAdminRemark) {
        this.sltAdminRemark = sltAdminRemark;
    }

    public String getAdminRemark() {
        return adminRemark;
    }

    public void setAdminRemark(String adminRemark) {
        this.adminRemark = adminRemark;
    }

    public String getTxtOrdNo() {
        return txtOrdNo;
    }

    public void setTxtOrdNo(String txtOrdNo) {
        this.txtOrdNo = txtOrdNo;
    }

    public String getTxtOrdDate() {
        return txtOrdDate;
    }

    public void setTxtOrdDate(String txtOrdDate) {
        this.txtOrdDate = txtOrdDate;
    }

    public String getTxtareaNote() {
        return txtareaNote;
    }

    public void setTxtareaNote(String txtareaNote) {
        this.txtareaNote = txtareaNote;
    }

    public String getRdAuthority() {
        return rdAuthority;
    }

    public void setRdAuthority(String rdAuthority) {
        this.rdAuthority = rdAuthority;
    }

    public String getAdversecommunicationsent() {
        return adversecommunicationsent;
    }

    public void setAdversecommunicationsent(String adversecommunicationsent) {
        this.adversecommunicationsent = adversecommunicationsent;
    }

    public String getIsparadmin() {
        return isparadmin;
    }

    public void setIsparadmin(String isparadmin) {
        this.isparadmin = isparadmin;
    }

    public Integer getTaskId() {
        if (this.taskId == null) {
            this.taskId = 0;
        }
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getAppriseOffice() {
        return appriseOffice;
    }

    public void setAppriseOffice(String appriseOffice) {
        this.appriseOffice = appriseOffice;
    }

    public String getSubmittedon() {
        return submittedon;
    }

    public void setSubmittedon(String submittedon) {
        this.submittedon = submittedon;
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

    public String getCadrecode() {
        return cadrecode;
    }

    public void setCadrecode(String cadrecode) {
        this.cadrecode = cadrecode;
    }

    public String getCadrename() {
        return cadrename;
    }

    public void setCadrename(String cadrename) {
        this.cadrename = cadrename;
    }

    public String getPostGroup() {
        return postGroup;
    }

    public void setPostGroup(String postGroup) {
        this.postGroup = postGroup;
    }

    

    public String getSpc() {
        return spc;
    }

    public void setSpc(String spc) {
        this.spc = spc;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getOffcode() {
        return offcode;
    }

    public void setOffcode(String offcode) {
        this.offcode = offcode;
    }

    public String getReviewedondate() {
        return reviewedondate;
    }

    public void setReviewedondate(String reviewed_ondate) {
        this.reviewedondate = reviewed_ondate;
    }

    public String getIsreviewed() {
        return isreviewed;
    }

    public void setIsreviewed(String isreviewed) {
        this.isreviewed = isreviewed;
    }

    public String getReviewedby() {
        return reviewedby;
    }

    public void setReviewedby(String reviewedby) {
        this.reviewedby = reviewedby;
    }

    public String getReviewedbyspc() {
        return reviewedbyspc;
    }

    public void setReviewedbyspc(String reviewedbyspc) {
        this.reviewedbyspc = reviewedbyspc;
    }

    public String getNrcreason() {
        return nrcreason;
    }

    public void setNrcreason(String nrcreason) {
        this.nrcreason = nrcreason;
    }

    public String getNrcremarks() {
        return nrcremarks;
    }

    public void setNrcremarks(String nrcremarks) {
        this.nrcremarks = nrcremarks;
    }

    public String getFiveTChartertenpercent() {
        return fiveTChartertenpercent;
    }

    public void setFiveTChartertenpercent(String fiveTChartertenpercent) {
        this.fiveTChartertenpercent = fiveTChartertenpercent;
    }

    public String getFiveTCharterfivePercent() {
        return fiveTCharterfivePercent;
    }

    public void setFiveTCharterfivePercent(String fiveTCharterfivePercent) {
        this.fiveTCharterfivePercent = fiveTCharterfivePercent;
    }

    public String getFiveTComponentmoSarkar() {
        return fiveTComponentmoSarkar;
    }

    public void setFiveTComponentmoSarkar(String fiveTComponentmoSarkar) {
        this.fiveTComponentmoSarkar = fiveTComponentmoSarkar;
    }

    public String getFiveTComponentappraise() {
        return fiveTComponentappraise;
    }

    public void setFiveTComponentappraise(String fiveTComponentappraise) {
        this.fiveTComponentappraise = fiveTComponentappraise;
    }

    public String getSubmitedonNRC() {
        return submitedonNRC;
    }

    public void setSubmitedonNRC(String submitedonNRC) {
        this.submitedonNRC = submitedonNRC;
    }

    public String getSltAcceptingGrading() {
        return sltAcceptingGrading;
    }

    public void setSltAcceptingGrading(String sltAcceptingGrading) {
        this.sltAcceptingGrading = sltAcceptingGrading;
    }

    public String getAcceptingempid() {
        return acceptingempid;
    }

    public void setAcceptingempid(String acceptingempid) {
        this.acceptingempid = acceptingempid;
    }

    public String getAcceptingSpc() {
        return acceptingSpc;
    }

    public void setAcceptingSpc(String acceptingSpc) {
        this.acceptingSpc = acceptingSpc;
    }

    public int getPactid() {
        return pactid;
    }

    public void setPactid(int pactid) {
        this.pactid = pactid;
    }

    public String getIsadversed() {
        return isadversed;
    }

    public void setIsadversed(String isadversed) {
        this.isadversed = isadversed;
    }

    public int getAdverseCommunicationStatusId() {
        return adverseCommunicationStatusId;
    }

    public void setAdverseCommunicationStatusId(int adverseCommunicationStatusId) {
        this.adverseCommunicationStatusId = adverseCommunicationStatusId;
    }

    public String getOffcodeforofficewise() {
        return offcodeforofficewise;
    }

    public void setOffcodeforofficewise(String offcodeforofficewise) {
        this.offcodeforofficewise = offcodeforofficewise;
    }

    public String getAuthorizationType() {
        return authorizationType;
    }

    public void setAuthorizationType(String authorizationType) {
        this.authorizationType = authorizationType;
    }

    public String getIsForceForward() {
        return isForceForward;
    }

    public void setIsForceForward(String isForceForward) {
        this.isForceForward = isForceForward;
    }

    public String getIsForceForwardFromReviewing() {
        return isForceForwardFromReviewing;
    }

    public void setIsForceForwardFromReviewing(String isForceForwardFromReviewing) {
        this.isForceForwardFromReviewing = isForceForwardFromReviewing;
    }

    public String getIsCompletedReviewing() {
        return isCompletedReviewing;
    }

    public void setIsCompletedReviewing(String isCompletedReviewing) {
        this.isCompletedReviewing = isCompletedReviewing;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public String getParType() {
        return parType;
    }

    public void setParType(String parType) {
        this.parType = parType;
    }

    public String getPlaceOfPostingSi() {
        return placeOfPostingSi;
    }

    public void setPlaceOfPostingSi(String placeOfPostingSi) {
        this.placeOfPostingSi = placeOfPostingSi;
    }

    public String getSiType() {
        return siType;
    }

    public void setSiType(String siType) {
        this.siType = siType;
    }

    public int getRatingAttitudeStScSection() {
        return ratingAttitudeStScSection;
    }

    public void setRatingAttitudeStScSection(int ratingAttitudeStScSection) {
        this.ratingAttitudeStScSection = ratingAttitudeStScSection;
    }

    public int getRatingQualityOfOutput() {
        return ratingQualityOfOutput;
    }

    public void setRatingQualityOfOutput(int ratingQualityOfOutput) {
        this.ratingQualityOfOutput = ratingQualityOfOutput;
    }

    public int getRatingeffectivenessHandlingWork() {
        return ratingeffectivenessHandlingWork;
    }

    public void setRatingeffectivenessHandlingWork(int ratingeffectivenessHandlingWork) {
        this.ratingeffectivenessHandlingWork = ratingeffectivenessHandlingWork;
    }

    public String getPenPictureOfOficerNote() {
        return penPictureOfOficerNote;
    }

    public void setPenPictureOfOficerNote(String penPictureOfOficerNote) {
        this.penPictureOfOficerNote = penPictureOfOficerNote;
    }

    public String getStateOfHealth() {
        return stateOfHealth;
    }

    public void setStateOfHealth(String stateOfHealth) {
        this.stateOfHealth = stateOfHealth;
    }

    public String getSickReportOnDate() {
        return sickReportOnDate;
    }

    public void setSickReportOnDate(String sickReportOnDate) {
        this.sickReportOnDate = sickReportOnDate;
    }

    public String getSickDetails() {
        return sickDetails;
    }

    public void setSickDetails(String sickDetails) {
        this.sickDetails = sickDetails;
    }

    public int getTotalofA() {
        return totalofA;
    }

    public void setTotalofA(int totalofA) {
        this.totalofA = totalofA;
    }

    public int getTotalofB() {
        return totalofB;
    }

    public void setTotalofB(int totalofB) {
        this.totalofB = totalofB;
    }

    public int getTotalofC() {
        return totalofC;
    }

    public void setTotalofC(int totalofC) {
        this.totalofC = totalofC;
    }

    public int getTotalofD() {
        return totalofD;
    }

    public void setTotalofD(int totalofD) {
        this.totalofD = totalofD;
    }

    public String getEncryptedParid() {
        return encryptedParid;
    }

    public void setEncryptedParid(String encryptedParid) throws Exception {
        //this.encryptedParid = CommonFunctions.encodedTxt(encryptedParid + "");
        this.encryptedParid = encryptedParid;
    }

    public String getEncryptedTaskid() {
        return encryptedTaskid;
    }

    public void setEncryptedTaskid(String encryptedTaskid) throws Exception {
        //this.encryptedTaskid = CommonFunctions.encodedTxt(encryptedTaskid + "");
        this.encryptedTaskid = encryptedTaskid;
    }

    public String getOriginalFileNamegradingDocumentReporting() {
        return originalFileNamegradingDocumentReporting;
    }

    public void setOriginalFileNamegradingDocumentReporting(String originalFileNamegradingDocumentReporting) {
        this.originalFileNamegradingDocumentReporting = originalFileNamegradingDocumentReporting;
    }

    public String getDiskFileNameforgradingDocumentReporting() {
        return diskFileNameforgradingDocumentReporting;
    }

    public void setDiskFileNameforgradingDocumentReporting(String diskFileNameforgradingDocumentReporting) {
        this.diskFileNameforgradingDocumentReporting = diskFileNameforgradingDocumentReporting;
    }

    public String getFileTypeforgradingDocumentReporting() {
        return fileTypeforgradingDocumentReporting;
    }

    public void setFileTypeforgradingDocumentReporting(String fileTypeforgradingDocumentReporting) {
        this.fileTypeforgradingDocumentReporting = fileTypeforgradingDocumentReporting;
    }

    public String getOriginalFileNamegradingDocumentReviewing() {
        return originalFileNamegradingDocumentReviewing;
    }

    public void setOriginalFileNamegradingDocumentReviewing(String originalFileNamegradingDocumentReviewing) {
        this.originalFileNamegradingDocumentReviewing = originalFileNamegradingDocumentReviewing;
    }

    public String getDiskFileNameforgradingDocumentReviewing() {
        return diskFileNameforgradingDocumentReviewing;
    }

    public void setDiskFileNameforgradingDocumentReviewing(String diskFileNameforgradingDocumentReviewing) {
        this.diskFileNameforgradingDocumentReviewing = diskFileNameforgradingDocumentReviewing;
    }

    public String getFileTypeforgradingDocumentReviewing() {
        return fileTypeforgradingDocumentReviewing;
    }

    public void setFileTypeforgradingDocumentReviewing(String fileTypeforgradingDocumentReviewing) {
        this.fileTypeforgradingDocumentReviewing = fileTypeforgradingDocumentReviewing;
    }

    public String getOriginalFileNamegradingDocumentAccepting() {
        return originalFileNamegradingDocumentAccepting;
    }

    public void setOriginalFileNamegradingDocumentAccepting(String originalFileNamegradingDocumentAccepting) {
        this.originalFileNamegradingDocumentAccepting = originalFileNamegradingDocumentAccepting;
    }

    public String getDiskFileNameforgradingDocumentAccepting() {
        return diskFileNameforgradingDocumentAccepting;
    }

    public void setDiskFileNameforgradingDocumentAccepting(String diskFileNameforgradingDocumentAccepting) {
        this.diskFileNameforgradingDocumentAccepting = diskFileNameforgradingDocumentAccepting;
    }

    public String getFileTypeforgradingDocumentAccepting() {
        return fileTypeforgradingDocumentAccepting;
    }

    public void setFileTypeforgradingDocumentAccepting(String fileTypeforgradingDocumentAccepting) {
        this.fileTypeforgradingDocumentAccepting = fileTypeforgradingDocumentAccepting;
    }

    public String getLoginUserType() {
        return loginUserType;
    }

    public void setLoginUserType(String loginUserType) {
        this.loginUserType = loginUserType;
    }

    public String getDownloadedById() {
        return downloadedById;
    }

    public void setDownloadedById(String downloadedById) {
        this.downloadedById = downloadedById;
    }

    public String getDownloadedByIp() {
        return DownloadedByIp;
    }

    public void setDownloadedByIp(String DownloadedByIp) {
        this.DownloadedByIp = DownloadedByIp;
    }

    public String getIsPARClosed() {
        return isPARClosed;
    }

    public void setIsPARClosed(String isPARClosed) {
        this.isPARClosed = isPARClosed;
    }

    public String getIsAuthRemarksClosed() {
        return isAuthRemarksClosed;
    }

    public void setIsAuthRemarksClosed(String isAuthRemarksClosed) {
        this.isAuthRemarksClosed = isAuthRemarksClosed;
    }

    public String getDownloadedByName() {
        return downloadedByName;
    }

    public void setDownloadedByName(String downloadedByName) {
        this.downloadedByName = downloadedByName;
    }

    public String getNrcReasonDetail() {
        return NrcReasonDetail;
    }

    public void setNrcReasonDetail(String NrcReasonDetail) {
        this.NrcReasonDetail = NrcReasonDetail;
    }

    public String getLoginByIp() {
        return loginByIp;
    }

    public void setLoginByIp(String loginByIp) {
        this.loginByIp = loginByIp;
    }

    public String getLoginById() {
        return loginById;
    }

    public void setLoginById(String loginById) {
        this.loginById = loginById;
    }

    
}
