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
public class PensionNomineeList {

    private int serialNo;
    private String penBenefitTypeId = null;
    private String nomineeOriginalOrAlterId = null;
    private String salutationNominee = null;
    private String nomineeName = null;
    private String relationId;
    private String relation;

    private String sex = null;
    private String nomineeMaritalStatusId;
    private String dob = null;
    private String mobileNo = null;
    private String nomineeAddress = null;
    private String priorityLevel;
    //private int sharePercentage;
    private String sharePercentage;
    private String familySex;
    private String bankAccountNo = null;
    private String ifscCode;
    private String bankBranchName;
    private String minorFlag;

    private String gurdianName;
    private String nomineeType;

    private List nomineeList;

    private String salutationGaurdian;
    private String salutationNomineeGaurdian;

    private String gaurdianDetails;
    private String familyHandicappedFlag;
    private String familyHandicappedTypeId;
    private String cvp;
    private String cvpPercentage;
    private String retirementBenefitType;
    private String nomineeTyp;
    private String remarks;

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getGaurdianDetails() {
        return gaurdianDetails;
    }

    public void setGaurdianDetails(String gaurdianDetails) {
        this.gaurdianDetails = gaurdianDetails;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNomineeType() {
        return nomineeType;
    }

    public void setNomineeType(String nomineeType) {
        this.nomineeType = nomineeType;
    }

    public String getNomineeMaritalStatusId() {
        return nomineeMaritalStatusId;
    }

    public void setNomineeMaritalStatusId(String nomineeMaritalStatusId) {
        this.nomineeMaritalStatusId = nomineeMaritalStatusId;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getNomineeAddress() {
        return nomineeAddress;
    }

    public void setNomineeAddress(String nomineeAddress) {
        this.nomineeAddress = nomineeAddress;
    }

    public String getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(String priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public String getSharePercentage() {
        return sharePercentage;
    }

    public void setSharePercentage(String sharePercentage) {
        this.sharePercentage = sharePercentage;
    }

    public List getNomineeList() {
        return nomineeList;
    }

    public void setNomineeList(List nomineeList) {
        this.nomineeList = nomineeList;
    }

    public String getFamilySex() {
        return familySex;
    }

    public void setFamilySex(String familySex) {
        this.familySex = familySex;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }

    public String getMinorFlag() {
        return minorFlag;
    }

    public void setMinorFlag(String minorFlag) {
        this.minorFlag = minorFlag;
    }

    public String getGurdianName() {
        return gurdianName;
    }

    public void setGurdianName(String gurdianName) {
        this.gurdianName = gurdianName;
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

    public String getSalutationGaurdian() {
        return salutationGaurdian;
    }

    public void setSalutationGaurdian(String salutationGaurdian) {
        this.salutationGaurdian = salutationGaurdian;
    }

    public String getSalutationNomineeGaurdian() {
        return salutationNomineeGaurdian;
    }

    public void setSalutationNomineeGaurdian(String salutationNomineeGaurdian) {
        this.salutationNomineeGaurdian = salutationNomineeGaurdian;
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

    public String getRetirementBenefitType() {
        return retirementBenefitType;
    }

    public void setRetirementBenefitType(String retirementBenefitType) {
        this.retirementBenefitType = retirementBenefitType;
    }

    public String getNomineeTyp() {
        return nomineeTyp;
    }

    public void setNomineeTyp(String nomineeTyp) {
        this.nomineeTyp = nomineeTyp;
    }

    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
   
}
