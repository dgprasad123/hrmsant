/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.empinfo;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Manas
 */
public class EmployeeGrievance {
    public int gid;
    private String ackno;
    private String categoryCode; 
    private String grievanceStatus;
    private String category;
    private String fullname;
    private String hrmsid;
    private String spc;
    private String appoffcode;
    private String offname;
    private String grievanceDetail;
    private String appmobile;
    private String isdisposed;
    private String grievanceTime;
    private String isforwarded;
    private String attachementName;
    private String attachementId;
    private MultipartFile attachment;
    private String source;
    private String authCode;
    private String isrejected;
    private String remark;
    private String finalRemark;
    private String grievanceFromDate;
    private String grievanceToDate;
    private String pendingat;
    
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    

    public String getIsrejected() {
        return isrejected;
    }

    public void setIsrejected(String isrejected) {
        this.isrejected = isrejected;
    }
            
    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getAckno() {
        return ackno;
    }

    public void setAckno(String ackno) {
        this.ackno = ackno;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getGrievanceStatus() {
        return grievanceStatus;
    }

    public void setGrievanceStatus(String grievanceStatus) {
        this.grievanceStatus = grievanceStatus;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getHrmsid() {
        return hrmsid;
    }

    public void setHrmsid(String hrmsid) {
        this.hrmsid = hrmsid;
    }

    public String getSpc() {
        return spc;
    }

    public void setSpc(String spc) {
        this.spc = spc;
    }

    public String getAppoffcode() {
        return appoffcode;
    }

    public void setAppoffcode(String appoffcode) {
        this.appoffcode = appoffcode;
    }   

    public String getOffname() {
        return offname;
    }

    public void setOffname(String offname) {
        this.offname = offname;
    }

    public String getGrievanceDetail() {
        return grievanceDetail;
    }

    public void setGrievanceDetail(String grievanceDetail) {
        this.grievanceDetail = grievanceDetail;
    }

    public String getAppmobile() {
        return appmobile;
    }

    public void setAppmobile(String appmobile) {
        this.appmobile = appmobile;
    }    

    public String getIsdisposed() {
        return isdisposed;
    }

    public void setIsdisposed(String isdisposed) {
        this.isdisposed = isdisposed;
    }    

    public String getGrievanceTime() {
        return grievanceTime;
    }

    public void setGrievanceTime(String grievanceTime) {
        this.grievanceTime = grievanceTime;
    }

    public String getIsforwarded() {
        return isforwarded;
    }

    public void setIsforwarded(String isforwarded) {
        this.isforwarded = isforwarded;
    }

    public String getAttachementName() {
        return attachementName;
    }

    public void setAttachementName(String attachementName) {
        this.attachementName = attachementName;
    }

    public String getAttachementId() {
        return attachementId;
    }

    public void setAttachementId(String attachementId) {
        this.attachementId = attachementId;
    }

    public MultipartFile getAttachment() {
        return attachment;
    }

    public void setAttachment(MultipartFile attachment) {
        this.attachment = attachment;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getFinalRemark() {
        return finalRemark;
    }

    public void setFinalRemark(String finalRemark) {
        this.finalRemark = finalRemark;
    }

    public String getGrievanceFromDate() {
        return grievanceFromDate;
    }

    public void setGrievanceFromDate(String grievanceFromDate) {
        this.grievanceFromDate = grievanceFromDate;
    }

    public String getGrievanceToDate() {
        return grievanceToDate;
    }

    public void setGrievanceToDate(String grievanceToDate) {
        this.grievanceToDate = grievanceToDate;
    }

    public String getPendingat() {
        return pendingat;
    }

    public void setPendingat(String pendingat) {
        this.pendingat = pendingat;
    }
    
}
