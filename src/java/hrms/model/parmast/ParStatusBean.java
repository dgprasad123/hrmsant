/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.parmast;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Manisha
 */
public class ParStatusBean {

    private int logId;
    private String isClosedForAppraisee;
    private String isClosedForAuthority;
    private MultipartFile parStatusdocument;
    private String originalFileNameForparStatus = "";
    private String diskfileNameForparStatus = "";
    private String fileTypeForparStatus = "";
    private String fiscalyear;
    private String parPeriodForAppraisee;
    private String parPeriodForReporting;
    private String parPeriodForReviewing;
    private String parPeriodForAccepting;
    private String parStatusChangeOnDate;
    private byte[] filecontent = null;
     private int parstatus = 0;

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getIsClosedForAppraisee() {
        return isClosedForAppraisee;
    }

    public void setIsClosedForAppraisee(String isClosedForAppraisee) {
        this.isClosedForAppraisee = isClosedForAppraisee;
    }

    public String getIsClosedForAuthority() {
        return isClosedForAuthority;
    }

    public void setIsClosedForAuthority(String isClosedForAuthority) {
        this.isClosedForAuthority = isClosedForAuthority;
    }

    

    public MultipartFile getParStatusdocument() {
        return parStatusdocument;
    }

    public void setParStatusdocument(MultipartFile parStatusdocument) {
        this.parStatusdocument = parStatusdocument;
    }

    

    public String getOriginalFileNameForparStatus() {
        return originalFileNameForparStatus;
    }

    public void setOriginalFileNameForparStatus(String originalFileNameForparStatus) {
        this.originalFileNameForparStatus = originalFileNameForparStatus;
    }

    public String getDiskfileNameForparStatus() {
        return diskfileNameForparStatus;
    }

    public void setDiskfileNameForparStatus(String diskfileNameForparStatus) {
        this.diskfileNameForparStatus = diskfileNameForparStatus;
    }

    public String getFileTypeForparStatus() {
        return fileTypeForparStatus;
    }

    public void setFileTypeForparStatus(String fileTypeForparStatus) {
        this.fileTypeForparStatus = fileTypeForparStatus;
    }

    public String getFiscalyear() {
        return fiscalyear;
    }

    public void setFiscalyear(String fiscalyear) {
        this.fiscalyear = fiscalyear;
    }

    public String getParPeriodForAppraisee() {
        return parPeriodForAppraisee;
    }

    public void setParPeriodForAppraisee(String parPeriodForAppraisee) {
        this.parPeriodForAppraisee = parPeriodForAppraisee;
    }

    public String getParPeriodForReporting() {
        return parPeriodForReporting;
    }

    public void setParPeriodForReporting(String parPeriodForReporting) {
        this.parPeriodForReporting = parPeriodForReporting;
    }

    public String getParPeriodForReviewing() {
        return parPeriodForReviewing;
    }

    public void setParPeriodForReviewing(String parPeriodForReviewing) {
        this.parPeriodForReviewing = parPeriodForReviewing;
    }

    public String getParPeriodForAccepting() {
        return parPeriodForAccepting;
    }

    public void setParPeriodForAccepting(String parPeriodForAccepting) {
        this.parPeriodForAccepting = parPeriodForAccepting;
    }

    public String getParStatusChangeOnDate() {
        return parStatusChangeOnDate;
    }

    public void setParStatusChangeOnDate(String parStatusChangeOnDate) {
        this.parStatusChangeOnDate = parStatusChangeOnDate;
    }

    public byte[] getFilecontent() {
        return filecontent;
    }

    public void setFilecontent(byte[] filecontent) {
        this.filecontent = filecontent;
    }

    public int getParstatus() {
        return parstatus;
    }

    public void setParstatus(int parstatus) {
        this.parstatus = parstatus;
    }

    
    

}
