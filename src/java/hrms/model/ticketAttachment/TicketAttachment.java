/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.ticketAttachment;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Surendra
 */
public class TicketAttachment {
    private int attachmentId;
    private String ofileName=null;
    private String dfileName =null;
    private int refId ;
    private String refType =null;
    private String filePath =null;
    private String fileType =null;

    public int getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(int attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getOfileName() {
        return ofileName;
    }

    public void setOfileName(String ofileName) {
        this.ofileName = ofileName;
    }

    public String getDfileName() {
        return dfileName;
    }

    public void setDfileName(String dfileName) {
        this.dfileName = dfileName;
    }

    public int getRefId() {
        return refId;
    }

    public void setRefId(int refId) {
        this.refId = refId;
    }

    public String getRefType() {
        return refType;
    }

    public void setRefType(String refType) {
        this.refType = refType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    
    
    
}
