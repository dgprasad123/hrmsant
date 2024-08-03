package hrms.model.parmast;

import hrms.common.CommonFunctions;

public class ParMasterBean {

    private int parid;
    private int parstatus;
    private String periodfrom = null;
    private String periodto = null;
    private String designation = null;
    private String isClosed = null;
    private String authRemarksClosed = null;
    private String iseditable;
    private String encryptedParid;
    private String isDeleted;
    private String reportingRemarksClosed = null;
    private String submittedOn;
    private String cadreName;
    private String appraiseePost;
    private String isResubmitRevertPAR;

    public int getParid() {
        return parid;
    }

    public void setParid(int parid) {
        this.parid = parid;
    }

    public String getPeriodfrom() {
        return periodfrom;
    }

    public void setPeriodfrom(String periodfrom) {
        this.periodfrom = periodfrom;
    }

    public String getPeriodto() {
        return periodto;
    }

    public void setPeriodto(String periodto) {
        this.periodto = periodto;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public int getParstatus() {
        return parstatus;
    }

    public void setParstatus(int parstatus) {
        this.parstatus = parstatus;
    }

    public String getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(String isClosed) {
        this.isClosed = isClosed;
    }

    public String getAuthRemarksClosed() {
        return authRemarksClosed;
    }

    public void setAuthRemarksClosed(String authRemarksClosed) {
        this.authRemarksClosed = authRemarksClosed;
    }

    public String getIseditable() {
        return iseditable;
    }

    public void setIseditable(String iseditable) {
        this.iseditable = iseditable;
    }

    public String getEncryptedParid() {
        return encryptedParid;
    }

    public void setEncryptedParid(String encryptedParid) throws Exception {
        this.encryptedParid = CommonFunctions.encodedTxt(encryptedParid + "");
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getReportingRemarksClosed() {
        return reportingRemarksClosed;
    }

    public void setReportingRemarksClosed(String reportingRemarksClosed) {
        this.reportingRemarksClosed = reportingRemarksClosed;
    }

    public String getSubmittedOn() {
        return submittedOn;
    }

    public void setSubmittedOn(String submittedOn) {
        this.submittedOn = submittedOn;
    }

    public String getCadreName() {
        return cadreName;
    }

    public void setCadreName(String cadreName) {
        this.cadreName = cadreName;
    }

    public String getAppraiseePost() {
        return appraiseePost;
    }

    public void setAppraiseePost(String appraiseePost) {
        this.appraiseePost = appraiseePost;
    }

    public String getIsResubmitRevertPAR() {
        return isResubmitRevertPAR;
    }

    public void setIsResubmitRevertPAR(String isResubmitRevertPAR) {
        this.isResubmitRevertPAR = isResubmitRevertPAR;
    }

}
