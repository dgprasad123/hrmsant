/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.discProceeding;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author manisha
 */
public class DispatchDetailsBean {

    private int ddid;
    private String postoffcName;
    private String emsNo;
    private MultipartFile emsCopyAttach;
    private MultipartFile emsTrackReportFilename;
    private byte[] filecontent = null;
    private String filetype = null;
    private String comType;
    private int comTypeReference;
    private int daId;
     private int uploadFileId;
    private String scanfilename;
    private String scantrackReport;
    private String emsoFileName;
   
    public int getDdid() {
        return ddid;
    }

    public void setDdid(int ddid) {
        this.ddid = ddid;
    }

    public String getPostoffcName() {
        return postoffcName;
    }

    public void setPostoffcName(String postoffcName) {
        this.postoffcName = postoffcName;
    }

    public String getEmsNo() {
        return emsNo;
    }

    public void setEmsNo(String emsNo) {
        this.emsNo = emsNo;
    }

   
    public MultipartFile getEmsCopyAttach() {
        return emsCopyAttach;
    }

    public void setEmsCopyAttach(MultipartFile emsCopyAttach) {
        this.emsCopyAttach = emsCopyAttach;
    }

    public MultipartFile getEmsTrackReportFilename() {
        return emsTrackReportFilename;
    }

    public void setEmsTrackReportFilename(MultipartFile emsTrackReportFilename) {
        this.emsTrackReportFilename = emsTrackReportFilename;
    }

    public String getComType() {
        return comType;
    }

    public void setComType(String comType) {
        this.comType = comType;
    }

    public int getComTypeReference() {
        return comTypeReference;
    }

    public void setComTypeReference(int comTypeReference) {
        this.comTypeReference = comTypeReference;
    }

    public int getDaId() {
        return daId;
    }

    public void setDaId(int daId) {
        this.daId = daId;
    }

    public int getUploadFileId() {
        return uploadFileId;
    }

    public void setUploadFileId(int uploadFileId) {
        this.uploadFileId = uploadFileId;
    }

    public String getScanfilename() {
        return scanfilename;
    }

    public void setScanfilename(String scanfilename) {
        this.scanfilename = scanfilename;
    }

    public String getScantrackReport() {
        return scantrackReport;
    }

    public void setScantrackReport(String scantrackReport) {
        this.scantrackReport = scantrackReport;
    }

    public byte[] getFilecontent() {
        return filecontent;
    }

    public void setFilecontent(byte[] filecontent) {
        this.filecontent = filecontent;
    }              

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getEmsoFileName() {
        return emsoFileName;
    }

    public void setEmsoFileName(String emsoFileName) {
        this.emsoFileName = emsoFileName;
    }

   
  

}
