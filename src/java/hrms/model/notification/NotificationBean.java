/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.notification;

import java.util.Date;

/**
 *
 * @author Surendra
 */
public class NotificationBean {

    private int notid;
    private String nottype = "";
    private String empId = "";
    private Date dateofEntry = null;
    private String ifAssumed = "";
    private String ordno = "";
    private Date ordDate = null;
    private String txtNotOrdDt="";
    private String deptCode = "";
    private String offCode = "";
    private String spc = "";
    private String note = "";
    private String ifVisible = "";
    private String entryDeptCode = "";
    private String entryOffCode = "";
    private String entryAuthCode = "";
    private String sancDeptCode = "";
    private String sancOffCode = "";
    private String sancAuthCode = "";
    
    private String cadreStatus;
    private String subCadreStatus;
    /*
     * Variables For Notifying Authority Portion
     * This will be Stored in EMP_NOTIFICATION TABLE(DEPT_CODE,OFF_CODE,AUTH)
     *
     * */
    private String sltdeptname;
    private String sltoffname;
    private String sltauth;
    private String txtdeptname;
    private String txtoffname;
    private String txtauth;
    private String hidntfnauth;
    private String radntfnauthtype;
    private String radpostingauthtype="";
    private String entryType=null;

    private String loginuserid;
    private int refcorrectionid;

    public String getNottype() {
        return nottype;
    }

    public void setNottype(String nottype) {
        this.nottype = nottype;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public Date getDateofEntry() {
        return dateofEntry;
    }

    public void setDateofEntry(Date dateofEntry) {
        this.dateofEntry = dateofEntry;
    }

    public String getIfAssumed() {
        return ifAssumed;
    }

    public void setIfAssumed(String ifAssumed) {
        this.ifAssumed = ifAssumed;
    }

    public String getOrdno() {
        return ordno;
    }

    public void setOrdno(String ordno) {
        this.ordno = ordno;
    }

    public Date getOrdDate() {
        return ordDate;
    }

    public void setOrdDate(Date ordDate) {
        this.ordDate = ordDate;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getOffCode() {
        return offCode;
    }

    public void setOffCode(String offCode) {
        this.offCode = offCode;
    }

    public String getSpc() {
        return spc;
    }

    public void setSpc(String spc) {
        this.spc = spc;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getIfVisible() {
        return ifVisible;
    }

    public void setIfVisible(String ifVisible) {
        this.ifVisible = ifVisible;
    }

    public String getEntryDeptCode() {
        return entryDeptCode;
    }

    public void setEntryDeptCode(String entryDeptCode) {
        this.entryDeptCode = entryDeptCode;
    }

    public String getEntryOffCode() {
        return entryOffCode;
    }

    public void setEntryOffCode(String entryOffCode) {
        this.entryOffCode = entryOffCode;
    }

    public String getEntryAuthCode() {
        return entryAuthCode;
    }

    public void setEntryAuthCode(String entryAuthCode) {
        this.entryAuthCode = entryAuthCode;
    }

    public String getSancDeptCode() {
        return sancDeptCode;
    }

    public void setSancDeptCode(String sancDeptCode) {
        this.sancDeptCode = sancDeptCode;
    }

    public String getSancOffCode() {
        return sancOffCode;
    }

    public void setSancOffCode(String sancOffCode) {
        this.sancOffCode = sancOffCode;
    }

    public String getSancAuthCode() {
        return sancAuthCode;
    }

    public void setSancAuthCode(String sancAuthCode) {
        this.sancAuthCode = sancAuthCode;
    }

    public String getSltdeptname() {
        return sltdeptname;
    }

    public void setSltdeptname(String sltdeptname) {
        this.sltdeptname = sltdeptname;
    }

    public String getSltoffname() {
        return sltoffname;
    }

    public void setSltoffname(String sltoffname) {
        this.sltoffname = sltoffname;
    }

    public String getSltauth() {
        return sltauth;
    }

    public void setSltauth(String sltauth) {
        this.sltauth = sltauth;
    }

    public String getTxtdeptname() {
        return txtdeptname;
    }

    public void setTxtdeptname(String txtdeptname) {
        this.txtdeptname = txtdeptname;
    }

    public String getTxtoffname() {
        return txtoffname;
    }

    public void setTxtoffname(String txtoffname) {
        this.txtoffname = txtoffname;
    }

    public String getTxtauth() {
        return txtauth;
    }

    public void setTxtauth(String txtauth) {
        this.txtauth = txtauth;
    }

    public String getHidntfnauth() {
        return hidntfnauth;
    }

    public void setHidntfnauth(String hidntfnauth) {
        this.hidntfnauth = hidntfnauth;
    }

    public String getRadntfnauthtype() {
        return radntfnauthtype;
    }

    public void setRadntfnauthtype(String radntfnauthtype) {
        this.radntfnauthtype = radntfnauthtype;
    }

    public String getCadreStatus() {
        return cadreStatus;
    }

    public void setCadreStatus(String cadreStatus) {
        this.cadreStatus = cadreStatus;
    }

    public String getSubCadreStatus() {
        return subCadreStatus;
    }

    public void setSubCadreStatus(String subCadreStatus) {
        this.subCadreStatus = subCadreStatus;
    }

    public String getTxtNotOrdDt() {
        return txtNotOrdDt;
    }

    public void setTxtNotOrdDt(String txtNotOrdDt) {
        this.txtNotOrdDt = txtNotOrdDt;
    }

    public int getNotid() {
        return notid;
    }

    public void setNotid(int notid) {
        this.notid = notid;
    }

    public String getRadpostingauthtype() {
        return radpostingauthtype;
    }

    public void setRadpostingauthtype(String radpostingauthtype) {
        this.radpostingauthtype = radpostingauthtype;
    }

    public String getLoginuserid() {
        return loginuserid;
    }

    public void setLoginuserid(String loginuserid) {
        this.loginuserid = loginuserid;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public int getRefcorrectionid() {
        return refcorrectionid;
    }

    public void setRefcorrectionid(int refcorrectionid) {
        this.refcorrectionid = refcorrectionid;
    }
}
