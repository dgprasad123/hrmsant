/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.employee;

import java.util.ArrayList;
import java.util.Date;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author manas jena
 */
public class Employee {

    private String empid;
    private String fname;
    private String mname;
    private String lname;
    private String fullname1;
    private String gender;
    private String marital;
    private String maritalStatusId;
    private String category;
    private String domicile;
    private String base64Image;
    private String recsource;//Requirement Source
    private double height;
    private String gpfno;
    private int basic;
    private String dob;
    private String joindategoo;
    private String dor;
    private String department;
    private String deptcode;
    private String post;
    private String postcode;
    private String cardeallotmentyear;
    private String spn;
    private String spc;
    private String office;
    private String officecode;
    private String bloodgrp;
    private String religion;
    private String religionName;
    private String permanentaddr;
    private String presentaddr;
    private String permanentdist;
    private String permanentps;
    private String phyhandicapt;
    private String idmark;
    private String mobile;
    private String prmTelNo;
    private String dateOfCurPosting;
    private int gp;
    private String payScale;
    private String cadreCode;
    private String cadreId;
    private String postGrpType;
    private String email;

    public String getHasNoEmail() {
        return hasNoEmail;
    }

    public void setHasNoEmail(String hasNoEmail) {
        this.hasNoEmail = hasNoEmail;
    }
    private String hasNoEmail;
    private String accttype;
    private String fieldOffCode;

    private String chest;
    private String weight;
    private String leftvision;
    private String rightvision;
    private String brassno;

    private String station;
    private String addlCharge;
    private String remark;

    private String cadreGrade;
    private String aadhaarno;
    private String panno;
    private String epic;
    private String drivingLicence;

    private String entryGovDateText = null;
    private String dobText;
    private String joinDateText;
    private String ifReservation;
    private String ifRehabiltation;
    private String doeGov = null;
    private String homeTown = null;
    private String empName;
    private String empCadre = null;
    private String empAllotmentYear = null;
    private String ifpPan = null;
    private String serverDate = null;
    private String residenceAdd = null;
    private ArrayList educationList = null;
    private String intitals;
    private String fullname;

    private String gisNo;
    private String gisType;
    private String domicil = null;
    private String timeOfEntryGoo = null;
    private String sltBank = null;
    private String sltbranch = null;
    private String bankaccno = null;

    private String deptCode = null;
    private String curDesg = null;
    private String username = null;
    private String district = null;
    private String txtDos = null;
    private String txtwefTime = null;
    private String refid = null;
    private String hasPriv = null;
    private String ddohrmsid = null;
    private String depstatus = null;
    private String isRegular = null;
    private String isApproved = null;
    private String bankName = null;
    private String branchName = null;

    private String ifprofileCompleted = null;
    private String ifprofileVerified = null;
    private String gisName = null;
    private String maritalStatus = null;

    private String txtDeclineReason;
    private String radyear1;

    private String dos;
    private String chkUGC;
    private String pageNo = null;

    private String mismatchFound;

    private String ifmsMobile;
    private String ifmsBankAccNo;
    private String ifmsIFSCode;

    private String sltGPFAssmued;
    private String entryGovDateTime;

    private String sltPayCommission;

    private String empType;

    private String password;

    private String cell;
    private String level;
    private String quarterAddress;
    private String isReengaged;
    private String stopPayNPS;
    private String empNonPran;

    private String staffType;
    private String staffCategory;
    private String staffPlacement;
    private String cDesignation;
    private String iDesignation;
    private String pscYear;
    private String lawLevel;
    private String hidmsg;

    private String newmobile;
    private String mobileupdatedby;
    private String mobileupdatedon;
    private String poName;
    private String psName;
    private String villName;
    private String blName;
    private String distName;
    private String initialpost;
    private String payscaleon31122005;
    private String payscaleon112006;
    private String acp;
    private String rracp;
    private String payment;
    private String payfixed;
    private String fathername;
    private String qualification;
    private String placeposting;
    private String postingunitdoj;
    private String periodparticularstraining;
    private String powerofdecesion;
    private String physicalfitness;
    private String mentalfitness;
    private String honestyintegrity;
    private String remarksnominatingprofessional;
    private String serviceFrom;
    private String serviceTo;
    private String placeOFposting;
    private String property_submitted_if_any;
    private String property_submitted_on_hrms_byofficer;
    private String present_rank;
    private double pay;
    private String payAfter;
    private String promotionalpost;
    private String revisedate;

    private String fixationDni;
    private String dateofjoining;
    private String currentJoindate;
    private String currentJoinPlace;
    private String spluserCategory;
    private String gradeType =null;
    private String deptType =null;
    private String cadreType =null;
    
    private String dateOfProfileCompletion;
    private String ipOfProfileCompletion;
    private String serviceGap;
    
    public String getRadyear1() {
        return radyear1;
    }

    public void setRadyear1(String radyear1) {
        this.radyear1 = radyear1;
    }

    public String getGisName() {
        return gisName;
    }

    public void setGisName(String gisName) {
        this.gisName = gisName;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public String getDepstatus() {
        return depstatus;
    }

    public void setDepstatus(String depstatus) {
        this.depstatus = depstatus;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public String getTxtDos() {
        return txtDos;
    }

    public void setTxtDos(String txtDos) {
        this.txtDos = txtDos;
    }

    public String getTxtwefTime() {
        return txtwefTime;
    }

    public void setTxtwefTime(String txtwefTime) {
        this.txtwefTime = txtwefTime;
    }

    public String getBankaccno() {
        return bankaccno;
    }

    public void setBankaccno(String bankaccno) {
        this.bankaccno = bankaccno;
    }

    public String getDomicil() {
        return domicil;
    }

    public void setDomicil(String domicil) {
        this.domicil = domicil;
    }

    public String getTimeOfEntryGoo() {
        return timeOfEntryGoo;
    }

    public void setTimeOfEntryGoo(String timeOfEntryGoo) {
        this.timeOfEntryGoo = timeOfEntryGoo;
    }

    public String getSltBank() {
        return sltBank;
    }

    public void setSltBank(String sltBank) {
        this.sltBank = sltBank;
    }

    public String getSltbranch() {
        return sltbranch;
    }

    public void setSltbranch(String sltbranch) {
        this.sltbranch = sltbranch;
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

    public String getIntitals() {
        return intitals;
    }

    public void setIntitals(String Intitals) {
        this.intitals = Intitals;
    }

    public String getFullname() {
        return StringUtils.defaultString(this.intitals) + " " + this.fname + " " + StringUtils.defaultString(this.mname) + " " + this.lname;
    }

    public String getIfpPan() {
        return ifpPan;
    }

    public void setIfpPan(String ifpPan) {
        this.ifpPan = ifpPan;
    }

    public String getServerDate() {
        return serverDate;
    }

    public void setServerDate(String serverDate) {
        this.serverDate = serverDate;
    }

    public String getResidenceAdd() {
        return residenceAdd;
    }

    public void setResidenceAdd(String residenceAdd) {
        this.residenceAdd = residenceAdd;
    }

    public ArrayList getEducationList() {
        return educationList;
    }

    public void setEducationList(ArrayList educationList) {
        this.educationList = educationList;
    }

    public String getEmpAllotmentYear() {
        return empAllotmentYear;
    }

    public void setEmpAllotmentYear(String empAllotmentYear) {
        this.empAllotmentYear = empAllotmentYear;
    }

    public String getEmpCadre() {
        return empCadre;
    }

    public void setEmpCadre(String empCadre) {
        this.empCadre = empCadre;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }

    public String getDoeGov() {
        return doeGov;
    }

    public void setDoeGov(String doeGov) {
        this.doeGov = doeGov;
    }

    public String getEntryGovDateText() {
        return entryGovDateText;
    }

    public void setEntryGovDateText(String entryGovDateText) {
        this.entryGovDateText = entryGovDateText;
    }

    public String getDobText() {
        return dobText;
    }

    public void setDobText(String dobText) {
        this.dobText = dobText;
    }

    public String getJoinDateText() {
        return joinDateText;
    }

    public void setJoinDateText(String joinDateText) {
        this.joinDateText = joinDateText;
    }

    public String getIfReservation() {
        return ifReservation;
    }

    public void setIfReservation(String ifReservation) {
        this.ifReservation = ifReservation;
    }

    public String getIfRehabiltation() {
        return ifRehabiltation;
    }

    public void setIfRehabiltation(String ifRehabiltation) {
        this.ifRehabiltation = ifRehabiltation;
    }

    public String getCadreGrade() {
        return cadreGrade;
    }

    public void setCadreGrade(String cadreGrade) {
        this.cadreGrade = cadreGrade;
    }

    public String getAadhaarno() {
        return aadhaarno;
    }

    public void setAadhaarno(String aadhaarno) {
        this.aadhaarno = aadhaarno;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getAddlCharge() {
        return addlCharge;
    }

    public void setAddlCharge(String addlCharge) {
        this.addlCharge = addlCharge;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPermanentaddr() {
        return permanentaddr;
    }

    public void setPermanentaddr(String permanentaddr) {
        this.permanentaddr = permanentaddr;
    }

    public String getRecsource() {
        return recsource;
    }

    public void setRecsource(String recsource) {
        this.recsource = recsource;
    }

    public String getCardeallotmentyear() {
        return cardeallotmentyear;
    }

    public void setCardeallotmentyear(String cardeallotmentyear) {
        this.cardeallotmentyear = cardeallotmentyear;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
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

    public String getGpfno() {
        return gpfno;
    }

    public void setGpfno(String gpfno) {
        this.gpfno = gpfno;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getSpn() {
        return spn;
    }

    public void setSpn(String spn) {
        this.spn = spn;
    }

    public String getSpc() {
        return spc;
    }

    public void setSpc(String spc) {
        this.spc = spc;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getOfficecode() {
        return officecode;
    }

    public void setOfficecode(String officecode) {
        this.officecode = officecode;
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

    public String getMaritalStatusId() {
        return maritalStatusId;
    }

    public void setMaritalStatusId(String maritalStatusId) {
        this.maritalStatusId = maritalStatusId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getBasic() {
        return basic;
    }

    public void setBasic(int basic) {
        this.basic = basic;
    }

    public String getDor() {
        return dor;
    }

    public void setDor(String dor) {
        this.dor = dor;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDeptcode() {
        return deptcode;
    }

    public void setDeptcode(String deptcode) {
        this.deptcode = deptcode;
    }

    public String getBloodgrp() {
        return bloodgrp;
    }

    public void setBloodgrp(String bloodgrp) {
        this.bloodgrp = bloodgrp;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getPermanentdist() {
        return permanentdist;
    }

    public void setPermanentdist(String permanentdist) {
        this.permanentdist = permanentdist;
    }

    public String getPermanentps() {
        return permanentps;
    }

    public void setPermanentps(String permanentps) {
        this.permanentps = permanentps;
    }

    public String getPhyhandicapt() {
        return phyhandicapt;
    }

    public void setPhyhandicapt(String phyhandicapt) {
        this.phyhandicapt = phyhandicapt;
    }

    public String getIdmark() {
        return idmark;
    }

    public void setIdmark(String idmark) {
        this.idmark = idmark;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPrmTelNo() {
        return prmTelNo;
    }

    public void setPrmTelNo(String prmTelNo) {
        this.prmTelNo = prmTelNo;
    }

    public String getDateOfCurPosting() {
        return dateOfCurPosting;
    }

    public void setDateOfCurPosting(String dateOfCurPosting) {
        this.dateOfCurPosting = dateOfCurPosting;
    }

    public int getGp() {
        return gp;
    }

    public void setGp(int gp) {
        this.gp = gp;
    }

    public String getPayScale() {
        return payScale;
    }

    public void setPayScale(String payScale) {
        this.payScale = payScale;
    }

    public String getCadreCode() {
        return cadreCode;
    }

    public void setCadreCode(String cadreCode) {
        this.cadreCode = cadreCode;
    }

    public String getCadreId() {
        return cadreId;
    }

    public void setCadreId(String cadreId) {
        this.cadreId = cadreId;
    }

    public String getPostGrpType() {
        return postGrpType;
    }

    public void setPostGrpType(String postGrpType) {
        this.postGrpType = postGrpType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFieldOffCode() {
        return fieldOffCode;
    }

    public void setFieldOffCode(String fieldOffCode) {
        this.fieldOffCode = fieldOffCode;
    }

    public String getChest() {
        return chest;
    }

    public void setChest(String chest) {
        this.chest = chest;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getLeftvision() {
        return leftvision;
    }

    public void setLeftvision(String leftvision) {
        this.leftvision = leftvision;
    }

    public String getRightvision() {
        return rightvision;
    }

    public void setRightvision(String rightvision) {
        this.rightvision = rightvision;
    }

    public String getBrassno() {
        return brassno;
    }

    public void setBrassno(String brassno) {
        this.brassno = brassno;
    }

    public String getDomicile() {
        return domicile;
    }

    public void setDomicile(String domicile) {
        this.domicile = domicile;
    }

    public String getJoindategoo() {
        return joindategoo;
    }

    public void setJoindategoo(String joindategoo) {
        this.joindategoo = joindategoo;
    }

    public String getCurDesg() {
        return curDesg;
    }

    public void setCurDesg(String curDesg) {
        this.curDesg = curDesg;
    }

    public String getAccttype() {
        return accttype;
    }

    public void setAccttype(String accttype) {
        this.accttype = accttype;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getRefid() {
        return refid;
    }

    public void setRefid(String refid) {
        this.refid = refid;
    }

    public String getEpic() {
        return epic;
    }

    public void setEpic(String epic) {
        this.epic = epic;
    }

    public String getHasPriv() {
        return hasPriv;
    }

    public void setHasPriv(String hasPriv) {
        this.hasPriv = hasPriv;
    }

    public String getDdohrmsid() {
        return ddohrmsid;
    }

    public void setDdohrmsid(String ddohrmsid) {
        this.ddohrmsid = ddohrmsid;
    }

    public String getIsRegular() {
        return isRegular;
    }

    public void setIsRegular(String isRegular) {
        this.isRegular = isRegular;
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

    public String getTxtDeclineReason() {
        return txtDeclineReason;
    }

    public void setTxtDeclineReason(String txtDeclineReason) {
        this.txtDeclineReason = txtDeclineReason;
    }

    public String getReligionName() {
        return religionName;
    }

    public void setReligionName(String religionName) {
        this.religionName = religionName;
    }

    public String getPanno() {
        return panno;
    }

    public void setPanno(String panno) {
        this.panno = panno;
    }

    public String getDos() {
        return dos;
    }

    public void setDos(String dos) {
        this.dos = dos;
    }

    public String getChkUGC() {
        return chkUGC;
    }

    public void setChkUGC(String chkUGC) {
        this.chkUGC = chkUGC;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getMismatchFound() {
        return mismatchFound;
    }

    public void setMismatchFound(String mismatchFound) {
        this.mismatchFound = mismatchFound;
    }

    public String getIfmsMobile() {
        return ifmsMobile;
    }

    public void setIfmsMobile(String ifmsMobile) {
        this.ifmsMobile = ifmsMobile;
    }

    public String getIfmsBankAccNo() {
        return ifmsBankAccNo;
    }

    public void setIfmsBankAccNo(String ifmsBankAccNo) {
        this.ifmsBankAccNo = ifmsBankAccNo;
    }

    public String getIfmsIFSCode() {
        return ifmsIFSCode;
    }

    public void setIfmsIFSCode(String ifmsIFSCode) {
        this.ifmsIFSCode = ifmsIFSCode;
    }

    public String getSltGPFAssmued() {
        return sltGPFAssmued;
    }

    public void setSltGPFAssmued(String sltGPFAssmued) {
        this.sltGPFAssmued = sltGPFAssmued;
    }

    public String getEntryGovDateTime() {
        return entryGovDateTime;
    }

    public void setEntryGovDateTime(String entryGovDateTime) {
        this.entryGovDateTime = entryGovDateTime;
    }

    public String getSltPayCommission() {
        return sltPayCommission;
    }

    public void setSltPayCommission(String sltPayCommission) {
        this.sltPayCommission = sltPayCommission;
    }

    public String getEmpType() {
        return empType;
    }

    public void setEmpType(String empType) {
        this.empType = empType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getQuarterAddress() {
        return quarterAddress;
    }

    public void setQuarterAddress(String quarterAddress) {
        this.quarterAddress = quarterAddress;
    }

    public String getIsReengaged() {
        return isReengaged;
    }

    public void setIsReengaged(String isReengaged) {
        this.isReengaged = isReengaged;
    }

    public String getStopPayNPS() {
        return stopPayNPS;
    }

    public void setStopPayNPS(String stopPayNPS) {
        this.stopPayNPS = stopPayNPS;
    }

    public String getEmpNonPran() {
        return empNonPran;
    }

    public void setEmpNonPran(String empNonPran) {
        this.empNonPran = empNonPran;
    }

    public String getStaffType() {
        return staffType;
    }

    public void setStaffType(String staffType) {
        this.staffType = staffType;
    }

    public String getStaffCategory() {
        return staffCategory;
    }

    public void setStaffCategory(String staffCategory) {
        this.staffCategory = staffCategory;
    }

    public String getStaffPlacement() {
        return staffPlacement;
    }

    public void setStaffPlacement(String staffPlacement) {
        this.staffPlacement = staffPlacement;
    }

    public String getcDesignation() {
        return cDesignation;
    }

    public void setcDesignation(String cDesignation) {
        this.cDesignation = cDesignation;
    }

    public String getiDesignation() {
        return iDesignation;
    }

    public void setiDesignation(String iDesignation) {
        this.iDesignation = iDesignation;
    }

    public String getPscYear() {
        return pscYear;
    }

    public void setPscYear(String pscYear) {
        this.pscYear = pscYear;
    }

    public String getLawLevel() {
        return lawLevel;
    }

    public void setLawLevel(String lawLevel) {
        this.lawLevel = lawLevel;
    }

    public String getFullname1() {
        return fullname;
    }

    public void setFullname1(String fullname) {
        this.fullname = fullname;
    }

    public String getDrivingLicence() {
        return drivingLicence;
    }

    public void setDrivingLicence(String drivingLicence) {
        this.drivingLicence = drivingLicence;
    }

    public String getPresentaddr() {
        return presentaddr;
    }

    public void setPresentaddr(String presentaddr) {
        this.presentaddr = presentaddr;
    }

    public String getHidmsg() {
        return hidmsg;
    }

    public void setHidmsg(String hidmsg) {
        this.hidmsg = hidmsg;
    }

    public String getNewmobile() {
        return newmobile;
    }

    public void setNewmobile(String newmobile) {
        this.newmobile = newmobile;
    }

    public String getMobileupdatedby() {
        return mobileupdatedby;
    }

    public void setMobileupdatedby(String mobileupdatedby) {
        this.mobileupdatedby = mobileupdatedby;
    }

    public String getMobileupdatedon() {
        return mobileupdatedon;
    }

    public void setMobileupdatedon(String mobileupdatedon) {
        this.mobileupdatedon = mobileupdatedon;
    }

    public String getPoName() {
        return poName;
    }

    public void setPoName(String poName) {
        this.poName = poName;
    }

    public String getPsName() {
        return psName;
    }

    public void setPsName(String psName) {
        this.psName = psName;
    }

    public String getVillName() {
        return villName;
    }

    public void setVillName(String villName) {
        this.villName = villName;
    }

    public String getBlName() {
        return blName;
    }

    public void setBlName(String blName) {
        this.blName = blName;
    }

    public String getDistName() {
        return distName;
    }

    public void setDistName(String distName) {
        this.distName = distName;
    }

    public String getInitialpost() {
        return initialpost;
    }

    public void setInitialpost(String initialpost) {
        this.initialpost = initialpost;
    }

    public String getPayscaleon31122005() {
        return payscaleon31122005;
    }

    public void setPayscaleon31122005(String payscaleon31122005) {
        this.payscaleon31122005 = payscaleon31122005;
    }

    public String getPayscaleon112006() {
        return payscaleon112006;
    }

    public void setPayscaleon112006(String payscaleon112006) {
        this.payscaleon112006 = payscaleon112006;
    }

    public String getAcp() {
        return acp;
    }

    public void setAcp(String acp) {
        this.acp = acp;
    }

    public String getRracp() {
        return rracp;
    }

    public void setRracp(String rracp) {
        this.rracp = rracp;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getPayfixed() {
        return payfixed;
    }

    public void setPayfixed(String payfixed) {
        this.payfixed = payfixed;
    }

    public String getFathername() {
        return fathername;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getPlaceposting() {
        return placeposting;
    }

    public void setPlaceposting(String placeposting) {
        this.placeposting = placeposting;
    }

    public String getPostingunitdoj() {
        return postingunitdoj;
    }

    public void setPostingunitdoj(String postingunitdoj) {
        this.postingunitdoj = postingunitdoj;
    }

    public String getPeriodparticularstraining() {
        return periodparticularstraining;
    }

    public void setPeriodparticularstraining(String periodparticularstraining) {
        this.periodparticularstraining = periodparticularstraining;
    }

    public String getPowerofdecesion() {
        return powerofdecesion;
    }

    public void setPowerofdecesion(String powerofdecesion) {
        this.powerofdecesion = powerofdecesion;
    }

    public String getPhysicalfitness() {
        return physicalfitness;
    }

    public void setPhysicalfitness(String physicalfitness) {
        this.physicalfitness = physicalfitness;
    }

    public String getMentalfitness() {
        return mentalfitness;
    }

    public void setMentalfitness(String mentalfitness) {
        this.mentalfitness = mentalfitness;
    }

    public String getHonestyintegrity() {
        return honestyintegrity;
    }

    public void setHonestyintegrity(String honestyintegrity) {
        this.honestyintegrity = honestyintegrity;
    }

    public String getRemarksnominatingprofessional() {
        return remarksnominatingprofessional;
    }

    public void setRemarksnominatingprofessional(String remarksnominatingprofessional) {
        this.remarksnominatingprofessional = remarksnominatingprofessional;
    }

    public String getServiceFrom() {
        return serviceFrom;
    }

    public void setServiceFrom(String serviceFrom) {
        this.serviceFrom = serviceFrom;
    }

    public String getServiceTo() {
        return serviceTo;
    }

    public void setServiceTo(String serviceTo) {
        this.serviceTo = serviceTo;
    }

    public String getPlaceOFposting() {
        return placeOFposting;
    }

    public void setPlaceOFposting(String placeOFposting) {
        this.placeOFposting = placeOFposting;
    }

    public String getProperty_submitted_if_any() {
        return property_submitted_if_any;
    }

    public void setProperty_submitted_if_any(String property_submitted_if_any) {
        this.property_submitted_if_any = property_submitted_if_any;
    }

    public String getProperty_submitted_on_hrms_byofficer() {
        return property_submitted_on_hrms_byofficer;
    }

    public void setProperty_submitted_on_hrms_byofficer(String property_submitted_on_hrms_byofficer) {
        this.property_submitted_on_hrms_byofficer = property_submitted_on_hrms_byofficer;
    }

    public String getPresent_rank() {
        return present_rank;
    }

    public void setPresent_rank(String present_rank) {
        this.present_rank = present_rank;
    }

    public double getPay() {
        return pay;
    }

    public void setPay(double pay) {
        this.pay = pay;
    }

    public String getPayAfter() {
        return payAfter;
    }

    public void setPayAfter(String payAfter) {
        this.payAfter = payAfter;
    }

    public String getPromotionalpost() {
        return promotionalpost;
    }

    public void setPromotionalpost(String promotionalpost) {
        this.promotionalpost = promotionalpost;
    }

    public String getRevisedate() {
        return revisedate;
    }

    public void setRevisedate(String revisedate) {
        this.revisedate = revisedate;
    }

    public String getFixationDni() {
        return fixationDni;
    }

    public void setFixationDni(String fixationDni) {
        this.fixationDni = fixationDni;
    }

    public String getDateofjoining() {
        return dateofjoining;
    }

    public void setDateofjoining(String dateofjoining) {
        this.dateofjoining = dateofjoining;
    }

    public String getCurrentJoindate() {
        return currentJoindate;
    }

    public void setCurrentJoindate(String currentJoindate) {
        this.currentJoindate = currentJoindate;
    }

    public String getCurrentJoinPlace() {
        return currentJoinPlace;
    }

    public void setCurrentJoinPlace(String currentJoinPlace) {
        this.currentJoinPlace = currentJoinPlace;
    }

    public String getSpluserCategory() {
        return spluserCategory;
    }

    public void setSpluserCategory(String spluserCategory) {
        this.spluserCategory = spluserCategory;
    }

    public String getGradeType() {
        return gradeType;
    }

    public void setGradeType(String gradeType) {
        this.gradeType = gradeType;
    }

    public String getDeptType() {
        return deptType;
    }

    public void setDeptType(String deptType) {
        this.deptType = deptType;
    }

    public String getCadreType() {
        return cadreType;
    }

    public void setCadreType(String cadreType) {
        this.cadreType = cadreType;
    }

    public String getDateOfProfileCompletion() {
        return dateOfProfileCompletion;
    }

    public void setDateOfProfileCompletion(String dateOfProfileCompletion) {
        this.dateOfProfileCompletion = dateOfProfileCompletion;
    }

    public String getIpOfProfileCompletion() {
        return ipOfProfileCompletion;
    }

    public void setIpOfProfileCompletion(String ipOfProfileCompletion) {
        this.ipOfProfileCompletion = ipOfProfileCompletion;
    }

    public String getServiceGap() {
        return serviceGap;
    }

    public void setServiceGap(String serviceGap) {
        this.serviceGap = serviceGap;
    }

}
