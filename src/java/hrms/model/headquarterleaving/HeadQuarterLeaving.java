/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.headquarterleaving;

import java.util.Date;

/**
 *
 * @author lenovo
 */
public class HeadQuarterLeaving {

    private String doe = null;
    private String ordno = null;
    private Date ordt = null;
    private String strordt = null;
    private String strOrdt = null;
    private String type = null;
    private Date fdate = null;
    private String strFdate = null;
    private String ftime = null;
    private Date edate = null;
    private String strEdate = null;
    private String etime = null;
    private String authPostName = null;
    private String postedPostName = null;
    private String hidTempAuthOffCode = null;
    private String hidTempAuthPost = null;
    private String hidTempPostedOffCode = null;
    private String hidTempPostedPost = null;
    private String hidTempPostedFieldOffCode = null;
    private String note = null;
    private String empid = null;
    private String hqlb = null;

    private int hnotid ;

    private String hidAuthDeptCode = null;
    private String hidAuthOffCode = null;
    private String authSpc = null;
    private String rdPostedAuthType = null;
    private String hidPostedDeptCode = null;
    private String hidPostedOffCode = null;
    private String postedspc = null;
    private String sltPostedFieldOff = null;
    private String hiddeptName = null;
    private String lcrid=null;
    private String permissionId = null;       
    private String hnotType = null;    
    private String hpayid = null;
    private double l_days;
    private String year=null;
    private String dv_id=null;
    private String chkNotSBPrintnew=null;
    private String chkNotSBPrintedit=null;
 
    private String isValidated;
    
    public String getDoe() {
        return doe;
    }

    public void setDoe(String doe) {
        this.doe = doe;
    }

    public String getOrdno() {
        return ordno;
    }

    public void setOrdno(String ordno) {
        this.ordno = ordno;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFtime() {
        return ftime;
    }

    public void setFtime(String ftime) {
        this.ftime = ftime;
    }

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
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

    public String getHqlb() {
        return hqlb;
    }

    public void setHqlb(String hqlb) {
        this.hqlb = hqlb;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
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

    public String getSltPostedFieldOff() {
        return sltPostedFieldOff;
    }

    public void setSltPostedFieldOff(String sltPostedFieldOff) {
        this.sltPostedFieldOff = sltPostedFieldOff;
    }

    public Date getOrdt() {
        return ordt;
    }

    public void setOrdt(Date ordt) {
        this.ordt = ordt;
    }

    public Date getFdate() {
        return fdate;
    }

    public void setFdate(Date fdate) {
        this.fdate = fdate;
    }

    public Date getEdate() {
        return edate;
    }

    public void setEdate(Date edate) {
        this.edate = edate;
    }

    public String getHiddeptName() {
        return hiddeptName;
    }

    public void setHiddeptName(String hiddeptName) {
        this.hiddeptName = hiddeptName;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public String getStrOrdt() {
        return strOrdt;
    }

    public void setStrOrdt(String strOrdt) {
        this.strOrdt = strOrdt;
    }

    public String getStrFdate() {
        return strFdate;
    }

    public void setStrFdate(String strFdate) {
        this.strFdate = strFdate;
    }

    public String getStrEdate() {
        return strEdate;
    }

    public void setStrEdate(String strEdate) {
        this.strEdate = strEdate;
    }

    public String getStrordt() {
        return strordt;
    }

    public void setStrordt(String strordt) {
        this.strordt = strordt;
    }

    public String getHnotType() {
        return hnotType;
    }

    public void setHnotType(String hnotType) {
        this.hnotType = hnotType;
    }

    public String getHpayid() {
        return hpayid;
    }

    public void setHpayid(String hpayid) {
        this.hpayid = hpayid;
    }

    public double getL_days() {
        return l_days;
    }

    public void setL_days(double l_days) {
        this.l_days = l_days;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDv_id() {
        return dv_id;
    }

    public void setDv_id(String dv_id) {
        this.dv_id = dv_id;
    }

    public String getLcrid() {
        return lcrid;
    }

    public void setLcrid(String lcrid) {
        this.lcrid = lcrid;
    }

    public int getHnotid() {
        return hnotid;
    }

    public void setHnotid(int hnotid) {
        this.hnotid = hnotid;
    }

    public String getIsValidated() {
        return isValidated;
    }

    public void setIsValidated(String isValidated) {
        this.isValidated = isValidated;
    }

    public String getChkNotSBPrintnew() {
        return chkNotSBPrintnew;
    }

    public void setChkNotSBPrintnew(String chkNotSBPrintnew) {
        this.chkNotSBPrintnew = chkNotSBPrintnew;
    }

    public String getChkNotSBPrintedit() {
        return chkNotSBPrintedit;
    }

    public void setChkNotSBPrintedit(String chkNotSBPrintedit) {
        this.chkNotSBPrintedit = chkNotSBPrintedit;
    }

    

}
