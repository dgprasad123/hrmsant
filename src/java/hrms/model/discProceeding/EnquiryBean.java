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
public class EnquiryBean {

    private String docType;
    private String filetpe;
    private String orgFileName;
    private int daioid;
    private int docid;
    private int dohrmsid;
    private String diskfilename;
    private String enquiryRemark;
    private MultipartFile uploadDocument;

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getFiletpe() {
        return filetpe;
    }

    public void setFiletpe(String filetpe) {
        this.filetpe = filetpe;
    }

    public String getOrgFileName() {
        return orgFileName;
    }

    public void setOrgFileName(String orgFileName) {
        this.orgFileName = orgFileName;
    }

    public int getDaioid() {
        return daioid;
    }

    public void setDaioid(int daioid) {
        this.daioid = daioid;
    }

   

    public int getDocid() {
        return docid;
    }

    public void setDocid(int docid) {
        this.docid = docid;
    }

    public int getDohrmsid() {
        return dohrmsid;
    }

    public void setDohrmsid(int dohrmsid) {
        this.dohrmsid = dohrmsid;
    }

    public String getDiskfilename() {
        return diskfilename;
    }

    public void setDiskfilename(String diskfilename) {
        this.diskfilename = diskfilename;
    }

    public String getEnquiryRemark() {
        return enquiryRemark;
    }

    public void setEnquiryRemark(String enquiryRemark) {
        this.enquiryRemark = enquiryRemark;
    }

    public MultipartFile getUploadDocument() {
        return uploadDocument;
    }

    public void setUploadDocument(MultipartFile uploadDocument) {
        this.uploadDocument = uploadDocument;
    }
    

}
