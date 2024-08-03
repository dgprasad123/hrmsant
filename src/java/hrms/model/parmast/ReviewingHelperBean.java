package hrms.model.parmast;

import org.springframework.web.multipart.MultipartFile;

public class ReviewingHelperBean {

    private int prtid;
    private int parId;
    private String isreviewingcompleted;
    private String reportingempid;
    private String reviewingNote;
    private int sltReviewGrading;
    private String reviewGrading;
     private String sltReviewGrading1;

    private String iscurrentreviewing = null;
    private String reviewingauthName = null;

    private String submittedon = null;
    
    private MultipartFile gradingDocumentReviewing = null;
    private String originalFileNamegradingDocumentReviewing = "";
    private String diskFileNameforgradingDocumentReviewing = "";
    private String fileTypeforgradingDocumentReviewing = "";
    private byte[] filecontent = null;

    public int getPrtid() {
        return prtid;
    }

    public void setPrtid(int prtid) {
        this.prtid = prtid;
    }

    public int getParId() {
        return parId;
    }

    public void setParId(int parId) {
        this.parId = parId;
    }

    public String getIsreviewingcompleted() {
        return isreviewingcompleted;
    }

    public void setIsreviewingcompleted(String isreviewingcompleted) {
        this.isreviewingcompleted = isreviewingcompleted;
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

    public int getSltReviewGrading() {
        return sltReviewGrading;
    }

    public void setSltReviewGrading(int sltReviewGrading) {
        this.sltReviewGrading = sltReviewGrading;
    }

    public String getReviewGrading() {
        return reviewGrading;
    }

    public void setReviewGrading(String reviewGrading) {
        this.reviewGrading = reviewGrading;
    }

    public String getIscurrentreviewing() {
        return iscurrentreviewing;
    }

    public void setIscurrentreviewing(String iscurrentreviewing) {
        this.iscurrentreviewing = iscurrentreviewing;
    }

    public String getReviewingauthName() {
        return reviewingauthName;
    }

    public void setReviewingauthName(String reviewingauthName) {
        this.reviewingauthName = reviewingauthName;
    }

    public String getSubmittedon() {
        return submittedon;
    }

    public void setSubmittedon(String submittedon) {
        this.submittedon = submittedon;
    }

    public String getSltReviewGrading1() {
        return sltReviewGrading1;
    }

    public void setSltReviewGrading1(String sltReviewGrading1) {
        this.sltReviewGrading1 = sltReviewGrading1;
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

    public byte[] getFilecontent() {
        return filecontent;
    }

    public void setFilecontent(byte[] filecontent) {
        this.filecontent = filecontent;
    }

}
