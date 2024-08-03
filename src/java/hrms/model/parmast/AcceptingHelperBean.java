package hrms.model.parmast;

import org.springframework.web.multipart.MultipartFile;

public class AcceptingHelperBean {

    private int pactid;
    private int parId;
    private String isacceptingcompleted;
    private String iscurrentaccepting;
    private String acceptingempid;
    private String acceptingNote;

    private String acceptingauthName = null;

    private String submittedon = null;
    private int sltAcceptingGrading;
    private String sltAcceptingGrading1;
    
    private MultipartFile gradingDocumentAccepting = null;
    private String originalFileNamegradingDocumentAccepting = "";
    private String diskFileNameforgradingDocumentAccepting = "";
    private String fileTypeforgradingDocumentAccepting = "";
    private String filepathforgradingDocumentAccepting = "";
    private byte[] filecontent = null;
    

    public int getPactid() {
        return pactid;
    }

    public void setPactid(int pactid) {
        this.pactid = pactid;
    }

    public int getParId() {
        return parId;
    }

    public void setParId(int parId) {
        this.parId = parId;
    }

    public String getIsacceptingcompleted() {
        return isacceptingcompleted;
    }

    public void setIsacceptingcompleted(String isacceptingcompleted) {
        this.isacceptingcompleted = isacceptingcompleted;
    }

    public String getIscurrentaccepting() {
        return iscurrentaccepting;
    }

    public void setIscurrentaccepting(String iscurrentaccepting) {
        this.iscurrentaccepting = iscurrentaccepting;
    }

    public String getAcceptingempid() {
        return acceptingempid;
    }

    public void setAcceptingempid(String acceptingempid) {
        this.acceptingempid = acceptingempid;
    }

    public String getAcceptingNote() {
        return acceptingNote;
    }

    public void setAcceptingNote(String acceptingNote) {
        this.acceptingNote = acceptingNote;
    }

    public String getAcceptingauthName() {
        return acceptingauthName;
    }

    public void setAcceptingauthName(String acceptingauthName) {
        this.acceptingauthName = acceptingauthName;
    }

    public String getSubmittedon() {
        return submittedon;
    }

    public void setSubmittedon(String submittedon) {
        this.submittedon = submittedon;
    }

    public int getSltAcceptingGrading() {
        return sltAcceptingGrading;
    }

    public void setSltAcceptingGrading(int sltAcceptingGrading) {
        this.sltAcceptingGrading = sltAcceptingGrading;
    }

    public String getSltAcceptingGrading1() {
        return sltAcceptingGrading1;
    }

    public void setSltAcceptingGrading1(String sltAcceptingGrading1) {
        this.sltAcceptingGrading1 = sltAcceptingGrading1;
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

    public String getFilepathforgradingDocumentAccepting() {
        return filepathforgradingDocumentAccepting;
    }

    public void setFilepathforgradingDocumentAccepting(String filepathforgradingDocumentAccepting) {
        this.filepathforgradingDocumentAccepting = filepathforgradingDocumentAccepting;
    }

    public byte[] getFilecontent() {
        return filecontent;
    }

    public void setFilecontent(byte[] filecontent) {
        this.filecontent = filecontent;
    }

}
