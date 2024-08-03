/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.employee;
 
import java.util.List;

/**
 *
 * @author lenovo
 */
public class PensionFamilyList {

    private String hrmsId;
    //private int slno = 0;
    private String salutationFamily = null;
    private String dependentName = null;
    private String relationshipId;
    private String sex = null;
    private String familyDob = null;
    private String mobileNo = null;
    private String intMaritalStatusTypeId = "";
    private List familyList;
    private String ifscCode;
    private String nomineeAddress;
    private String bankAccountNo;
    private String bankBranchName;
    private String familySharePercentage;
    private String minorFlag;
    private String familyRemarks;
    private String gurdianName;
    private String tiApplicableFlag;

    private String familyHandicappedFlag;
    private String familyHandicappedTypeId;
    private String isGuardian;
    private String isNominee;
    private String relation_type;
    private String salutationgurdian;
    private String salutationFamilyGuardian;
    private String dod;
    private String priority;
    private String cvp;
    private String cvpPercentage;
    private String guardianfname;
    private String guardianmname;
    private String guardianlname;
    private String penBenefitTypeId;
    private String nomineeOriginalOrAlterId;
    private String familyAddress;

    public String getDod() {
        return dod;
    }

    public void setDod(String dod) {
        this.dod = dod;
    }

    public String getSalutationgurdian() {
        return salutationgurdian;
    }

    public void setSalutationgurdian(String salutationgurdian) {
        this.salutationgurdian = salutationgurdian;
    }

    public String getNomineeAddress() {
        return nomineeAddress;
    }

    public void setNomineeAddress(String nomineeAddress) {
        this.nomineeAddress = nomineeAddress;
    }

    public String getHrmsId() {
        return hrmsId;
    }

    public void setHrmsId(String hrmsId) {
        this.hrmsId = hrmsId;
    }

    public String getIsGuardian() {
        return isGuardian;
    }

    public void setIsGuardian(String isGuardian) {
        this.isGuardian = isGuardian;
    }

    public String getIsNominee() {
        return isNominee;
    }

    public void setIsNominee(String isNominee) {
        this.isNominee = isNominee;
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

    public String getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(String relationshipId) {
        this.relationshipId = relationshipId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFamilyDob() {
        return familyDob;
    }

    public void setFamilyDob(String familyDob) {
        this.familyDob = familyDob;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getIntMaritalStatusTypeId() {
        return intMaritalStatusTypeId;
    }

    public void setIntMaritalStatusTypeId(String intMaritalStatusTypeId) {
        this.intMaritalStatusTypeId = intMaritalStatusTypeId;
    }

    public List getFamilyList() {
        return familyList;
    }

    public void setFamilyList(List familyList) {
        this.familyList = familyList;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }

    public String getFamilySharePercentage() {
        return familySharePercentage;
    }

    public void setFamilySharePercentage(String familySharePercentage) {
        this.familySharePercentage = familySharePercentage;
    }

    public String getMinorFlag() {
        return minorFlag;
    }

    public void setMinorFlag(String minorFlag) {
        this.minorFlag = minorFlag;
    }

    public String getFamilyRemarks() {
        return familyRemarks;
    }

    public void setFamilyRemarks(String familyRemarks) {
        this.familyRemarks = familyRemarks;
    }

    public String getGurdianName() {
        return gurdianName;
    }

    public void setGurdianName(String gurdianName) {
        this.gurdianName = gurdianName;
    }

    public String getTiApplicableFlag() {
        return tiApplicableFlag;
    }

    public void setTiApplicableFlag(String tiApplicableFlag) {
        this.tiApplicableFlag = tiApplicableFlag;
    }

    public String getFamilyHandicappedFlag() {
        return familyHandicappedFlag;
    }

    public void setFamilyHandicappedFlag(String familyHandicappedFlag) {
        this.familyHandicappedFlag = familyHandicappedFlag;
    }

    public String getFamilyHandicappedTypeId() {
        return familyHandicappedTypeId;
    }

    public void setFamilyHandicappedTypeId(String familyHandicappedTypeId) {
        this.familyHandicappedTypeId = familyHandicappedTypeId;
    }

    public String getRelation_type() {
        return relation_type;
    }

    public void setRelation_type(String relation_type) {
        this.relation_type = relation_type;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
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

    public String getGuardianfname() {
        return guardianfname;
    }

    public void setGuardianfname(String guardianfname) {
        this.guardianfname = guardianfname;
    }

    public String getGuardianmname() {
        return guardianmname;
    }

    public void setGuardianmname(String guardianmname) {
        this.guardianmname = guardianmname;
    }

    public String getGuardianlname() {
        return guardianlname;
    }

    public void setGuardianlname(String guardianlname) {
        this.guardianlname = guardianlname;
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

    public String getFamilyAddress() {
        return familyAddress;
    }

    public void setFamilyAddress(String familyAddress) {
        this.familyAddress = familyAddress;
    }

    public String getSalutationFamilyGuardian() {
        return salutationFamilyGuardian;
    }

    public void setSalutationFamilyGuardian(String salutationFamilyGuardian) {
        this.salutationFamilyGuardian = salutationFamilyGuardian;
    }
    
    
    
    
}
