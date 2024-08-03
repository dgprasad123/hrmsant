/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.onlineTicketing;

import java.io.File;
import java.util.Date;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Surendra
 */
public class OnlineTicketing {

    private String username = null;
    private int ticketId;
    private String userId = null;
    private Date createdDateTime = null;
    private String createdDateTimeString = "";
    private String message = null;
    private Date closedDateTime = null;
    private String status = null;
    private Date overDueDateTime = null;
    private double durationForReply;
    private Date reopenDateTime = null;
    private String assignedToUserId = null;
    private String topicId;
    private String topicName;
    private String offname = "";
    private MultipartFile file;
    private int attachmentId;
    private String ofileName = null;
    private String dfileName = null;
    private int refId;
    private String refType = null;
    private String filePath = null;
    private String fileType = null;
    private String deptCode = null;
    private String distCode = null;
    private String sltdistName = null;
    private String fticketid = null;
    private String userType = null;
    private String month = null;
    private String year = null;
    private String pendingdays = null;

    private String loginId = null;
    private int intMonth;
    private int intYear;

    private String iread;

    private int totalticketReceived = 0;
    private int totalticketdisposed = 0;
    private int totalticketsent = 0;

    private String txtperiodFrom;
    private String txtperiodTo;
    private String ddoCode;
    
     private int topicsubId;
    private String subtopic;
    
    private String description;
    private String ticketValue;
    
    private int lessthan24Hours;
    private int lessthan48Hours;
    private int lessthan7days;
    private int lessthan30days;
    private int morethan30days;
    private String strTicketId;
    private String fullName;
    private String encticketid;
    private String solvedBy;
    
    private String forward;
    
   

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    
    

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

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getClosedDateTime() {
        return closedDateTime;
    }

    public void setClosedDateTime(Date closedDateTime) {
        this.closedDateTime = closedDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getOverDueDateTime() {
        return overDueDateTime;
    }

    public void setOverDueDateTime(Date overDueDateTime) {
        this.overDueDateTime = overDueDateTime;
    }

    public double getDurationForReply() {
        return durationForReply;
    }

    public void setDurationForReply(double durationForReply) {
        this.durationForReply = durationForReply;
    }

    public Date getReopenDateTime() {
        return reopenDateTime;
    }

    public void setReopenDateTime(Date reopenDateTime) {
        this.reopenDateTime = reopenDateTime;
    }

    public String getAssignedToUserId() {
        return assignedToUserId;
    }

    public void setAssignedToUserId(String assignedToUserId) {
        this.assignedToUserId = assignedToUserId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getOffname() {
        return offname;
    }

    public void setOffname(String offname) {
        this.offname = offname;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDistCode() {
        return distCode;
    }

    public void setDistCode(String distCode) {
        this.distCode = distCode;
    }

    public String getFticketid() {
        return fticketid;
    }

    public void setFticketid(String fticketid) {
        this.fticketid = fticketid;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSltdistName() {
        return sltdistName;
    }

    public void setSltdistName(String sltdistName) {
        this.sltdistName = sltdistName;
    }

    public String getPendingdays() {
        return pendingdays;
    }

    public void setPendingdays(String pendingdays) {
        this.pendingdays = pendingdays;
    }

    public String getCreatedDateTimeString() {
        return createdDateTimeString;
    }

    public void setCreatedDateTimeString(String createdDateTimeString) {
        this.createdDateTimeString = createdDateTimeString;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public int getIntMonth() {
        return intMonth;
    }

    public void setIntMonth(int intMonth) {
        this.intMonth = intMonth;
    }

    public int getIntYear() {
        return intYear;
    }

    public void setIntYear(int intYear) {
        this.intYear = intYear;
    }

    public int getTotalticketReceived() {
        return totalticketReceived;
    }

    public void setTotalticketReceived(int totalticketReceived) {
        this.totalticketReceived = totalticketReceived;
    }

    public int getTotalticketdisposed() {
        return totalticketdisposed;
    }

    public void setTotalticketdisposed(int totalticketdisposed) {
        this.totalticketdisposed = totalticketdisposed;
    }

    public int getTotalticketsent() {
        return totalticketsent;
    }

    public void setTotalticketsent(int totalticketsent) {
        this.totalticketsent = totalticketsent;
    }

    public String getIread() {
        return iread;
    }

    public void setIread(String iread) {
        this.iread = iread;
    }

    public String getTxtperiodFrom() {
        return txtperiodFrom;
    }

    public void setTxtperiodFrom(String txtperiodFrom) {
        this.txtperiodFrom = txtperiodFrom;
    }

    public String getTxtperiodTo() {
        return txtperiodTo;
    }

    public void setTxtperiodTo(String txtperiodTo) {
        this.txtperiodTo = txtperiodTo;
    }

    public String getDdoCode() {
        return ddoCode;
    }

    public void setDdoCode(String ddoCode) {
        this.ddoCode = ddoCode;
    }

    public int getTopicsubId() {
        return topicsubId;
    }

    public void setTopicsubId(int topicsubId) {
        this.topicsubId = topicsubId;
    }

    public String getSubtopic() {
        return subtopic;
    }

    public void setSubtopic(String subtopic) {
        this.subtopic = subtopic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTicketValue() {
        return ticketValue;
    }

    public void setTicketValue(String ticketValue) {
        this.ticketValue = ticketValue;
    }

    public int getLessthan24Hours() {
        return lessthan24Hours;
    }

    public void setLessthan24Hours(int lessthan24Hours) {
        this.lessthan24Hours = lessthan24Hours;
    }

    public int getLessthan48Hours() {
        return lessthan48Hours;
    }

    public void setLessthan48Hours(int lessthan48Hours) {
        this.lessthan48Hours = lessthan48Hours;
    }

    public int getLessthan7days() {
        return lessthan7days;
    }

    public void setLessthan7days(int lessthan7days) {
        this.lessthan7days = lessthan7days;
    }

    public int getLessthan30days() {
        return lessthan30days;
    }

    public void setLessthan30days(int lessthan30days) {
        this.lessthan30days = lessthan30days;
    }

    public int getMorethan30days() {
        return morethan30days;
    }

    public void setMorethan30days(int morethan30days) {
        this.morethan30days = morethan30days;
    }

    public void setStrTicketId(String strTicketId) {
        this.strTicketId = strTicketId;
    }

    public String getEncticketid() {
        return encticketid;
    }

    public void setEncticketid(String encticketid) {
        this.encticketid = encticketid;
    }

    public String getSolvedBy() {
        return solvedBy;
    }

    public void setSolvedBy(String solvedBy) {
        this.solvedBy = solvedBy;
    }

    public String getForward() {
        return forward;
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

}
