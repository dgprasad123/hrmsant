/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.employee;

/**
 *
 * @author Durga
 */
public class IdentityInfo {
    private String hidIdentityId=null;
    private String identityDocType = null;
    private String identityDocNo = null;
    private String identityNo = "";
    private String identityDesc = "";
    private String issueDate = "";
    private String expiryDate = "";
    private String placeOfIssue = "";
    private String empId = null;
    private String printMsg=null;
    
    private String isLocked;
    private String isVerified;
    
    public String getPrintMsg() {
        return printMsg;
    }

    public void setPrintMsg(String printMsg) {
        this.printMsg = printMsg;
    }
    

    public String getHidIdentityId() {
        return hidIdentityId;
    }

    public void setHidIdentityId(String hidIdentityId) {
        this.hidIdentityId = hidIdentityId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getIdentityNo() {
        return identityNo;
    }

    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }

    public String getIdentityDesc() {
        return identityDesc;
    }

    public void setIdentityDesc(String identityDesc) {
        this.identityDesc = identityDesc;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getPlaceOfIssue() {
        return placeOfIssue;
    }

    public void setPlaceOfIssue(String placeOfIssue) {
        this.placeOfIssue = placeOfIssue;
    }

    public String getIdentityDocType() {
        return identityDocType;
    }

    public void setIdentityDocType(String identityDocType) {
        this.identityDocType = identityDocType;
    }

    public String getIdentityDocNo() {
        return identityDocNo;
    }

    public void setIdentityDocNo(String identityDocNo) {
        this.identityDocNo = identityDocNo;
    }

    public String getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(String isLocked) {
        this.isLocked = isLocked;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }
}
