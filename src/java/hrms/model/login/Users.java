/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.login;

import hrms.common.CommonFunctions;
import hrms.model.cadre.Cadre;
import hrms.model.master.Office;
import hrms.model.master.SubstantivePost;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Surendra
 */
public class Users implements Serializable {

    private String empId;

    private Date paydate;

    private String userName;

    private String userPassword;

    private String hrmsEncId;

    private boolean enabled;

    private Set<UserRole> userRole = new HashSet<UserRole>(0);

    private String hasPrivilages;

    private String isddoLogin = "N";

    private String hasManageRent = "N";

    private String hasmyCadreTab;

    private String hasmyDeptTab;

    private String hasmyDistTab;

    private String hasmyHodTab;

    private String hasmyOfficeTab;

    private String hasparadminTab;

    private String haspoliceDGTab;

    private String hasPayRevisionAuth;

    private String hascheckingAuth;

    private String hasverifyingAuth;

    private String hasCommandandAuthPriv;

    private String hasProfileAuthorization;

    private String hasAERAuthorization;

    private String hasAerForReviewerAuthorization;

    private String hasAerForAcceptorAuthorization;

    private String hasServiceBookValidateAuthorization = "";

    private String newpassword;

    private String confirmpassword;

    private String initials;

    private String fname;

    private String mname;

    private String lname;

    private String usertype;

    private Date doegov;

    private Date dob;

    private String mobile;

    private String emailId = "";

    private String depcode;

    private String depstatus;

    private String payheldupstatus;

    private String ifgpfAssumed = "";

    private String postgrp;

    private String acctType;

    private String gpfno;

    private Date dateOfnincr;

    private double gradepay = 0;

    private String payscale;

    private Double curBasic;

    private Cadre cadre;

    private String cadrecode = "";

    private String cadrename = "";

    private String offname = "";

    private String officeLevel = "";

    private String offcode = "";

    private String distCode = "";

    private String ddoCode = "";

    private String fullName;

    private Office office;

    private SubstantivePost substantivePost;

    private String curspc;

    private String spn = null;

    private String aadharno;

    private String postname = null;

    private String captcha;//new line

    private String deptcode;
    private String gpc = null;
    private String deptName;

    private String postCode;

    private String gisNo;
    private String gisType;
    private String gender;
    private String marital;
    private String bloodGrp;
    private String category;

    private Date dos;
    private String homeTown;
    private String domicil;
    private String religion;
    private String height;
    private String idMark;
    private Date joinDateGoo;
    private String toeGov;
    private String joiningTime;
    private String bankAccNo;
    private String bankCode;
    private String branchCode;
    private String ifprofileCompleted = null;
    private String ifprofileVerified = null;
    private String loginEmpid = null;
    private String strIncrDate = null;

    private String hasPostTerminationForVerifier;
    private String hasPostTerminationForAcceptor;
    private String hasparreviewingAuthorization;
    private String hasparcustodianAuthorization;
    private String hasparviewingAuthorization;
    private String haspropertyadminAuthorization;
    private String isNodalOfficerSiPar;

    private String payCommission = "";

    private String ifmsCode;
    private String isRegular;
    private String gp;
    private String basicsalry;
    private String address;
    private String empDob;
    private String empDos;
    private String isauthority;

    private String hasGroupCcustodianAuthorization;
    private String empIsDDO;
    private String ifVisited = null;

    private String loginType;
    private String ifRetired;
    private String ifReengaged;

    private String hasQMSLink;
    
    private String hasOfficeWisePARCustodianAuthorization;
    private String isForeignbodyEmp;
    
    private String additionalChargespc;
    private String hasAERAuthorizationForAdditionalChargeSPC;
    private String additionalChargeOffCode;
    private String additionalChargeDDOCode;
    private String additionalChargeOfficename;
    
    private String hasPQMSLink;

    public String getIfVisited() {
        return ifVisited;
    }

    public void setIfVisited(String ifVisited) {
        this.ifVisited = ifVisited;
    }

    public Date getDos() {
        return dos;
    }

    public void setDos(Date dos) {
        this.dos = dos;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }

    public String getDomicil() {
        return domicil;
    }

    public void setDomicil(String domicil) {
        this.domicil = domicil;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getIdMark() {
        return idMark;
    }

    public void setIdMark(String idMark) {
        this.idMark = idMark;
    }

    public Date getJoinDateGoo() {
        return joinDateGoo;
    }

    public void setJoinDateGoo(Date joinDateGoo) {
        this.joinDateGoo = joinDateGoo;
    }

    public String getToeGov() {
        return toeGov;
    }

    public void setToeGov(String toeGov) {
        this.toeGov = toeGov;
    }

    public String getJoiningTime() {
        return joiningTime;
    }

    public void setJoiningTime(String joiningTime) {
        this.joiningTime = joiningTime;
    }

    public String getBankAccNo() {
        return bankAccNo;
    }

    public void setBankAccNo(String bankAccNo) {
        this.bankAccNo = bankAccNo;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMarital() {
        return marital;
    }

    public void setMarital(String marital) {
        this.marital = marital;
    }

    public String getBloodGrp() {
        return bloodGrp;
    }

    public void setBloodGrp(String bloodGrp) {
        this.bloodGrp = bloodGrp;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGisType() {
        return gisType;
    }

    public void setGisType(String gisType) {
        this.gisType = gisType;
    }

    public String getGisNo() {
        return gisNo;
    }

    public void setGisNo(String gisNo) {
        this.gisNo = gisNo;
    }

    public String getGpc() {
        return gpc;
    }

    public void setGpc(String gpc) {
        this.gpc = gpc;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptcode() {
        return deptcode;
    }

    public void setDeptcode(String deptcode) {
        this.deptcode = deptcode;
    }

    public String getPostname() {
        return postname;
    }

    public void setPostname(String postname) {
        this.postname = postname;
    }

    public String getSpn() {
        return spn;
    }

    public void setSpn(String spn) {
        this.spn = spn;
    }

    public String getCadrename() {
        return cadrename;
    }

    public void setCadrename(String cadrename) {
        this.cadrename = cadrename;
    }

    public String getOffname() {
        return offname;
    }

    public void setOffname(String offname) {
        this.offname = offname;
    }

    public String getOffcode() {
        return offcode;
    }

    public void setOffcode(String offcode) {
        this.offcode = offcode;
    }

    public String getCurspc() {
        return curspc;
    }

    public void setCurspc(String curspc) {
        this.curspc = curspc;
    }

    public String getAadharno() {
        return aadharno;
    }

    public void setAadharno(String aadharno) {
        this.aadharno = aadharno;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getHrmsEncId() {
        return hrmsEncId;
    }

    public void setHrmsEncId(String hrmsEncId) {
        this.hrmsEncId = hrmsEncId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<UserRole> getUserRole() {
        return userRole;
    }

    public void setUserRole(Set<UserRole> userRole) {
        this.userRole = userRole;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHasPrivilages() {
        return hasPrivilages;
    }

    public void setHasPrivilages(String hasPrivilages) {
        this.hasPrivilages = hasPrivilages;
    }

    public String getHasmyCadreTab() {
        return hasmyCadreTab;
    }

    public void setHasmyCadreTab(String hasmyCadreTab) {
        this.hasmyCadreTab = hasmyCadreTab;
    }

    public String getHasmyDeptTab() {
        return hasmyDeptTab;
    }

    public void setHasmyDeptTab(String hasmyDeptTab) {
        this.hasmyDeptTab = hasmyDeptTab;
    }

    public String getHasmyDistTab() {
        return hasmyDistTab;
    }

    public void setHasmyDistTab(String hasmyDistTab) {
        this.hasmyDistTab = hasmyDistTab;
    }

    public String getHasmyHodTab() {
        return hasmyHodTab;
    }

    public void setHasmyHodTab(String hasmyHodTab) {
        this.hasmyHodTab = hasmyHodTab;
    }

    public String getHasmyOfficeTab() {
        return hasmyOfficeTab;
    }

    public void setHasmyOfficeTab(String hasmyOfficeTab) {
        this.hasmyOfficeTab = hasmyOfficeTab;
    }

    public String getHasparadminTab() {
        return hasparadminTab;
    }

    public void setHasparadminTab(String hasparadminTab) {
        this.hasparadminTab = hasparadminTab;
    }

    public String getHaspoliceDGTab() {
        return haspoliceDGTab;
    }

    public void setHaspoliceDGTab(String haspoliceDGTab) {
        this.haspoliceDGTab = haspoliceDGTab;
    }

    public String getHasPayRevisionAuth() {
        return hasPayRevisionAuth;
    }

    public void setHasPayRevisionAuth(String hasPayRevisionAuth) {
        this.hasPayRevisionAuth = hasPayRevisionAuth;
    }

    public String getHascheckingAuth() {
        return hascheckingAuth;
    }

    public void setHascheckingAuth(String hascheckingAuth) {
        this.hascheckingAuth = hascheckingAuth;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAcctType() {
        return acctType;
    }

    public void setAcctType(String acctType) {
        this.acctType = acctType;
    }

    public String getGpfno() {
        return gpfno;
    }

    public void setGpfno(String gpfno) {
        this.gpfno = gpfno;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public SubstantivePost getSubstantivePost() {
        return substantivePost;
    }

    public void setSubstantivePost(SubstantivePost substantivePost) {
        this.substantivePost = substantivePost;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public Date getDoegov() {
        return doegov;
    }

    public String getFormattedDoegov() {
        return CommonFunctions.getFormattedOutputDate3(doegov);
    }

    public void setDoegov(Date doegov) {
        this.doegov = doegov;
    }

    public Date getDob() {
        return dob;
    }

    public String getFormattedDob() {
        return CommonFunctions.getFormattedOutputDate3(dob);
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Cadre getCadre() {
        return cadre;
    }

    public void setCadre(Cadre cadre) {
        this.cadre = cadre;
    }

    public Date getDateOfnincr() {
        return dateOfnincr;
    }

    public void setDateOfnincr(Date dateOfnincr) {
        this.dateOfnincr = dateOfnincr;
    }

    public String getPostgrp() {
        return postgrp;
    }

    public void setPostgrp(String postgrp) {
        this.postgrp = postgrp;
    }

    public double getGradepay() {
        return gradepay;
    }

    public void setGradepay(double gradepay) {
        this.gradepay = gradepay;
    }

    public String getPayscale() {
        return payscale;
    }

    public void setPayscale(String payscale) {
        this.payscale = payscale;
    }

    public Double getCurBasic() {
        return curBasic;
    }

    public void setCurBasic(Double curBasic) {
        this.curBasic = curBasic;
    }

    public Date getPaydate() {
        return paydate;
    }

    public void setPaydate(Date paydate) {
        this.paydate = paydate;
    }

    public String getDepcode() {
        return depcode;
    }

    public void setDepcode(String depcode) {
        this.depcode = depcode;
    }

    public String getDepstatus() {
        return depstatus;
    }

    public void setDepstatus(String depstatus) {
        this.depstatus = depstatus;
    }

    public String getCadrecode() {
        return cadrecode;
    }

    public void setCadrecode(String cadrecode) {
        this.cadrecode = cadrecode;
    }

    public String getHasverifyingAuth() {
        return hasverifyingAuth;
    }

    public void setHasverifyingAuth(String hasverifyingAuth) {
        this.hasverifyingAuth = hasverifyingAuth;
    }

    public String getHasCommandandAuthPriv() {
        return hasCommandandAuthPriv;
    }

    public void setHasCommandandAuthPriv(String hasCommandandAuthPriv) {
        this.hasCommandandAuthPriv = hasCommandandAuthPriv;
    }

    public String getDdoCode() {
        return ddoCode;
    }

    public void setDdoCode(String ddoCode) {
        this.ddoCode = ddoCode;
    }

    public String getHasProfileAuthorization() {
        return hasProfileAuthorization;
    }

    public void setHasProfileAuthorization(String hasProfileAuthorization) {
        this.hasProfileAuthorization = hasProfileAuthorization;
    }

    public String getIfprofileCompleted() {
        return ifprofileCompleted;
    }

    public void setIfprofileCompleted(String ifprofileCompleted) {
        this.ifprofileCompleted = ifprofileCompleted;
    }

    public String getIfprofileVerified() {
        return ifprofileVerified;
    }

    public void setIfprofileVerified(String ifprofileVerified) {
        this.ifprofileVerified = ifprofileVerified;
    }

    public String getHasAERAuthorization() {
        return hasAERAuthorization;
    }

    public void setHasAERAuthorization(String hasAERAuthorization) {
        this.hasAERAuthorization = hasAERAuthorization;
    }

    public String getHasAerForReviewerAuthorization() {
        return hasAerForReviewerAuthorization;
    }

    public void setHasAerForReviewerAuthorization(String hasAerForReviewerAuthorization) {
        this.hasAerForReviewerAuthorization = hasAerForReviewerAuthorization;
    }

    public String getHasAerForAcceptorAuthorization() {
        return hasAerForAcceptorAuthorization;
    }

    public void setHasAerForAcceptorAuthorization(String hasAerForAcceptorAuthorization) {
        this.hasAerForAcceptorAuthorization = hasAerForAcceptorAuthorization;
    }

    public String getStrIncrDate() {
        return strIncrDate;
    }

    public void setStrIncrDate(String strIncrDate) {
        this.strIncrDate = strIncrDate;
    }

    public String getLoginEmpid() {
        return loginEmpid;
    }

    public void setLoginEmpid(String loginEmpid) {
        this.loginEmpid = loginEmpid;
    }

    public String getHasPostTerminationForVerifier() {
        return hasPostTerminationForVerifier;
    }

    public void setHasPostTerminationForVerifier(String hasPostTerminationForVerifier) {
        this.hasPostTerminationForVerifier = hasPostTerminationForVerifier;
    }

    public String getHasPostTerminationForAcceptor() {
        return hasPostTerminationForAcceptor;
    }

    public void setHasPostTerminationForAcceptor(String hasPostTerminationForAcceptor) {
        this.hasPostTerminationForAcceptor = hasPostTerminationForAcceptor;
    }

    public String getHasparreviewingAuthorization() {
        return hasparreviewingAuthorization;
    }

    public void setHasparreviewingAuthorization(String hasparreviewingAuthorization) {
        this.hasparreviewingAuthorization = hasparreviewingAuthorization;
    }

    public String getHasparcustodianAuthorization() {
        return hasparcustodianAuthorization;
    }

    public void setHasparcustodianAuthorization(String hasparcustodianAuthorization) {
        this.hasparcustodianAuthorization = hasparcustodianAuthorization;
    }

    public String getHasparviewingAuthorization() {
        return hasparviewingAuthorization;
    }

    public void setHasparviewingAuthorization(String hasparviewingAuthorization) {
        this.hasparviewingAuthorization = hasparviewingAuthorization;
    }

    public String getPayheldupstatus() {
        return payheldupstatus;
    }

    public void setPayheldupstatus(String payheldupstatus) {
        this.payheldupstatus = payheldupstatus;
    }

    public String getIfgpfAssumed() {
        return ifgpfAssumed;
    }

    public void setIfgpfAssumed(String ifgpfAssumed) {
        this.ifgpfAssumed = ifgpfAssumed;
    }

    public String getPayCommission() {
        return payCommission;
    }

    public void setPayCommission(String payCommission) {
        this.payCommission = payCommission;
    }

    public String getIfmsCode() {
        return ifmsCode;
    }

    public void setIfmsCode(String ifmsCode) {
        this.ifmsCode = ifmsCode;
    }

    public String getIsRegular() {
        return isRegular;
    }

    public void setIsRegular(String isRegular) {
        this.isRegular = isRegular;
    }

    public String getGp() {
        return gp;
    }

    public void setGp(String gp) {
        this.gp = gp;
    }

    public String getBasicsalry() {
        return basicsalry;
    }

    public void setBasicsalry(String basicsalry) {
        this.basicsalry = basicsalry;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmpDob() {
        return empDob;
    }

    public void setEmpDob(String empDob) {
        this.empDob = empDob;
    }

    public String getEmpDos() {
        return empDos;
    }

    public void setEmpDos(String empDos) {
        this.empDos = empDos;
    }

    public String getIsauthority() {
        return isauthority;
    }

    public void setIsauthority(String isauthority) {
        this.isauthority = isauthority;
    }

    public String getIsddoLogin() {
        return isddoLogin;
    }

    public void setIsddoLogin(String isddoLogin) {
        this.isddoLogin = isddoLogin;
    }

    public String getHasManageRent() {
        return hasManageRent;
    }

    public void setHasManageRent(String hasManageRent) {
        this.hasManageRent = hasManageRent;
    }

    public String getDistCode() {
        return distCode;
    }

    public void setDistCode(String distCode) {
        this.distCode = distCode;
    }

    public String getOfficeLevel() {
        return officeLevel;
    }

    public void setOfficeLevel(String officeLevel) {
        this.officeLevel = officeLevel;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getHaspropertyadminAuthorization() {
        return haspropertyadminAuthorization;
    }

    public void setHaspropertyadminAuthorization(String haspropertyadminAuthorization) {
        this.haspropertyadminAuthorization = haspropertyadminAuthorization;
    }

    public String getHasServiceBookValidateAuthorization() {
        return hasServiceBookValidateAuthorization;
    }

    public void setHasServiceBookValidateAuthorization(String hasServiceBookValidateAuthorization) {
        this.hasServiceBookValidateAuthorization = hasServiceBookValidateAuthorization;
    }

    public String getHasGroupCcustodianAuthorization() {
        return hasGroupCcustodianAuthorization;
    }

    public void setHasGroupCcustodianAuthorization(String hasGroupCcustodianAuthorization) {
        this.hasGroupCcustodianAuthorization = hasGroupCcustodianAuthorization;
    }

    public String getEmpIsDDO() {
        return empIsDDO;
    }

    public void setEmpIsDDO(String empIsDDO) {
        this.empIsDDO = empIsDDO;
    }

    public String getIsNodalOfficerSiPar() {
        return isNodalOfficerSiPar;
    }

    public void setIsNodalOfficerSiPar(String isNodalOfficerSiPar) {
        this.isNodalOfficerSiPar = isNodalOfficerSiPar;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getIfRetired() {
        return ifRetired;
    }

    public void setIfRetired(String ifRetired) {
        this.ifRetired = ifRetired;
    }

    public String getIfReengaged() {
        return ifReengaged;
    }

    public void setIfReengaged(String ifReengaged) {
        this.ifReengaged = ifReengaged;
    }

    public String getHasQMSLink() {
        return hasQMSLink;
    }

    public void setHasQMSLink(String hasQMSLink) {
        this.hasQMSLink = hasQMSLink;
    }

    public String getHasOfficeWisePARCustodianAuthorization() {
        return hasOfficeWisePARCustodianAuthorization;
    }

    public void setHasOfficeWisePARCustodianAuthorization(String hasOfficeWisePARCustodianAuthorization) {
        this.hasOfficeWisePARCustodianAuthorization = hasOfficeWisePARCustodianAuthorization;
    }

    public String getIsForeignbodyEmp() {
        return isForeignbodyEmp;
    }

    public void setIsForeignbodyEmp(String isForeignbodyEmp) {
        this.isForeignbodyEmp = isForeignbodyEmp;
    }

    public String getAdditionalChargespc() {
        return additionalChargespc;
    }

    public void setAdditionalChargespc(String additionalChargespc) {
        this.additionalChargespc = additionalChargespc;
    }

    public String getHasAERAuthorizationForAdditionalChargeSPC() {
        return hasAERAuthorizationForAdditionalChargeSPC;
    }

    public void setHasAERAuthorizationForAdditionalChargeSPC(String hasAERAuthorizationForAdditionalChargeSPC) {
        this.hasAERAuthorizationForAdditionalChargeSPC = hasAERAuthorizationForAdditionalChargeSPC;
    }

    public String getAdditionalChargeOffCode() {
        return additionalChargeOffCode;
    }

    public void setAdditionalChargeOffCode(String additionalChargeOffCode) {
        this.additionalChargeOffCode = additionalChargeOffCode;
    }

    public String getAdditionalChargeDDOCode() {
        return additionalChargeDDOCode;
    }

    public void setAdditionalChargeDDOCode(String additionalChargeDDOCode) {
        this.additionalChargeDDOCode = additionalChargeDDOCode;
    }

    public String getAdditionalChargeOfficename() {
        return additionalChargeOfficename;
    }

    public void setAdditionalChargeOfficename(String additionalChargeOfficename) {
        this.additionalChargeOfficename = additionalChargeOfficename;
    }

    public String getHasPQMSLink() {
        return hasPQMSLink;
    }

    public void setHasPQMSLink(String hasPQMSLink) {
        this.hasPQMSLink = hasPQMSLink;
    }
}
