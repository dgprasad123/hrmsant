/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.employee;
 
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

/**
 *
 * @author lenovo
 */
public class PensionProfile {

    private String tpfSeries = null;
    private String tpfAcNo = null;
    private String hrmsEmpId = null;
    private String salutationEmp = null;
    private String employeeFirstName = null;
    private String employeeMiddleName = null;
    private String employeeLastName = null;
    private String salutation = null;
    private String guardianFName = null;
    private String guardianMName = null;
    private String guardianLName = null;
    private String guardianRelId = null;
    private String dob = null;
    private String retirementDate = null;
    private String sex = null;
    private String penCategoryId = null;
    private String penTypeId = null;
    private String intMaritalStatusTypeId = null;
    private String religionId = null;
    private String nationalityId = null;
    private String mobileNo = null;
    private String penIdnMark = null;
    private String penIdnMark2 = null;
    private String mailId = null;
    private String panNo = null;
    private List pensionfamilylist = null;
    private List pensionnomineelist = null;
    private List GuardianDetails = null;
    private String identityDocId = null;
    private String identityDocNumber = null;
    private String designationId = null;
    private String cvp = null;
    private String cvpPercentage = null;
    private String finalGpfAppliedFlag = null;
    private String finalGpfAppliedDate = null;
    private String finalGpfAppliedLetterNo = null;
    private String height = null;
    private String ifscCode = null;
    private String bankAcctNo = "";
    private String bankBranch = null;
    private String bsrCode = null;
    private String treasuryCode = null;
    private String ddoName = null;
    private String districtCode = null;
    private String perAddpin = null;
    private String perAddstateId = null;
    private String perAddcity = null;
    private String perAddtown = null;
    private String perAddpoliceStation = null;
    private String commAddpin = null;
    private String commAddstateId = null;
    private String commAddtown = null;
    private String commAddpoliceStation = null;
    private String commAddcity = null;
    private String cDistrictCode = null;
    private String prevPenPia = null;
    private String prevPPOOrFPPONo = null;
    private String prevPensionEfffromDate = null;
    private String prevPenSource = null;
    private String prevPenType = null;
    private String prevPenPayTresId = null;
    private String prevPensionerId = null;
    private String prevPenBankBranch = null;
    private String prevPenBankIfscCd = null;
    private String prevPenAmt;
    private String prevPenPayTresCode = null;
    private String retirementType = null;

    private String penBenefitTypeId = null;
    private String nomineeOriginalOrAlterId = null;
    private String salutationNominee = null;
    private String nomineeName = null;
    private String nomineeSex = null;
    private String nomineeDob = null;
    private String nomineeMobileNo = null;
    private String nomineeAddress = null;

    private String salutationFamily = null;
    private String dependentName = null;
    private String FamilySex = null;
    private String familyDob = null;
    private String FamilyMobileNo = null;
    private String commDistrictCode = null;
    private String nomineeRelation;

    private String checkPenProfileStauts;
    private String retBenifitType;
    private String nomineeIfscCode;

    private String ddoId;
    private String intTreasuryId;

    private String gpfNo;
    private String IfpensionCompleted;

    private String deptCode;

    private String departmentName;

    private String offName;
    private String offAddress;

    private String nomineeType;

    private String prioritylable;

    private String nomineeBankAcc;

    private String dod;

    private String reason;
    private String status;
    private String guardianName;
    private String postName;
    private String postCode;
    private List availablePosts;
    private String msg;
    private String commdist;
    private String gpftype;
    private String lastDistrictServe;
    private String retirementBenefitType;
    private String userType;

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDod() {
        return dod;
    }

    public void setDod(String dod) {
        this.dod = dod;
    }

    public String getNomineeBankAcc() {
        return nomineeBankAcc;
    }

    public void setNomineeBankAcc(String nomineeBankAcc) {
        this.nomineeBankAcc = nomineeBankAcc;
    }

    public String getNomineeIfscCode() {
        return nomineeIfscCode;
    }

    public void setNomineeIfscCode(String nomineeIfscCode) {
        this.nomineeIfscCode = nomineeIfscCode;
    }

    public String getPrioritylable() {
        return prioritylable;
    }

    public void setPrioritylable(String prioritylable) {
        this.prioritylable = prioritylable;
    }

    public String getNomineeRelation() {
        return nomineeRelation;
    }

    public void setNomineeRelation(String nomineeRelation) {
        this.nomineeRelation = nomineeRelation;
    }

    public String getNomineeType() {
        return nomineeType;
    }

    public void setNomineeType(String nomineeType) {
        this.nomineeType = nomineeType;
    }

    public String getRetBenifitType() {
        return retBenifitType;
    }

    public void setRetBenifitType(String retBenifitType) {
        this.retBenifitType = retBenifitType;
    }

    public String getOffName() {
        return offName;
    }

    public void setOffName(String offName) {
        this.offName = offName;
    }

    public String getOffAddress() {
        return offAddress;
    }

    public void setOffAddress(String offAddress) {
        this.offAddress = offAddress;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getCheckPenProfileStauts() {
        return checkPenProfileStauts;
    }

    public void setCheckPenProfileStauts(String checkPenProfileStauts) {
        this.checkPenProfileStauts = checkPenProfileStauts;
    }

    public String getTpfSeries() {
        return tpfSeries;
    }

    public void setTpfSeries(String tpfSeries) {
        this.tpfSeries = tpfSeries;
    }

    public String getTpfAcNo() {
        return tpfAcNo;
    }

    public void setTpfAcNo(String tpfAcNo) {
        this.tpfAcNo = tpfAcNo;
    }

    public String getHrmsEmpId() {
        return hrmsEmpId;
    }

    public void setHrmsEmpId(String hrmsEmpId) {
        this.hrmsEmpId = hrmsEmpId;
    }

    public String getSalutationEmp() {
        return salutationEmp;
    }

    public void setSalutationEmp(String salutationEmp) {
        this.salutationEmp = salutationEmp;
    }

    public String getEmployeeFirstName() {
        return employeeFirstName;
    }

    public void setEmployeeFirstName(String employeeFirstName) {
        this.employeeFirstName = employeeFirstName;
    }

    public String getEmployeeMiddleName() {
        return employeeMiddleName;
    }

    public void setEmployeeMiddleName(String employeeMiddleName) {
        this.employeeMiddleName = employeeMiddleName;
    }

    public String getEmployeeLastName() {
        return employeeLastName;
    }

    public void setEmployeeLastName(String employeeLastName) {
        this.employeeLastName = employeeLastName;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getGuardianFName() {
        return guardianFName;
    }

    public void setGuardianFName(String guardianFName) {
        this.guardianFName = guardianFName;
    }

    public String getGuardianMName() {
        return guardianMName;
    }

    public void setGuardianMName(String guardianMName) {
        this.guardianMName = guardianMName;
    }

    public String getGuardianLName() {
        return guardianLName;
    }

    public void setGuardianLName(String guardianLName) {
        this.guardianLName = guardianLName;
    }

    public String getGuardianRelId() {
        return guardianRelId;
    }

    public void setGuardianRelId(String guardianRelId) {
        this.guardianRelId = guardianRelId;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRetirementDate() {
        return retirementDate;
    }

    public void setRetirementDate(String retirementDate) {
        this.retirementDate = retirementDate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPenCategoryId() {
        return penCategoryId;
    }

    public void setPenCategoryId(String penCategoryId) {
        this.penCategoryId = penCategoryId;
    }

    public String getPenTypeId() {
        return penTypeId;
    }

    public void setPenTypeId(String penTypeId) {
        this.penTypeId = penTypeId;
    }

    public String getIntMaritalStatusTypeId() {
        return intMaritalStatusTypeId;
    }

    public void setIntMaritalStatusTypeId(String intMaritalStatusTypeId) {
        this.intMaritalStatusTypeId = intMaritalStatusTypeId;
    }

    public String getReligionId() {
        return religionId;
    }

    public void setReligionId(String religionId) {
        this.religionId = religionId;
    }

    public String getNationalityId() {
        return nationalityId;
    }

    public void setNationalityId(String nationalityId) {
        this.nationalityId = nationalityId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPenIdnMark() {
        return penIdnMark;
    }

    public void setPenIdnMark(String penIdnMark) {
        this.penIdnMark = penIdnMark;
    }

    public String getPenIdnMark2() {
        return penIdnMark2;
    }

    public void setPenIdnMark2(String penIdnMark2) {
        this.penIdnMark2 = penIdnMark2;
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getIdentityDocId() {
        return identityDocId;
    }

    public void setIdentityDocId(String identityDocId) {
        this.identityDocId = identityDocId;
    }

    public String getIdentityDocNumber() {
        return identityDocNumber;
    }

    public void setIdentityDocNumber(String identityDocNumber) {
        this.identityDocNumber = identityDocNumber;
    }

    public String getDesignationId() {
        return designationId;
    }

    public void setDesignationId(String designationId) {
        this.designationId = designationId;
    }

    public String getCvp() {
        return cvp;
    }

    public void setCvp(String cvp) {
        this.cvp = cvp;
    }

    public String getCvpPercentage() {
        return cvpPercentage;
    }

    public void setCvpPercentage(String cvpPercentage) {
        this.cvpPercentage = cvpPercentage;
    }

    public String getFinalGpfAppliedFlag() {
        return finalGpfAppliedFlag;
    }

    public void setFinalGpfAppliedFlag(String finalGpfAppliedFlag) {
        this.finalGpfAppliedFlag = finalGpfAppliedFlag;
    }

    public String getFinalGpfAppliedDate() {
        return finalGpfAppliedDate;
    }

    public void setFinalGpfAppliedDate(String finalGpfAppliedDate) {
        this.finalGpfAppliedDate = finalGpfAppliedDate;
    }

    public String getFinalGpfAppliedLetterNo() {
        return finalGpfAppliedLetterNo;
    }

    public void setFinalGpfAppliedLetterNo(String finalGpfAppliedLetterNo) {
        this.finalGpfAppliedLetterNo = finalGpfAppliedLetterNo;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getBankAcctNo() {
        return bankAcctNo;
    }

    public void setBankAcctNo(String bankAcctNo) {
        this.bankAcctNo = bankAcctNo;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getBsrCode() {
        return bsrCode;
    }

    public void setBsrCode(String bsrCode) {
        this.bsrCode = bsrCode;
    }

    public String getTreasuryCode() {
        return treasuryCode;
    }

    public void setTreasuryCode(String treasuryCode) {
        this.treasuryCode = treasuryCode;
    }

    public String getDdoName() {
        return ddoName;
    }

    public void setDdoName(String ddoName) {
        this.ddoName = ddoName;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getPerAddpin() {
        return perAddpin;
    }

    public void setPerAddpin(String perAddpin) {
        this.perAddpin = perAddpin;
    }

    public String getPerAddstateId() {
        return perAddstateId;
    }

    public void setPerAddstateId(String perAddstateId) {
        this.perAddstateId = perAddstateId;
    }

    public String getPerAddcity() {
        return perAddcity;
    }

    public void setPerAddcity(String perAddcity) {
        this.perAddcity = perAddcity;
    }

    public String getPerAddtown() {
        return perAddtown;
    }

    public void setPerAddtown(String perAddtown) {
        this.perAddtown = perAddtown;
    }

    public String getPerAddpoliceStation() {
        return perAddpoliceStation;
    }

    public void setPerAddpoliceStation(String perAddpoliceStation) {
        this.perAddpoliceStation = perAddpoliceStation;
    }

    public String getCommAddpin() {
        return commAddpin;
    }

    public void setCommAddpin(String commAddpin) {
        this.commAddpin = commAddpin;
    }

    public String getCommAddstateId() {
        return commAddstateId;
    }

    public void setCommAddstateId(String commAddstateId) {
        this.commAddstateId = commAddstateId;
    }

    public String getCommAddtown() {
        return commAddtown;
    }

    public void setCommAddtown(String commAddtown) {
        this.commAddtown = commAddtown;
    }

    public String getCommAddpoliceStation() {
        return commAddpoliceStation;
    }

    public void setCommAddpoliceStation(String commAddpoliceStation) {
        this.commAddpoliceStation = commAddpoliceStation;
    }

    public String getCommAddcity() {
        return commAddcity;
    }

    public void setCommAddcity(String commAddcity) {
        this.commAddcity = commAddcity;
    }

    public String getcDistrictCode() {
        return cDistrictCode;
    }

    public void setcDistrictCode(String cDistrictCode) {
        this.cDistrictCode = cDistrictCode;
    }

    public String getPrevPenPia() {
        return prevPenPia;
    }

    public void setPrevPenPia(String prevPenPia) {
        this.prevPenPia = prevPenPia;
    }

    public String getPrevPPOOrFPPONo() {
        return prevPPOOrFPPONo;
    }

    public void setPrevPPOOrFPPONo(String prevPPOOrFPPONo) {
        this.prevPPOOrFPPONo = prevPPOOrFPPONo;
    }

    public String getPrevPensionEfffromDate() {
        return prevPensionEfffromDate;
    }

    public void setPrevPensionEfffromDate(String prevPensionEfffromDate) {
        this.prevPensionEfffromDate = prevPensionEfffromDate;
    }

    public String getPrevPenSource() {
        return prevPenSource;
    }

    public void setPrevPenSource(String prevPenSource) {
        this.prevPenSource = prevPenSource;
    }

    public String getPrevPenType() {
        return prevPenType;
    }

    public void setPrevPenType(String prevPenType) {
        this.prevPenType = prevPenType;
    }

    public String getPrevPenPayTresId() {
        return prevPenPayTresId;
    }

    public void setPrevPenPayTresId(String prevPenPayTresId) {
        this.prevPenPayTresId = prevPenPayTresId;
    }

    public String getPrevPensionerId() {
        return prevPensionerId;
    }

    public void setPrevPensionerId(String prevPensionerId) {
        this.prevPensionerId = prevPensionerId;
    }

    public String getPrevPenBankBranch() {
        return prevPenBankBranch;
    }

    public void setPrevPenBankBranch(String prevPenBankBranch) {
        this.prevPenBankBranch = prevPenBankBranch;
    }

    public String getPrevPenBankIfscCd() {
        return prevPenBankIfscCd;
    }

    public void setPrevPenBankIfscCd(String prevPenBankIfscCd) {
        this.prevPenBankIfscCd = prevPenBankIfscCd;
    }

    public String getPrevPenAmt() {
        return prevPenAmt;
    }

    public void setPrevPenAmt(String prevPenAmt) {
        this.prevPenAmt = prevPenAmt;
    }

    public String getPrevPenPayTresCode() {
        return prevPenPayTresCode;
    }

    public void setPrevPenPayTresCode(String prevPenPayTresCode) {
        this.prevPenPayTresCode = prevPenPayTresCode;
    }

    public String getPenBenefitTypeId() {
        return penBenefitTypeId;
    }

    public void setPenBenefitTypeId(String penBenefitTypeId) {
        this.penBenefitTypeId = penBenefitTypeId;
    }

    public String getNomineeOriginalOrAlterId() {
        return nomineeOriginalOrAlterId;
    }

    public void setNomineeOriginalOrAlterId(String nomineeOriginalOrAlterId) {
        this.nomineeOriginalOrAlterId = nomineeOriginalOrAlterId;
    }

    public String getSalutationNominee() {
        return salutationNominee;
    }

    public void setSalutationNominee(String salutationNominee) {
        this.salutationNominee = salutationNominee;
    }

    public String getNomineeName() {
        return nomineeName;
    }

    public void setNomineeName(String nomineeName) {
        this.nomineeName = nomineeName;
    }

    public String getNomineeSex() {
        return nomineeSex;
    }

    public void setNomineeSex(String nomineeSex) {
        this.nomineeSex = nomineeSex;
    }

    public String getNomineeDob() {
        return nomineeDob;
    }

    public void setNomineeDob(String nomineeDob) {
        this.nomineeDob = nomineeDob;
    }

    public String getNomineeMobileNo() {
        return nomineeMobileNo;
    }

    public void setNomineeMobileNo(String nomineeMobileNo) {
        this.nomineeMobileNo = nomineeMobileNo;
    }

    public String getNomineeAddress() {
        return nomineeAddress;
    }

    public void setNomineeAddress(String nomineeAddress) {
        this.nomineeAddress = nomineeAddress;
    }

    public String getSalutationFamily() {
        return salutationFamily;
    }

    public void setSalutationFamily(String salutationFamily) {
        this.salutationFamily = salutationFamily;
    }

    public String getDependentName() {
        return dependentName;
    }

    public void setDependentName(String dependentName) {
        this.dependentName = dependentName;
    }

    public String getFamilySex() {
        return FamilySex;
    }

    public void setFamilySex(String FamilySex) {
        this.FamilySex = FamilySex;
    }

    public String getFamilyDob() {
        return familyDob;
    }

    public void setFamilyDob(String familyDob) {
        this.familyDob = familyDob;
    }

    public String getFamilyMobileNo() {
        return FamilyMobileNo;
    }

    public void setFamilyMobileNo(String FamilyMobileNo) {
        this.FamilyMobileNo = FamilyMobileNo;
    }

    public List getPensionfamilylist() {
        return pensionfamilylist;
    }

    public void setPensionfamilylist(List pensionfamilylist) {
        this.pensionfamilylist = pensionfamilylist;
    }

    public List getPensionnomineelist() {
        return pensionnomineelist;
    }

    public void setPensionnomineelist(List pensionnomineelist) {
        this.pensionnomineelist = pensionnomineelist;
    }

    public String getDdoId() {
        return ddoId;
    }

    public void setDdoId(String ddoId) {
        this.ddoId = ddoId;
    }

    public String getIntTreasuryId() {
        return intTreasuryId;
    }

    public void setIntTreasuryId(String intTreasuryId) {
        this.intTreasuryId = intTreasuryId;
    }

    public String getCommDistrictCode() {
        return commDistrictCode;
    }

    public void setCommDistrictCode(String commDistrictCode) {
        this.commDistrictCode = commDistrictCode;
    }

    public String getRetirementType() {
        return retirementType;
    }

    public void setRetirementType(String retirementType) {
        this.retirementType = retirementType;
    }

    public String getGpfNo() {
        return gpfNo;
    }

    public void setGpfNo(String gpfNo) {
        this.gpfNo = gpfNo;
    }

    public String getIfpensionCompleted() {
        return IfpensionCompleted;
    }

    public void setIfpensionCompleted(String IfpensionCompleted) {
        this.IfpensionCompleted = IfpensionCompleted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public List getAvailablePosts() {
        return availablePosts;
    }

    public void setAvailablePosts(List availablePosts) {
        this.availablePosts = availablePosts;
    }

    public List getGuardianDetails() {
        return GuardianDetails;
    }

    public void setGuardianDetails(List GuardianDetails) {
        this.GuardianDetails = GuardianDetails;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCommdist() {
        return commdist;
    }

    public void setCommdist(String commdist) {
        this.commdist = commdist;
    }

    public String getGpftype() {
        return gpftype;
    }

    public void setGpftype(String gpftype) {
        this.gpftype = gpftype;
    }

    public String getLastDistrictServe() {
        return lastDistrictServe;
    }

    public void setLastDistrictServe(String lastDistrictServe) {
        this.lastDistrictServe = lastDistrictServe;
    }

    public String getRetirementBenefitType() {
        return retirementBenefitType;
    }

    public void setRetirementBenefitType(String retirementBenefitType) {
        this.retirementBenefitType = retirementBenefitType;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    
}
