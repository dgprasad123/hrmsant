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
public class DefenceBean {

    private int dadid;
    private int daId;
    private int taskId;
    private String docType;
    private String filetpe;
    private String orgFileName;
    private int defid;
    private int docid;
    private String defhrmsid;
    private String diskfilename;
    private String defenceRemark;
    private MultipartFile uploadDocument;
    private String defspc;
    private String ioappointhrmsid;
    private String ioappointspc;
    private String initHrmsId;
    private String initSpc;
    private String authHrmsId;
    private String authSpc;
    private ArrayList<FileAttachBean> attachments;
    private String writtenStatemenyByDOOnDt;
    private MultipartFile defenceByDOdocument;
    private String defenceByDOoriginalFileName;
    private String defenceByDOdiskFileName;
    private String defenceByDOcontentType;
    private byte[] Filecontent;
    private String hasReplyByDelinquentOfficer;

    public int getDadid() {
        return dadid;
    }

    public void setDadid(int dadid) {
        this.dadid = dadid;
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

    public int getDefid() {
        return defid;
    }

    public void setDefid(int defid) {
        this.defid = defid;
    }

    public int getDocid() {
        return docid;
    }

    public void setDocid(int docid) {
        this.docid = docid;
    }

    public String getDefhrmsid() {
        return defhrmsid;
    }

    public void setDefhrmsid(String defhrmsid) {
        this.defhrmsid = defhrmsid;
    }

    public String getDiskfilename() {
        return diskfilename;
    }

    public void setDiskfilename(String diskfilename) {
        this.diskfilename = diskfilename;
    }

    public String getDefenceRemark() {
        return defenceRemark;
    }

    public void setDefenceRemark(String defenceRemark) {
        this.defenceRemark = defenceRemark;
    }

    public MultipartFile getUploadDocument() {
        return uploadDocument;
    }

    public void setUploadDocument(MultipartFile uploadDocument) {
        this.uploadDocument = uploadDocument;
    }

    public String getDefspc() {
        return defspc;
    }

    public void setDefspc(String defspc) {
        this.defspc = defspc;
    }

    public String getIoappointhrmsid() {
        return ioappointhrmsid;
    }

    public void setIoappointhrmsid(String ioappointhrmsid) {
        this.ioappointhrmsid = ioappointhrmsid;
    }

    public String getIoappointspc() {
        return ioappointspc;
    }

    public void setIoappointspc(String ioappointspc) {
        this.ioappointspc = ioappointspc;
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

    public ArrayList<FileAttachBean> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<FileAttachBean> attachments) {
        this.attachments = attachments;
    }

    public String getWrittenStatemenyByDOOnDt() {
        return writtenStatemenyByDOOnDt;
    }

    public void setWrittenStatemenyByDOOnDt(String writtenStatemenyByDOOnDt) {
        this.writtenStatemenyByDOOnDt = writtenStatemenyByDOOnDt;
    }

    public MultipartFile getDefenceByDOdocument() {
        return defenceByDOdocument;
    }

    public void setDefenceByDOdocument(MultipartFile defenceByDOdocument) {
        this.defenceByDOdocument = defenceByDOdocument;
    }

    public String getDefenceByDOoriginalFileName() {
        return defenceByDOoriginalFileName;
    }

    public void setDefenceByDOoriginalFileName(String defenceByDOoriginalFileName) {
        this.defenceByDOoriginalFileName = defenceByDOoriginalFileName;
    }

    public String getDefenceByDOdiskFileName() {
        return defenceByDOdiskFileName;
    }

    public void setDefenceByDOdiskFileName(String defenceByDOdiskFileName) {
        this.defenceByDOdiskFileName = defenceByDOdiskFileName;
    }

    public String getDefenceByDOcontentType() {
        return defenceByDOcontentType;
    }

    public void setDefenceByDOcontentType(String defenceByDOcontentType) {
        this.defenceByDOcontentType = defenceByDOcontentType;
    }

    public byte[] getFilecontent() {
        return Filecontent;
    }

    public void setFilecontent(byte[] Filecontent) {
        this.Filecontent = Filecontent;
    }

    public String getHasReplyByDelinquentOfficer() {
        return hasReplyByDelinquentOfficer;
    }

    public void setHasReplyByDelinquentOfficer(String hasReplyByDelinquentOfficer) {
        this.hasReplyByDelinquentOfficer = hasReplyByDelinquentOfficer;
    }

}
