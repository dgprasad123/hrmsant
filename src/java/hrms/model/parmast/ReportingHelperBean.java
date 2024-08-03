package hrms.model.parmast;

import java.util.ArrayList;
import org.springframework.web.multipart.MultipartFile;

public class ReportingHelperBean {

    private int parId;
    private int prtid;
    private String iscurrentreporting;
    private String reportingempid;
    private String authNote;
    private int ratequalityofwork;
    private int ratingattitude;
    private int ratingcomskill;
    private int ratingcoordination;
    private int ratingdecisionmaking;
    private int ratinginitiative;
    private int ratingitskill;
    private int ratingleadership;
    private int ratingresponsibility;
    private int teamworkrating;
    private String inadequaciesNote;
    private String integrityNote;
    private String sltGrading;
    private String sltGradingName;
    private String gradingNote;
    private String isreportingcompleted;
    private String reportingauthName = null;
    private String submittedon = null;
    private ArrayList authorityType = null;
    private String ratequalityofwork1;

    private String ratingattitude1 = null;
    private String ratingcomskill1 = null;
    private String ratingcoordination1 = null;
    private String ratingdecisionmaking1 = null;
    private String ratinginitiative1 = null;
    private String ratingitskill1 = null;
    private String ratingleadership1 = null;
    private String ratingresponsibility1 = null;
    private String teamworkrating1 = null;
    private String sltGrading1 = null;
    private String hasadminPriv = null;

    private String fiveTChartertenpercent;
    private String fiveTCharterfivePercent;
    private String fiveTComponentmoSarkar;
    private String fiscalYear = null;

    private MultipartFile gradingDocumentReporting = null;
    private String originalFileNamegradingDocumentReporting = "";
    private String diskFileNameforgradingDocumentReporting = "";
    private String fileTypeforgradingDocumentReporting = "";
    private byte[] filecontent = null;

    // ==========Properties for SI par=================    
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

    public int getParId() {
        return parId;
    }

    public void setParId(int parId) {
        this.parId = parId;
    }

    
    
    public int getPrtid() {
        return prtid;
    }

    public void setPrtid(int prtid) {
        this.prtid = prtid;
    }

    public String getIscurrentreporting() {
        return iscurrentreporting;
    }

    public void setIscurrentreporting(String iscurrentreporting) {
        this.iscurrentreporting = iscurrentreporting;
    }

    public String getReportingempid() {
        return reportingempid;
    }

    public void setReportingempid(String reportingempid) {
        this.reportingempid = reportingempid;
    }

    public String getAuthNote() {
        return authNote;
    }

    public void setAuthNote(String authNote) {
        this.authNote = authNote;
    }

    public int getRatequalityofwork() {
        return ratequalityofwork;
    }

    public void setRatequalityofwork(int ratequalityofwork) {
        this.ratequalityofwork = ratequalityofwork;
    }

    public int getRatingattitude() {
        return ratingattitude;
    }

    public void setRatingattitude(int ratingattitude) {
        this.ratingattitude = ratingattitude;
    }

    public int getRatingcomskill() {
        return ratingcomskill;
    }

    public void setRatingcomskill(int ratingcomskill) {
        this.ratingcomskill = ratingcomskill;
    }

    public int getRatingcoordination() {
        return ratingcoordination;
    }

    public void setRatingcoordination(int ratingcoordination) {
        this.ratingcoordination = ratingcoordination;
    }

    public int getRatingdecisionmaking() {
        return ratingdecisionmaking;
    }

    public void setRatingdecisionmaking(int ratingdecisionmaking) {
        this.ratingdecisionmaking = ratingdecisionmaking;
    }

    public int getRatinginitiative() {
        return ratinginitiative;
    }

    public void setRatinginitiative(int ratinginitiative) {
        this.ratinginitiative = ratinginitiative;
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

    public String getGradingNote() {
        return gradingNote;
    }

    public void setGradingNote(String gradingNote) {
        this.gradingNote = gradingNote;
    }

    public String getIsreportingcompleted() {
        return isreportingcompleted;
    }

    public void setIsreportingcompleted(String isreportingcompleted) {
        this.isreportingcompleted = isreportingcompleted;
    }

    public String getReportingauthName() {
        return reportingauthName;
    }

    public void setReportingauthName(String reportingauthName) {
        this.reportingauthName = reportingauthName;
    }

    public String getSubmittedon() {
        return submittedon;
    }

    public void setSubmittedon(String submittedon) {
        this.submittedon = submittedon;
    }

    public ArrayList getAuthorityType() {
        return authorityType;
    }

    public void setAuthorityType(ArrayList authorityType) {
        this.authorityType = authorityType;
    }

    public String getRatequalityofwork1() {
        return ratequalityofwork1;
    }

    public void setRatequalityofwork1(String ratequalityofwork1) {
        this.ratequalityofwork1 = ratequalityofwork1;
    }

    public String getRatingattitude1() {
        return ratingattitude1;
    }

    public void setRatingattitude1(String ratingattitude1) {
        this.ratingattitude1 = ratingattitude1;
    }

    public String getRatingcomskill1() {
        return ratingcomskill1;
    }

    public void setRatingcomskill1(String ratingcomskill1) {
        this.ratingcomskill1 = ratingcomskill1;
    }

    public String getRatingcoordination1() {
        return ratingcoordination1;
    }

    public void setRatingcoordination1(String ratingcoordination1) {
        this.ratingcoordination1 = ratingcoordination1;
    }

    public String getRatingdecisionmaking1() {
        return ratingdecisionmaking1;
    }

    public void setRatingdecisionmaking1(String ratingdecisionmaking1) {
        this.ratingdecisionmaking1 = ratingdecisionmaking1;
    }

    public String getRatinginitiative1() {
        return ratinginitiative1;
    }

    public void setRatinginitiative1(String ratinginitiative1) {
        this.ratinginitiative1 = ratinginitiative1;
    }

    public String getRatingitskill1() {
        return ratingitskill1;
    }

    public void setRatingitskill1(String ratingitskill1) {
        this.ratingitskill1 = ratingitskill1;
    }

    public String getRatingleadership1() {
        return ratingleadership1;
    }

    public void setRatingleadership1(String ratingleadership1) {
        this.ratingleadership1 = ratingleadership1;
    }

    public String getRatingresponsibility1() {
        return ratingresponsibility1;
    }

    public void setRatingresponsibility1(String ratingresponsibility1) {
        this.ratingresponsibility1 = ratingresponsibility1;
    }

    public String getTeamworkrating1() {
        return teamworkrating1;
    }

    public void setTeamworkrating1(String teamworkrating1) {
        this.teamworkrating1 = teamworkrating1;
    }

    public String getSltGrading1() {
        return sltGrading1;
    }

    public void setSltGrading1(String sltGrading1) {
        this.sltGrading1 = sltGrading1;
    }

    public String getHasadminPriv() {
        return hasadminPriv;
    }

    public void setHasadminPriv(String hasadminPriv) {
        this.hasadminPriv = hasadminPriv;
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

    public String getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(String fiscalYear) {
        this.fiscalYear = fiscalYear;
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

}
