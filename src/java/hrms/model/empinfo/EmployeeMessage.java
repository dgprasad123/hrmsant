/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.empinfo;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author manisha
 */
public class EmployeeMessage {

    private String empid;
    private String empname;
    private String message;
    private String msgTitle;    
    private String senderSpc;
    private String recieverSpc;
    private String recieverSpn;
    private String offcode;
    private String isviewed;
    private String messageondate;
    private String viewondate;
    private String attachementname;
    private String repliedondate;
    private int attachmentid;
    private int noofAttachments;
    private List attachements;
    private List<MultipartFile> uploadedFile;
    private byte[] filecontent = null;
    private String filetype;
    private String senderId = null;
    private int messageId;
    
    private String senderName=null;
    private String receiverName=null;
    private String senderid;
    private String senderUserType;
    private String receiverUserType;
    private String sino;
    private String mobileNo;

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOffcode() {
        return offcode;
    }

    public void setOffcode(String offcode) {
        this.offcode = offcode;
    }

    public String getIsviewed() {
        return isviewed;
    }

    public void setIsviewed(String isviewed) {
        this.isviewed = isviewed;
    }

    public String getMessageondate() {
        return messageondate;
    }

    public void setMessageondate(String messageondate) {
        this.messageondate = messageondate;
    }

    public String getViewondate() {
        return viewondate;
    }

    public void setViewondate(String viewondate) {
        this.viewondate = viewondate;
    }

    public String getAttachementname() {
        return attachementname;
    }

    public void setAttachementname(String attachementname) {
        this.attachementname = attachementname;
    }

    public String getRepliedondate() {
        return repliedondate;
    }

    public void setRepliedondate(String repliedondate) {
        this.repliedondate = repliedondate;
    }

    public int getAttachmentid() {
        return attachmentid;
    }

    public void setAttachmentid(int attachmentid) {
        this.attachmentid = attachmentid;
    }

    public int getNoofAttachments() {
        return noofAttachments;
    }

    public void setNoofAttachments(int noofAttachments) {
        this.noofAttachments = noofAttachments;
    }

    public List<MultipartFile> getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(List<MultipartFile> uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public List getAttachements() {
        return attachements;
    }

    public void setAttachements(List attachements) {
        this.attachements = attachements;
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

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getSenderSpc() {
        return senderSpc;
    }

    public void setSenderSpc(String senderSpc) {
        this.senderSpc = senderSpc;
    }

    public String getRecieverSpc() {
        return recieverSpc;
    }

    public void setRecieverSpc(String recieverSpc) {
        this.recieverSpc = recieverSpc;
    }

    public String getRecieverSpn() {
        return recieverSpn;
    }

    public void setRecieverSpn(String recieverSpn) {
        this.recieverSpn = recieverSpn;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }



    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public String getSenderUserType() {
        return senderUserType;
    }

    public void setSenderUserType(String senderUserType) {
        this.senderUserType = senderUserType;
    }

    public String getReceiverUserType() {
        return receiverUserType;
    }

    public void setReceiverUserType(String receiverUserType) {
        this.receiverUserType = receiverUserType;
    }

    public String getSino() {
        return sino;
    }

    public void setSino(String sino) {
        this.sino = sino;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

   
    

}
