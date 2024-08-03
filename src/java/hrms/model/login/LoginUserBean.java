/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.login;

import java.io.Serializable;

/**
 *
 * @author Surendra
 */
public class LoginUserBean implements Serializable {

    private String loginempid;
    private String logingpfno;
    private String logindeptcode;
    private String logindeptname;
    private String loginoffcode;
    private String loginoffname;
    private String loginspc;
    private String loginspn;
    private String loginusertype;
    private String loginEmployeeType = "";
    private String loginurlName;
    private String logincadrecode;
    private String loginhasofficePriv = "N";
    private String loginuserid;
    private String loginpwd;
    private String loginusername;
    private String loginname;
    private String loginmobile;
    private String logingpc;
    private String logindistrictcode;
    private String loginadmintype;
    private String isauthority;
    private String loginAsDDO = "N";
    private String loginDDOCode = "";
    private String loginOffLevel = "";
    private String loginPostGrptype = "";
    private String loginIsDDOType = "";
    private String esignSessKey = null;

    private String dateOfVisit = null;
    private String ifVisited = null;
    private String parType;
    private String loginempSpecific;
    private String loginAsForeignbody;
    
    private String loginAdditionalChargeSpc;
    private String loginAdditionalChargeOffCode;
    private String loginAdditionalChargeDDOCode;
    private String loginadditionalChargeOfficename;

    public String getIfVisited() {
        return ifVisited;
    }

    public void setIfVisited(String ifVisited) {
        this.ifVisited = ifVisited;
    }

    public String getDateOfVisit() {
        return dateOfVisit;
    }

    public void setDateOfVisit(String dateOfVisit) {
        this.dateOfVisit = dateOfVisit;
    }

    
    
    public String getLoginadmintype() {
        return loginadmintype;
    }

    public void setLoginadmintype(String loginadmintype) {
        this.loginadmintype = loginadmintype;
    }

    public String getLoginempid() {
        return loginempid;
    }

    public void setLoginempid(String loginempid) {
        this.loginempid = loginempid;
    }

    public String getLogingpfno() {
        return logingpfno;
    }

    public void setLogingpfno(String logingpfno) {
        this.logingpfno = logingpfno;
    }

    public String getLogindeptcode() {
        return logindeptcode;
    }

    public void setLogindeptcode(String logindeptcode) {
        this.logindeptcode = logindeptcode;
    }

    public String getLogindeptname() {
        return logindeptname;
    }

    public void setLogindeptname(String logindeptname) {
        this.logindeptname = logindeptname;
    }

    public String getLoginoffcode() {
        return loginoffcode;
    }

    public void setLoginoffcode(String loginoffcode) {
        this.loginoffcode = loginoffcode;
    }

    public String getLoginoffname() {
        return loginoffname;
    }

    public void setLoginoffname(String loginoffname) {
        this.loginoffname = loginoffname;
    }

    public String getLoginspc() {
        return loginspc;
    }

    public void setLoginspc(String loginspc) {
        this.loginspc = loginspc;
    }

    public String getLoginspn() {
        return loginspn;
    }

    public void setLoginspn(String loginspn) {
        this.loginspn = loginspn;
    }

    public String getLoginusertype() {
        return loginusertype;
    }

    public void setLoginusertype(String loginusertype) {
        this.loginusertype = loginusertype;
    }

    public String getLoginurlName() {
        return loginurlName;
    }

    public void setLoginurlName(String loginurlName) {
        this.loginurlName = loginurlName;
    }

    public String getLogincadrecode() {
        return logincadrecode;
    }

    public void setLogincadrecode(String logincadrecode) {
        this.logincadrecode = logincadrecode;
    }

    public String getLoginhasofficePriv() {
        return loginhasofficePriv;
    }

    public void setLoginhasofficePriv(String loginhasofficePriv) {
        this.loginhasofficePriv = loginhasofficePriv;
    }

    public String getLoginuserid() {
        return loginuserid;
    }

    public void setLoginuserid(String loginuserid) {
        this.loginuserid = loginuserid;
    }

    public String getLoginpwd() {
        return loginpwd;
    }

    public void setLoginpwd(String loginpwd) {
        this.loginpwd = loginpwd;
    }

    public String getLoginusername() {
        return loginusername;
    }

    public void setLoginusername(String loginusername) {
        this.loginusername = loginusername;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getLoginmobile() {
        return loginmobile;
    }

    public void setLoginmobile(String loginmobile) {
        this.loginmobile = loginmobile;
    }

    public String getLogingpc() {
        return logingpc;
    }

    public void setLogingpc(String logingpc) {
        this.logingpc = logingpc;
    }

    public String getLogindistrictcode() {
        return logindistrictcode;
    }

    public void setLogindistrictcode(String logindistrictcode) {
        this.logindistrictcode = logindistrictcode;
    }

    public String getIsauthority() {
        return isauthority;
    }

    public void setIsauthority(String isauthority) {
        this.isauthority = isauthority;
    }

    public String getLoginAsDDO() {
        return loginAsDDO;
    }

    public void setLoginAsDDO(String loginAsDDO) {
        this.loginAsDDO = loginAsDDO;
    }

    public String getLoginDDOCode() {
        return loginDDOCode;
    }

    public void setLoginDDOCode(String loginDDOCode) {
        this.loginDDOCode = loginDDOCode;
    }

    public String getLoginOffLevel() {
        return loginOffLevel;
    }

    public void setLoginOffLevel(String loginOffLevel) {
        this.loginOffLevel = loginOffLevel;
    }

    public String getLoginPostGrptype() {
        return loginPostGrptype;
    }

    public void setLoginPostGrptype(String loginPostGrptype) {
        this.loginPostGrptype = loginPostGrptype;
    }

    public String getLoginEmployeeType() {
        return loginEmployeeType;
    }

    public void setLoginEmployeeType(String loginEmployeeType) {
        this.loginEmployeeType = loginEmployeeType;
    }

    public String getLoginIsDDOType() {
        return loginIsDDOType;
    }

    public void setLoginIsDDOType(String loginIsDDOType) {
        this.loginIsDDOType = loginIsDDOType;
    }

    public String getEsignSessKey() {
        return esignSessKey;
    }

    public void setEsignSessKey(String esignSessKey) {
        this.esignSessKey = esignSessKey;
    }

    public String getParType() {
        return parType;
    }

    public void setParType(String parType) {
        this.parType = parType;
    }

    public String getLoginempSpecific() {
        return loginempSpecific;
    }

    public void setLoginempSpecific(String loginempSpecific) {
        this.loginempSpecific = loginempSpecific;
    }

    public String getDeptcode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getLoginAsForeignbody() {
        return loginAsForeignbody;
    }

    public void setLoginAsForeignbody(String loginAsForeignbody) {
        this.loginAsForeignbody = loginAsForeignbody;
    }

    public String getLoginAdditionalChargeSpc() {
        return loginAdditionalChargeSpc;
    }

    public void setLoginAdditionalChargeSpc(String loginAdditionalChargeSpc) {
        this.loginAdditionalChargeSpc = loginAdditionalChargeSpc;
    }

    public String getLoginAdditionalChargeOffCode() {
        return loginAdditionalChargeOffCode;
    }

    public void setLoginAdditionalChargeOffCode(String loginAdditionalChargeOffCode) {
        this.loginAdditionalChargeOffCode = loginAdditionalChargeOffCode;
    }

    public String getLoginAdditionalChargeDDOCode() {
        return loginAdditionalChargeDDOCode;
    }

    public void setLoginAdditionalChargeDDOCode(String loginAdditionalChargeDDOCode) {
        this.loginAdditionalChargeDDOCode = loginAdditionalChargeDDOCode;
    }

    public String getLoginadditionalChargeOfficename() {
        return loginadditionalChargeOfficename;
    }

    public void setLoginadditionalChargeOfficename(String loginadditionalChargeOfficename) {
        this.loginadditionalChargeOfficename = loginadditionalChargeOfficename;
    }
}
