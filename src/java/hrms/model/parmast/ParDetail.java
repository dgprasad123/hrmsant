package hrms.model.parmast;

import java.util.ArrayList;
import org.springframework.web.multipart.MultipartFile;

public class ParDetail {

    private int parid;
    private int taskid;
    private int refid;
    private int parstatus;
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
    private String parPeriodFrom = null;
    private String parPeriodTo = null;
    private ArrayList leaveAbsentee = null;
    private ArrayList achivementList = null;
    private String selfappraisal = null;
    private String specialcontribution = null;
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
    private String sltGradingName = null;
    private String reportinggradename = null;
    private String sltReviewGrading = null;
    private ArrayList reportingauth = null;
    private ArrayList reviewingauth = null;
    private ArrayList acceptingauth = null;

    private ArrayList reportingdata = null;
    private ArrayList reviewingdata = null;
    private ArrayList acceptingdata = null;

    private String rdEmpIdReverted = null;
    private String revertremarks = null;
    private String revertdone = null;

    private String sltAcceptingAuthorityRemarks = null;
    private int grading = 0;

    private String auth = "";

    private String isClosedFiscalYearAuthority = null;

    private String nrcreason = null;
    private String nrcremarks = null;
    private String nrcattchname = null;

    private String fiveTChartertenpercent;
    private String fiveTCharterfivePercent;
    private String fiveTComponentmoSarkar;

    private String fiveTComponentappraise;
    private String submitedonNRC;
    private String sltAcceptingGrading = null;
    private String postGroupAppraise = null;
    private String cadreNameAppraise = null;
    private int pactid;
    private int paptid;
    private int prvtid;
    
    private MultipartFile gradingDocumentReporting = null;
    private String originalFileNamegradingDocumentReporting = "";
    private String diskFileNameforgradingDocumentReporting = "";
    private String fileTypeforgradingDocumentReporting = "";
    private byte[] filecontent = null;
    
    private MultipartFile gradingDocumentReviewing = null;
    private String originalFileNamegradingDocumentReviewing = "";
    private String diskFileNameforgradingDocumentReviewing = "";
    private String fileTypeforgradingDocumentReviewing = "";
    
    private MultipartFile gradingDocumentAccepting = null;
    private String originalFileNamegradingDocumentAccepting = "";
    private String diskFileNameforgradingDocumentAccepting = "";
    private String fileTypeforgradingDocumentAccepting = "";
    
    private String nrcReasonDetail;
    private String isAuthorityAbleToGiveRemarks;
    

    // ==========properties for SI par================= 
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
    
    private String encryptedparid;
    private String ipAddress;
    
    public int getParid() {
        return parid;
    }

    public void setParid(int parid) {
        this.parid = parid;
    }

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
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

    public String getParPeriodFrom() {
        return parPeriodFrom;
    }

    public void setParPeriodFrom(String parPeriodFrom) {
        this.parPeriodFrom = parPeriodFrom;
    }

    public String getParPeriodTo() {
        return parPeriodTo;
    }

    public void setParPeriodTo(String parPeriodTo) {
        this.parPeriodTo = parPeriodTo;
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

    public String getSltGradingName() {
        return sltGradingName;
    }

    public void setSltGradingName(String sltGradingName) {
        this.sltGradingName = sltGradingName;
    }

    public String getReportinggradename() {
        return reportinggradename;
    }

    public void setReportinggradename(String reportinggradename) {
        this.reportinggradename = reportinggradename;
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

    public String getRdEmpIdReverted() {
        return rdEmpIdReverted;
    }

    public void setRdEmpIdReverted(String rdEmpIdReverted) {
        this.rdEmpIdReverted = rdEmpIdReverted;
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

    public String getSltAcceptingAuthorityRemarks() {
        return sltAcceptingAuthorityRemarks;
    }

    public void setSltAcceptingAuthorityRemarks(String sltAcceptingAuthorityRemarks) {
        this.sltAcceptingAuthorityRemarks = sltAcceptingAuthorityRemarks;
    }

    public int getGrading() {
        return grading;
    }

    public void setGrading(int grading) {
        this.grading = grading;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getIsClosedFiscalYearAuthority() {
        return isClosedFiscalYearAuthority;
    }

    public void setIsClosedFiscalYearAuthority(String isClosedFiscalYearAuthority) {
        this.isClosedFiscalYearAuthority = isClosedFiscalYearAuthority;
    }

    public String getIshideremark() {
        String ishideremark = "Y";
        if (this.applicantempid != null && this.applicantempid.equals(this.loginId)) {
            if (this.reportingempid != null && this.applicantempid.equals(this.reportingempid)) {
                ishideremark = "N";
            } else {
                ishideremark = "Y";
            }
        } else {
            if (this.parstatus == 9) {
                ishideremark = "N";
            } else {
                ishideremark = "N";
            }
        }
        return ishideremark;
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

    public String getNrcattchname() {
        return nrcattchname;
    }

    public void setNrcattchname(String nrcattchname) {
        this.nrcattchname = nrcattchname;
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

    public String getPostGroupAppraise() {
        return postGroupAppraise;
    }

    public void setPostGroupAppraise(String postGroupAppraise) {
        this.postGroupAppraise = postGroupAppraise;
    }

    public String getCadreNameAppraise() {
        return cadreNameAppraise;
    }

    public void setCadreNameAppraise(String cadreNameAppraise) {
        this.cadreNameAppraise = cadreNameAppraise;
    }

    public int getPactid() {
        return pactid;
    }

    public void setPactid(int pactid) {
        this.pactid = pactid;
    }

    public int getPaptid() {
        return paptid;
    }

    public void setPaptid(int paptid) {
        this.paptid = paptid;
    }

    public int getPrvtid() {
        return prvtid;
    }

    public void setPrvtid(int prvtid) {
        this.prvtid = prvtid;
    }

    public MultipartFile getGradingDocumentReporting() {
        return gradingDocumentReporting;
    }

    public void setGradingDocumentReporting(MultipartFile gradingDocumentReporting) {
        this.gradingDocumentReporting = gradingDocumentReporting;
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

    public byte[] getFilecontent() {
        return filecontent;
    }

    public void setFilecontent(byte[] filecontent) {
        this.filecontent = filecontent;
    }

    public MultipartFile getGradingDocumentReviewing() {
        return gradingDocumentReviewing;
    }

    public void setGradingDocumentReviewing(MultipartFile gradingDocumentReviewing) {
        this.gradingDocumentReviewing = gradingDocumentReviewing;
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

    public MultipartFile getGradingDocumentAccepting() {
        return gradingDocumentAccepting;
    }

    public void setGradingDocumentAccepting(MultipartFile gradingDocumentAccepting) {
        this.gradingDocumentAccepting = gradingDocumentAccepting;
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

    public String getNrcReasonDetail() {
        return nrcReasonDetail;
    }

    public void setNrcReasonDetail(String nrcReasonDetail) {
        this.nrcReasonDetail = nrcReasonDetail;
    }

    public String getIsAuthorityAbleToGiveRemarks() {
        return isAuthorityAbleToGiveRemarks;
    }

    public void setIsAuthorityAbleToGiveRemarks(String isAuthorityAbleToGiveRemarks) {
        this.isAuthorityAbleToGiveRemarks = isAuthorityAbleToGiveRemarks;
    }
    

    public String getParType() {
        return parType;
    }

    public void setParType(String parType) {
        this.parType = parType;
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

    public String getPlaceOfPostingSi() {
        return placeOfPostingSi;
    }

    public void setPlaceOfPostingSi(String placeOfPostingSi) {
        this.placeOfPostingSi = placeOfPostingSi;
    }

    public String getEncryptedparid() {
        return encryptedparid;
    }

    public void setEncryptedparid(String encryptedparid) {
        this.encryptedparid = encryptedparid;
    } 

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    
}
