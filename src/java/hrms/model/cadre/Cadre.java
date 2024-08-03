

package hrms.model.cadre;

import java.io.Serializable;

public class Cadre implements Serializable{
    
    private String cadreCode;
    
    private String cadreName;
    
    private String deptCode = null;
    private String hiddeptCode = null;
    
    private String hidcadreCode;
    private String hrmsId;
    private String gpfno;
    private String empfullName;
    private String officeName;
    private String designation;
   
    
    
    
    private int notId;
    private String notType;
    private String empId;
    private String cadreDept;
    private String grade;
    private String cadreLvl;
    private String description;
    private String allotmentYear;
    private String cadreId;
    private String postingDept;
    private String postCode;
    private String postClassification;
    private String postStatus;
    private String cadreJoiningWEFDt;
    private String cadreJoiningWEFTime;
    private String ifJoined;
    private String joinedAsSuch;
    private String ifProforma;
    private String deptName;
    private String postGrp;
    private int gradeCode;
    private String gradeCodeName;

    private String refcorrectionid;
    
    public String getHiddeptCode() {
        return hiddeptCode;
    }

    public void setHiddeptCode(String hiddeptCode) {
        this.hiddeptCode = hiddeptCode;
    }
    
    public String getHrmsId() {
        return hrmsId;
    }

    public void setHrmsId(String hrmsId) {
        this.hrmsId = hrmsId;
    }

    
    public String getGpfno() {
        return gpfno;
    }

    public void setGpfno(String gpfno) {
        this.gpfno = gpfno;
    }

    public String getEmpfullName() {
        return empfullName;
    }

    public void setEmpfullName(String empfullName) {
        this.empfullName = empfullName;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
    public String getHidcadreCode() {
        return hidcadreCode;
    }

    public void setHidcadreCode(String hidcadreCode) {
        this.hidcadreCode = hidcadreCode;
    } 
    
    public String getCadreCode() {
        return cadreCode;
    }

    public void setCadreCode(String cadreCode) {
        this.cadreCode = cadreCode;
    }

    public String getCadreName() {
        return cadreName;
    }

    public void setCadreName(String cadreName) {
        this.cadreName = cadreName;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    

    public String getNotType() {
        return notType;
    }

    public void setNotType(String notType) {
        this.notType = notType;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getCadreDept() {
        return cadreDept;
    }

    public void setCadreDept(String cadreDept) {
        this.cadreDept = cadreDept;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getCadreLvl() {
        return cadreLvl;
    }

    public void setCadreLvl(String cadreLvl) {
        this.cadreLvl = cadreLvl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAllotmentYear() {
        return allotmentYear;
    }

    public void setAllotmentYear(String allotmentYear) {
        this.allotmentYear = allotmentYear;
    }

    public String getCadreId() {
        return cadreId;
    }

    public void setCadreId(String cadreId) {
        this.cadreId = cadreId;
    }

    public String getPostingDept() {
        return postingDept;
    }

    public void setPostingDept(String postingDept) {
        this.postingDept = postingDept;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPostClassification() {
        return postClassification;
    }

    public void setPostClassification(String postClassification) {
        this.postClassification = postClassification;
    }

    public String getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(String postStatus) {
        this.postStatus = postStatus;
    }

    public String getCadreJoiningWEFDt() {
        return cadreJoiningWEFDt;
    }

    public void setCadreJoiningWEFDt(String cadreJoiningWEFDt) {
        this.cadreJoiningWEFDt = cadreJoiningWEFDt;
    }

    public String getCadreJoiningWEFTime() {
        return cadreJoiningWEFTime;
    }

    public void setCadreJoiningWEFTime(String cadreJoiningWEFTime) {
        this.cadreJoiningWEFTime = cadreJoiningWEFTime;
    }

    public String getIfJoined() {
        return ifJoined;
    }

    public void setIfJoined(String ifJoined) {
        this.ifJoined = ifJoined;
    }

    public String getJoinedAsSuch() {
        return joinedAsSuch;
    }

    public void setJoinedAsSuch(String joinedAsSuch) {
        this.joinedAsSuch = joinedAsSuch;
    }

    public String getIfProforma() {
        return ifProforma;
    }

    public void setIfProforma(String ifProforma) {
        this.ifProforma = ifProforma;
    }

    public int getNotId() {
        return notId;
    }

    public void setNotId(int notId) {
        this.notId = notId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPostGrp() {
        return postGrp;
    }

    public void setPostGrp(String postGrp) {
        this.postGrp = postGrp;
    }

    public int getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(int gradeCode) {
        this.gradeCode = gradeCode;
    }

    public String getGradeCodeName() {
        return gradeCodeName;
    }

    public void setGradeCodeName(String gradeCodeName) {
        this.gradeCodeName = gradeCodeName;
    }

    public String getRefcorrectionid() {
        return refcorrectionid;
    }

    public void setRefcorrectionid(String refcorrectionid) {
        this.refcorrectionid = refcorrectionid;
    }
}
