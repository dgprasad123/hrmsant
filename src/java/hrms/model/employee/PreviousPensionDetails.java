/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.employee;

import java.util.Date;

/**
 *
 * @author Surendra
 */
public class PreviousPensionDetails {
    
    private String empid;
    private String pensionType;
    private String pensionAmount;
    private String payableTreasury;
    private String bankName;
    private String branchName;
    private String pensionIssuingAuth = null;
    private String source;
    private String pPOFPPONo;
    private String pensionEffectiveDate = null;
    private String pensionId;
    private String entryTakenBy;
    private Date entryDate = null;
    private String ifscCode;

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getPensionType() {
        return pensionType;
    }

    public void setPensionType(String pensionType) {
        this.pensionType = pensionType;
    }

    public String getPensionAmount() {
        return pensionAmount;
    }

    public void setPensionAmount(String pensionAmount) {
        this.pensionAmount = pensionAmount;
    }

    public String getPayableTreasury() {
        return payableTreasury;
    }

    public void setPayableTreasury(String payableTreasury) {
        this.payableTreasury = payableTreasury;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getPensionIssuingAuth() {
        return pensionIssuingAuth;
    }

    public void setPensionIssuingAuth(String pensionIssuingAuth) {
        this.pensionIssuingAuth = pensionIssuingAuth;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getpPOFPPONo() {
        return pPOFPPONo;
    }

    public void setpPOFPPONo(String pPOFPPONo) {
        this.pPOFPPONo = pPOFPPONo;
    }

    public String getPensionEffectiveDate() {
        return pensionEffectiveDate;
    }

    public void setPensionEffectiveDate(String pensionEffectiveDate) {
        this.pensionEffectiveDate = pensionEffectiveDate;
    }

    public String getPensionId() {
        return pensionId;
    }

    public void setPensionId(String pensionId) {
        this.pensionId = pensionId;
    }

    public String getEntryTakenBy() {
        return entryTakenBy;
    }

    public void setEntryTakenBy(String entryTakenBy) {
        this.entryTakenBy = entryTakenBy;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    
    
    
    

    
    
    
    
}
