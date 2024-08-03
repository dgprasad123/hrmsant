/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.resignation;

/**
 *
 * @author Surendra
 */

public class Resignation {

    private String retid = null;
    private String notId = null;
    private String retiredEmpid = null;
    private String curspc = null;
    private String notType = null;
    private String authtyp = null;        
    private String doe = null;
    private String toe = null;
    private String ifAssumed = null;
    private String ordno = null;
    private String ordDate = null;
    private String authDeptCode = null;
    private String authOfficeCode = null;
    private String authSpc = null;
    private String note = null;
    private String regtype = null;
    private String retauthtyp = null;
    private String ifVisible = null;
    private String entDeptCode = null;
    private String entOfficeCode = null;
    private String entAuthCode = null;
    private String cadreStatus = null;
    private String subStatus = null;
    private String retiredfromdeptCode;
    private String retiredfromofficeCode;
    private String retiredfromspc;
    private String duedate;
    private String retired;
    
    private String rdTransaction;
    private String doResg=null;
    private String auth_dept_code=null;
    private String auth_off_code;
    private String ret_dept_code;
    private String ret_off_code;
    private int hidnotid;
    private String hidempid;
    private String retdDeptName;
    private String retdPostName;
    private String retdoffName;
    
    public String getRetid() {
        return retid;
    }

    public void setRetid(String retid) {
        this.retid = retid;
    }

    public String getRegtype() {
        return regtype;
    }

    public void setRegtype(String regtype) {
        this.regtype = regtype;
    }
    
    
    public String getNotId() {
        return notId;
    }

    public void setNotId(String notId) {
        this.notId = notId;
    }

    public String getRetiredEmpid() {
        return retiredEmpid;
    }

    public void setRetiredEmpid(String retiredEmpid) {
        this.retiredEmpid = retiredEmpid;
    }    

    public String getCurspc() {
        return curspc;
    }

    public void setCurspc(String curspc) {
        this.curspc = curspc;
    }

    public String getNotType() {
        return notType;
    }

    public void setNotType(String notType) {
        this.notType = notType;
    }

    public String getAuthtyp() {
        return authtyp;
    }

    public void setAuthtyp(String authtyp) {
        this.authtyp = authtyp;
    }    

    public String getDoe() {
        return doe;
    }

    public void setDoe(String doe) {
        this.doe = doe;
    }

    public String getToe() {
        return toe;
    }

    public void setToe(String toe) {
        this.toe = toe;
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

    public String getOrdDate() {
        return ordDate;
    }

    public void setOrdDate(String ordDate) {
        this.ordDate = ordDate;
    }

    public String getAuthDeptCode() {
        return authDeptCode;
    }

    public void setAuthDeptCode(String authDeptCode) {
        this.authDeptCode = authDeptCode;
    }

    public String getAuthOfficeCode() {
        return authOfficeCode;
    }

    public void setAuthOfficeCode(String authOfficeCode) {
        this.authOfficeCode = authOfficeCode;
    }

    public String getAuthSpc() {
        return authSpc;
    }

    public void setAuthSpc(String authSpc) {
        this.authSpc = authSpc;
    }       

    public String getRetauthtyp() {
        return retauthtyp;
    }

    public void setRetauthtyp(String retauthtyp) {
        this.retauthtyp = retauthtyp;
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

    public String getEntDeptCode() {
        return entDeptCode;
    }

    public void setEntDeptCode(String entDeptCode) {
        this.entDeptCode = entDeptCode;
    }

    public String getEntOfficeCode() {
        return entOfficeCode;
    }

    public void setEntOfficeCode(String entOfficeCode) {
        this.entOfficeCode = entOfficeCode;
    }

    public String getEntAuthCode() {
        return entAuthCode;
    }

    public void setEntAuthCode(String entAuthCode) {
        this.entAuthCode = entAuthCode;
    }

    public String getCadreStatus() {
        return cadreStatus;
    }

    public void setCadreStatus(String cadreStatus) {
        this.cadreStatus = cadreStatus;
    }

    public String getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(String subStatus) {
        this.subStatus = subStatus;
    }

    public String getRetiredfromdeptCode() {
        return retiredfromdeptCode;
    }

    public void setRetiredfromdeptCode(String retiredfromdeptCode) {
        this.retiredfromdeptCode = retiredfromdeptCode;
    }

    public String getRetiredfromofficeCode() {
        return retiredfromofficeCode;
    }

    public void setRetiredfromofficeCode(String retiredfromofficeCode) {
        this.retiredfromofficeCode = retiredfromofficeCode;
    }

    public String getRetiredfromspc() {
        return retiredfromspc;
    }

    public void setRetiredfromspc(String retiredfromspc) {
        this.retiredfromspc = retiredfromspc;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public String getRetired() {
        return retired;
    }

    public void setRetired(String retired) {
        this.retired = retired;
    }

    public String getRdTransaction() {
        return rdTransaction;
    }

    public void setRdTransaction(String rdTransaction) {
        this.rdTransaction = rdTransaction;
    }

    public String getDoResg() {
        return doResg;
    }

    public void setDoResg(String doResg) {
        this.doResg = doResg;
    }

    public String getAuth_dept_code() {
        return auth_dept_code;
    }

    public void setAuth_dept_code(String auth_dept_code) {
        this.auth_dept_code = auth_dept_code;
    }

    public String getAuth_off_code() {
        return auth_off_code;
    }

    public void setAuth_off_code(String auth_off_code) {
        this.auth_off_code = auth_off_code;
    }

    public String getRet_dept_code() {
        return ret_dept_code;
    }

    public void setRet_dept_code(String ret_dept_code) {
        this.ret_dept_code = ret_dept_code;
    }

    public String getRet_off_code() {
        return ret_off_code;
    }

    public void setRet_off_code(String ret_off_code) {
        this.ret_off_code = ret_off_code;
    }

    public int getHidnotid() {
        return hidnotid;
    }

    public void setHidnotid(int hidnotid) {
        this.hidnotid = hidnotid;
    }

   
    public String getHidempid() {
        return hidempid;
    }

    public void setHidempid(String hidempid) {
        this.hidempid = hidempid;
    }

    public String getRetdDeptName() {
        return retdDeptName;
    }

    public void setRetdDeptName(String retdDeptName) {
        this.retdDeptName = retdDeptName;
    }

    public String getRetdPostName() {
        return retdPostName;
    }

    public void setRetdPostName(String retdPostName) {
        this.retdPostName = retdPostName;
    }

    public String getRetdoffName() {
        return retdoffName;
    }

    public void setRetdoffName(String retdoffName) {
        this.retdoffName = retdoffName;
    }

    
    
}
