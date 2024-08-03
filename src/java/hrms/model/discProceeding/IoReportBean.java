/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.discProceeding;

import java.util.ArrayList;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author manisha
 */
public class IoReportBean {

    private int ioRemarkId;
    private int daId;
    private int taskId;
    private int daioid;
    private String[] doiod;
    private String remark;
    private String exihibitstype;
    private String documentName;
    private int documentId;
    private MultipartFile ioDocument;
    private String authHrmsId;
    private String authSpc;
    private String iohrmsId;
    private String ioSpc;
    private String ioRemark;
    private String initHrmsId;
    private String initSpc;
    private String istype;
    private ArrayList attachments;
    

    public int getIoRemarkId() {
        return ioRemarkId;
    }

    public void setIoRemarkId(int ioRemarkId) {
        this.ioRemarkId = ioRemarkId;
    }

    public int getDaId() {
        return daId;
    }

    public void setDaId(int daId) {
        this.daId = daId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getDaioid() {
        return daioid;
    }

    public void setDaioid(int daioid) {
        this.daioid = daioid;
    }

    public String[] getDoiod() {
        return doiod;
    }

    public void setDoiod(String[] doiod) {
        this.doiod = doiod;
    }

   
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getExihibitstype() {
        return exihibitstype;
    }

    public void setExihibitstype(String exihibitstype) {
        this.exihibitstype = exihibitstype;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public MultipartFile getIoDocument() {
        return ioDocument;
    }

    public void setIoDocument(MultipartFile ioDocument) {
        this.ioDocument = ioDocument;
    }

    public String getAuthHrmsId() {
        return authHrmsId;
    }

    public void setAuthHrmsId(String authHrmsId) {
        this.authHrmsId = authHrmsId;
    }

    public String getAuthSpc() {
        return authSpc;
    }

    public void setAuthSpc(String authSpc) {
        this.authSpc = authSpc;
    }

    public String getIohrmsId() {
        return iohrmsId;
    }

    public void setIohrmsId(String iohrmsId) {
        this.iohrmsId = iohrmsId;
    }

   

    public String getIoSpc() {
        return ioSpc;
    }

    public void setIoSpc(String ioSpc) {
        this.ioSpc = ioSpc;
    }

    public String getIoRemark() {
        return ioRemark;
    }

    public void setIoRemark(String ioRemark) {
        this.ioRemark = ioRemark;
    }

    public String getInitHrmsId() {
        return initHrmsId;
    }

    public void setInitHrmsId(String initHrmsId) {
        this.initHrmsId = initHrmsId;
    }

   

    public String getInitSpc() {
        return initSpc;
    }

    public void setInitSpc(String initSpc) {
        this.initSpc = initSpc;
    }

    public String getIstype() {
        return istype;
    }

    public void setIstype(String istype) {
        this.istype = istype;
    }

    public ArrayList getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList attachments) {
        this.attachments = attachments;
    }
    
    

}
