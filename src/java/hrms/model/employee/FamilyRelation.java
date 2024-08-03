/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.employee;

/**
 *
 * @author Manas Jena
 */
public class FamilyRelation {

    private String empId;
    private int slno = 0;
    private String relation;
    private String ifalive;
    private String initials;
    private String fname;
    private String mname;
    private String lname;
    private int employeeempid = 0;
    private String gender;
    private String dob;
    private String mobile;
    private String marital_statuS;
    private String pension_date;
    private int share_pct = 0;
    private String ifsc_code;
    private String bank_acc_no;
    private String sltBank;
    private String branch_code;
    private String is_pwd;
    private String pwd_type;
    private String is_minor;
    private String minor_guardian;
    private String remarks;
    private String address;
    private int priority_lvl = 0;
    private String relname;
    private String is_Nominee;
    private String fatherName;
    private String mode;

    private String isLocked;

    private String sltAmountType;
    private String fixedAmount;
    private String formula;

    private String identityDocType;
    private String identityDocNo;
    private String nomGpf;
    private String nomCvpDcrgLta;
    private int arrearsPer = 0;
    private int arrearsAcc = 0;
    private String familyHandicappedTypeId;
    private String familyHandicappedFlag;
    private String intMaritalStatusTypeId;
    private String salutationFamilyGuardian;
    private String gurdianName;

    private String strMarriageStatus;

    private String isGuardian;
    
    private String marital;
    private String gaurdianInitials;
    private String age;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public int getSlno() {
        return slno;
    }

    public void setSlno(int slno) {
        this.slno = slno;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getIs_Nominee() {
        return is_Nominee;
    }

    public void setIs_Nominee(String is_Nominee) {
        this.is_Nominee = is_Nominee;
    }

    public String getRelname() {
        return relname;
    }

    public void setRelname(String relname) {
        this.relname = relname;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getIfalive() {
        return ifalive;
    }

    public void setIfalive(String ifalive) {
        this.ifalive = ifalive;
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

    public int getEmployeeempid() {
        return employeeempid;
    }

    public void setEmployeeempid(int employeeempid) {
        this.employeeempid = employeeempid;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMarital_statuS() {
        return marital_statuS;
    }

    public void setMarital_statuS(String marital_statuS) {
        this.marital_statuS = marital_statuS;
    }

    public String getPension_date() {
        return pension_date;
    }

    public void setPension_date(String pension_date) {
        this.pension_date = pension_date;
    }

    public int getShare_pct() {
        return share_pct;
    }

    public void setShare_pct(int share_pct) {
        this.share_pct = share_pct;
    }

    public String getIfsc_code() {
        return ifsc_code;
    }

    public void setIfsc_code(String ifsc_code) {
        this.ifsc_code = ifsc_code;
    }

    public String getBank_acc_no() {
        return bank_acc_no;
    }

    public void setBank_acc_no(String bank_acc_no) {
        this.bank_acc_no = bank_acc_no;
    }

    public String getBranch_code() {
        return branch_code;
    }

    public void setBranch_code(String branch_code) {
        this.branch_code = branch_code;
    }

    public String getIs_pwd() {
        return is_pwd;
    }

    public void setIs_pwd(String is_pwd) {
        this.is_pwd = is_pwd;
    }

    public String getPwd_type() {
        return pwd_type;
    }

    public void setPwd_type(String pwd_type) {
        this.pwd_type = pwd_type;
    }

    public String getIs_minor() {
        return is_minor;
    }

    public void setIs_minor(String is_minor) {
        this.is_minor = is_minor;
    }

    public String getMinor_guardian() {
        return minor_guardian;
    }

    public void setMinor_guardian(String minor_guardian) {
        this.minor_guardian = minor_guardian;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPriority_lvl() {
        return priority_lvl;
    }

    public void setPriority_lvl(int priority_lvl) {
        this.priority_lvl = priority_lvl;
    }

    public String getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(String isLocked) {
        this.isLocked = isLocked;
    }

    public String getSltBank() {
        return sltBank;
    }

    public void setSltBank(String sltBank) {
        this.sltBank = sltBank;
    }

    public String getSltAmountType() {
        return sltAmountType;
    }

    public void setSltAmountType(String sltAmountType) {
        this.sltAmountType = sltAmountType;
    }

    public String getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(String fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getIdentityDocType() {
        return identityDocType;
    }

    public void setIdentityDocType(String identityDocType) {
        this.identityDocType = identityDocType;
    }

    public String getIdentityDocNo() {
        return identityDocNo;
    }

    public void setIdentityDocNo(String identityDocNo) {
        this.identityDocNo = identityDocNo;
    }

    public String getNomGpf() {
        return nomGpf;
    }

    public void setNomGpf(String nomGpf) {
        this.nomGpf = nomGpf;
    }

    public String getNomCvpDcrgLta() {
        return nomCvpDcrgLta;
    }

    public void setNomCvpDcrgLta(String nomCvpDcrgLta) {
        this.nomCvpDcrgLta = nomCvpDcrgLta;
    }

    public int getArrearsPer() {
        return arrearsPer;
    }

    public void setArrearsPer(int arrearsPer) {
        this.arrearsPer = arrearsPer;
    }

    public int getArrearsAcc() {
        return arrearsAcc;
    }

    public void setArrearsAcc(int arrearsAcc) {
        this.arrearsAcc = arrearsAcc;
    }

    public String getMarital() {
        return marital;
    }

    public void setMarital(String marital) {
        this.marital = marital;
    }

    public String getIsGuardian() {
        return isGuardian;
    }

    public void setIsGuardian(String isGuardian) {
        this.isGuardian = isGuardian;
    }

    public String getGaurdianInitials() {
        return gaurdianInitials;
    }

    public void setGaurdianInitials(String gaurdianInitials) {
        this.gaurdianInitials = gaurdianInitials;
    }

    public String getFamilyHandicappedTypeId() {
        return familyHandicappedTypeId;
    }

    public void setFamilyHandicappedTypeId(String familyHandicappedTypeId) {
        this.familyHandicappedTypeId = familyHandicappedTypeId;
    }

    public String getFamilyHandicappedFlag() {
        return familyHandicappedFlag;
    }

    public void setFamilyHandicappedFlag(String familyHandicappedFlag) {
        this.familyHandicappedFlag = familyHandicappedFlag;
    }

    public String getIntMaritalStatusTypeId() {
        return intMaritalStatusTypeId;
    }

    public void setIntMaritalStatusTypeId(String intMaritalStatusTypeId) {
        this.intMaritalStatusTypeId = intMaritalStatusTypeId;
    }

    public String getSalutationFamilyGuardian() {
        return salutationFamilyGuardian;
    }

    public void setSalutationFamilyGuardian(String salutationFamilyGuardian) {
        this.salutationFamilyGuardian = salutationFamilyGuardian;
    }

    public String getGurdianName() {
        return gurdianName;
    }

    public void setGurdianName(String gurdianName) {
        this.gurdianName = gurdianName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getStrMarriageStatus() {
        return strMarriageStatus;
    }

    public void setStrMarriageStatus(String strMarriageStatus) {
        this.strMarriageStatus = strMarriageStatus;
    }

}
