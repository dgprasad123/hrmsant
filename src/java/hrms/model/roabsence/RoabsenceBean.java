/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.roabsence;


public class RoabsenceBean {
    private int notificationId ;
    private String acId = null;
    private String orderNumber = null;
    private String orderDate = null;
    private String empId = null;
    private String notificationType = null;
    private String leaveId = null;
    private String leaveType = null;
    private String durationFrom = null;
    
    private String durationTo = null;
    
    private String noofDays = null;
    private String reason = null;
    
    private String note = null;
    private String hidAuthDeptCode = null;
    private String hidAuthOffCode = null;
    private String authSpc = null;
    private String rdPostedAuthType = null;
    private String hidPostedDeptCode = null;
    private String hidPostedOffCode = null;
    private String postedspc = null;
    
    private String hidTempDeptCode = null;
    private String hidTempAuthOffCode;
    private String hidTempAuthPost;
    private String authPostName = null;
    private String postedPostName = null;
    private String hidTempPostedOffCode;
    private String hidTempPostedPost;
    private String hidTempPostedFieldOffCode;
    private String heldUp = null;
    private String dateOfEntry = null;
    private String chkNotSBPrint=null;

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getAcId() {
        return acId;
    }

    public void setAcId(String acId) {
        this.acId = acId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getDurationFrom() {
        return durationFrom;
    }

    public void setDurationFrom(String durationFrom) {
        this.durationFrom = durationFrom;
    }

    public String getDurationTo() {
        return durationTo;
    }

    public void setDurationTo(String durationTo) {
        this.durationTo = durationTo;
    }

    public String getNoofDays() {
        return noofDays;
    }

    public void setNoofDays(String noofDays) {
        this.noofDays = noofDays;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getHidAuthDeptCode() {
        return hidAuthDeptCode;
    }

    public void setHidAuthDeptCode(String hidAuthDeptCode) {
        this.hidAuthDeptCode = hidAuthDeptCode;
    }

    public String getHidAuthOffCode() {
        return hidAuthOffCode;
    }

    public void setHidAuthOffCode(String hidAuthOffCode) {
        this.hidAuthOffCode = hidAuthOffCode;
    }

    public String getAuthSpc() {
        return authSpc;
    }

    public void setAuthSpc(String authSpc) {
        this.authSpc = authSpc;
    }

    public String getRdPostedAuthType() {
        return rdPostedAuthType;
    }

    public void setRdPostedAuthType(String rdPostedAuthType) {
        this.rdPostedAuthType = rdPostedAuthType;
    }

    public String getHidPostedDeptCode() {
        return hidPostedDeptCode;
    }

    public void setHidPostedDeptCode(String hidPostedDeptCode) {
        this.hidPostedDeptCode = hidPostedDeptCode;
    }

    public String getHidPostedOffCode() {
        return hidPostedOffCode;
    }

    public void setHidPostedOffCode(String hidPostedOffCode) {
        this.hidPostedOffCode = hidPostedOffCode;
    }

    public String getPostedspc() {
        return postedspc;
    }

    public void setPostedspc(String postedspc) {
        this.postedspc = postedspc;
    }

    public String getHidTempDeptCode() {
        return hidTempDeptCode;
    }

    public void setHidTempDeptCode(String hidTempDeptCode) {
        this.hidTempDeptCode = hidTempDeptCode;
    }

    public String getHidTempAuthOffCode() {
        return hidTempAuthOffCode;
    }

    public void setHidTempAuthOffCode(String hidTempAuthOffCode) {
        this.hidTempAuthOffCode = hidTempAuthOffCode;
    }

    public String getHidTempAuthPost() {
        return hidTempAuthPost;
    }

    public void setHidTempAuthPost(String hidTempAuthPost) {
        this.hidTempAuthPost = hidTempAuthPost;
    }

    public String getAuthPostName() {
        return authPostName;
    }

    public void setAuthPostName(String authPostName) {
        this.authPostName = authPostName;
    }

    public String getPostedPostName() {
        return postedPostName;
    }

    public void setPostedPostName(String postedPostName) {
        this.postedPostName = postedPostName;
    }

    public String getHidTempPostedOffCode() {
        return hidTempPostedOffCode;
    }

    public void setHidTempPostedOffCode(String hidTempPostedOffCode) {
        this.hidTempPostedOffCode = hidTempPostedOffCode;
    }

    public String getHidTempPostedPost() {
        return hidTempPostedPost;
    }

    public void setHidTempPostedPost(String hidTempPostedPost) {
        this.hidTempPostedPost = hidTempPostedPost;
    }

    public String getHidTempPostedFieldOffCode() {
        return hidTempPostedFieldOffCode;
    }

    public void setHidTempPostedFieldOffCode(String hidTempPostedFieldOffCode) {
        this.hidTempPostedFieldOffCode = hidTempPostedFieldOffCode;
    }

    public String getHeldUp() {
        return heldUp;
    }

    public void setHeldUp(String heldUp) {
        this.heldUp = heldUp;
    }

    public String getDateOfEntry() {
        return dateOfEntry;
    }

    public void setDateOfEntry(String dateOfEntry) {
        this.dateOfEntry = dateOfEntry;
    }

    public String getChkNotSBPrint() {
        return chkNotSBPrint;
    }

    public void setChkNotSBPrint(String chkNotSBPrint) {
        this.chkNotSBPrint = chkNotSBPrint;
    }
    
}
