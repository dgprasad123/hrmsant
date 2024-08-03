package hrms.model.parmast;

import java.util.ArrayList;

public class InitiateOtherPARForm {

    private String sltDept;
    private String sltOffice = null;

    private String appraiseeEmpId;
    private String fiscalYear;
    private String fromDate;
    private String toDate;

    private ArrayList existDataList;

    private int parid;

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
    private String authNote;
    private String inadequaciesNote;
    private String integrityNote;
    private String sltGrading;
    private String gradingNote;

    private String txtReportingAuth;
    private String[] txtReportingAuthFromDate;
    private String[] txtReportingAuthToDate;

    private String txtReviewingAuth;
    private String[] txtRevieiwingAuthFromDate;
    private String[] txtRevieiwingAuthToDate;

    private String txtAcceptingAuth;
    private String[] txtAcceptingAuthFromDate;
    private String[] txtAcceptingAuthToDate;

    private String[] hidReportingEmpId;
    private String[] hidReportingSpcCode;

    private String[] hidReviewingEmpId;
    private String[] hidReviewingpcCode;

    private String[] hidAcceptingEmpId;
    private String[] hidAcceptingSpcCode;

    private String fiveTComponent;
    private String fiveTComponentfivePercent;
    private String fiveTComponentmoSarkar;
    
    private String fiveTChartertenpercent;
    private String fiveTCharterfivePercent;
    
    private String cadreName;
    private String hidcadrename;
    private String Deptcode;
    private String postGroupType;
    private String designationDuringPeriod;
    private String hidspc;
    
    private String errmsg;

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

    public String getAppraiseeEmpId() {
        return appraiseeEmpId;
    }

    public void setAppraiseeEmpId(String appraiseeEmpId) {
        this.appraiseeEmpId = appraiseeEmpId;
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

    public String getAuthNote() {
        return authNote;
    }

    public void setAuthNote(String authNote) {
        this.authNote = authNote;
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

    public String getGradingNote() {
        return gradingNote;
    }

    public void setGradingNote(String gradingNote) {
        this.gradingNote = gradingNote;
    }

    public String getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(String fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getTxtReportingAuth() {
        return txtReportingAuth;
    }

    public void setTxtReportingAuth(String txtReportingAuth) {
        this.txtReportingAuth = txtReportingAuth;
    }

    public String getTxtReviewingAuth() {
        return txtReviewingAuth;
    }

    public void setTxtReviewingAuth(String txtReviewingAuth) {
        this.txtReviewingAuth = txtReviewingAuth;
    }

    public String getTxtAcceptingAuth() {
        return txtAcceptingAuth;
    }

    public void setTxtAcceptingAuth(String txtAcceptingAuth) {
        this.txtAcceptingAuth = txtAcceptingAuth;
    }

    public String[] getHidReportingEmpId() {
        return hidReportingEmpId;
    }

    public void setHidReportingEmpId(String[] hidReportingEmpId) {
        this.hidReportingEmpId = hidReportingEmpId;
    }

    public String[] getHidReportingSpcCode() {
        return hidReportingSpcCode;
    }

    public void setHidReportingSpcCode(String[] hidReportingSpcCode) {
        this.hidReportingSpcCode = hidReportingSpcCode;
    }

    public String[] getHidReviewingEmpId() {
        return hidReviewingEmpId;
    }

    public void setHidReviewingEmpId(String[] hidReviewingEmpId) {
        this.hidReviewingEmpId = hidReviewingEmpId;
    }

    public String[] getHidReviewingpcCode() {
        return hidReviewingpcCode;
    }

    public void setHidReviewingpcCode(String[] hidReviewingpcCode) {
        this.hidReviewingpcCode = hidReviewingpcCode;
    }

    public String[] getHidAcceptingEmpId() {
        return hidAcceptingEmpId;
    }

    public void setHidAcceptingEmpId(String[] hidAcceptingEmpId) {
        this.hidAcceptingEmpId = hidAcceptingEmpId;
    }

    public String[] getHidAcceptingSpcCode() {
        return hidAcceptingSpcCode;
    }

    public void setHidAcceptingSpcCode(String[] hidAcceptingSpcCode) {
        this.hidAcceptingSpcCode = hidAcceptingSpcCode;
    }

    public String[] getTxtReportingAuthFromDate() {
        return txtReportingAuthFromDate;
    }

    public void setTxtReportingAuthFromDate(String[] txtReportingAuthFromDate) {
        this.txtReportingAuthFromDate = txtReportingAuthFromDate;
    }

    public String[] getTxtReportingAuthToDate() {
        return txtReportingAuthToDate;
    }

    public void setTxtReportingAuthToDate(String[] txtReportingAuthToDate) {
        this.txtReportingAuthToDate = txtReportingAuthToDate;
    }

    public String[] getTxtRevieiwingAuthFromDate() {
        return txtRevieiwingAuthFromDate;
    }

    public void setTxtRevieiwingAuthFromDate(String[] txtRevieiwingAuthFromDate) {
        this.txtRevieiwingAuthFromDate = txtRevieiwingAuthFromDate;
    }

    public String[] getTxtRevieiwingAuthToDate() {
        return txtRevieiwingAuthToDate;
    }

    public void setTxtRevieiwingAuthToDate(String[] txtRevieiwingAuthToDate) {
        this.txtRevieiwingAuthToDate = txtRevieiwingAuthToDate;
    }

    public String[] getTxtAcceptingAuthFromDate() {
        return txtAcceptingAuthFromDate;
    }

    public void setTxtAcceptingAuthFromDate(String[] txtAcceptingAuthFromDate) {
        this.txtAcceptingAuthFromDate = txtAcceptingAuthFromDate;
    }

    public String[] getTxtAcceptingAuthToDate() {
        return txtAcceptingAuthToDate;
    }

    public void setTxtAcceptingAuthToDate(String[] txtAcceptingAuthToDate) {
        this.txtAcceptingAuthToDate = txtAcceptingAuthToDate;
    }

    public int getParid() {
        return parid;
    }

    public void setParid(int parid) {
        this.parid = parid;
    }

    public ArrayList getExistDataList() {
        return existDataList;
    }

    public void setExistDataList(ArrayList existDataList) {
        this.existDataList = existDataList;
    }

    public String getFiveTComponent() {
        return fiveTComponent;
    }

    public void setFiveTComponent(String fiveTComponent) {
        this.fiveTComponent = fiveTComponent;
    }

    public String getFiveTComponentfivePercent() {
        return fiveTComponentfivePercent;
    }

    public void setFiveTComponentfivePercent(String fiveTComponentfivePercent) {
        this.fiveTComponentfivePercent = fiveTComponentfivePercent;
    }

    public String getFiveTComponentmoSarkar() {
        return fiveTComponentmoSarkar;
    }

    public void setFiveTComponentmoSarkar(String fiveTComponentmoSarkar) {
        this.fiveTComponentmoSarkar = fiveTComponentmoSarkar;
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

    public String getCadreName() {
        return cadreName;
    }

    public void setCadreName(String cadreName) {
        this.cadreName = cadreName;
    }

    public String getHidcadrename() {
        return hidcadrename;
    }

    public void setHidcadrename(String hidcadrename) {
        this.hidcadrename = hidcadrename;
    }

    public String getDeptcode() {
        return Deptcode;
    }

    public void setDeptcode(String Deptcode) {
        this.Deptcode = Deptcode;
    }

    public String getPostGroupType() {
        return postGroupType;
    }

    public void setPostGroupType(String postGroupType) {
        this.postGroupType = postGroupType;
    }

    public String getDesignationDuringPeriod() {
        return designationDuringPeriod;
    }

    public void setDesignationDuringPeriod(String designationDuringPeriod) {
        this.designationDuringPeriod = designationDuringPeriod;
    }

    public String getHidspc() {
        return hidspc;
    }

    public void setHidspc(String hidspc) {
        this.hidspc = hidspc;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
    

}
