/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.parmast;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Manisha
 */
public class GroupCCustodianCommunication {

    private int promotionId;
    private int communicationId;
    private String fromempId;
    private String fromempName;
    private String fromspc;
    private String toempId;
    private String tospc;
    private String messagedetail;
    private int attachmentId;
    private String diskFileName;
    private String originalFilename;
    private byte[] filecontent = null;
    private String getContentType;
    private MultipartFile uploadDocument;
    private String searchCriteria;
    private String authoritytype;
    private int taskId;
    private String communicationOnDate;
    private String reviewedempname;
    private String reviewedPost;

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public int getCommunicationId() {
        return communicationId;
    }

    public void setCommunicationId(int communicationId) {
        this.communicationId = communicationId;
    }

    public String getFromempId() {
        return fromempId;
    }

    public void setFromempId(String fromempId) {
        this.fromempId = fromempId;
    }

    public String getFromspc() {
        return fromspc;
    }

    public void setFromspc(String fromspc) {
        this.fromspc = fromspc;
    }

    public String getToempId() {
        return toempId;
    }

    public void setToempId(String toempId) {
        this.toempId = toempId;
    }

    public String getTospc() {
        return tospc;
    }

    public void setTospc(String tospc) {
        this.tospc = tospc;
    }

    public String getMessagedetail() {
        return messagedetail;
    }

    public void setMessagedetail(String messagedetail) {
        this.messagedetail = messagedetail;
    }

    public int getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(int attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getDiskFileName() {
        return diskFileName;
    }

    public void setDiskFileName(String diskFileName) {
        this.diskFileName = diskFileName;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public byte[] getFilecontent() {
        return filecontent;
    }

    public void setFilecontent(byte[] filecontent) {
        this.filecontent = filecontent;
    }
    

    public String getGetContentType() {
        return getContentType;
    }

    public void setGetContentType(String getContentType) {
        this.getContentType = getContentType;
    }

    public MultipartFile getUploadDocument() {
        return uploadDocument;
    }

    public void setUploadDocument(MultipartFile uploadDocument) {
        this.uploadDocument = uploadDocument;
    }

    public String getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public String getAuthoritytype() {
        return authoritytype;
    }

    public void setAuthoritytype(String authoritytype) {
        this.authoritytype = authoritytype;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getCommunicationOnDate() {
        return communicationOnDate;
    }

    public void setCommunicationOnDate(String communicationOnDate) {
        this.communicationOnDate = communicationOnDate;
    }

    public String getFromempName() {
        return fromempName;
    }

    public void setFromempName(String fromempName) {
        this.fromempName = fromempName;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public String getReviewedempname() {
        return reviewedempname;
    }

    public void setReviewedempname(String reviewedempname) {
        this.reviewedempname = reviewedempname;
    }

    public String getReviewedPost() {
        return reviewedPost;
    }

    public void setReviewedPost(String reviewedPost) {
        this.reviewedPost = reviewedPost;
    }

   
    
}
