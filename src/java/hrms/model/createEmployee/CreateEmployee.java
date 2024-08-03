/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.createEmployee;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author lenovo
 */
public class CreateEmployee {

    private String empId = null;
    private String gpfNo = null;
    private String title = null;
    private String firstnmae = null;
    private String mname = null;
    private String lname = null;
    private String gender = null;
    private String faname = null;
    private String accountType = null;
    private String gptNumber = null;
    private String gpfType = null;
    private String tpfType = null;
    private String doj = null;
    private String dob = null;
    private String rTitle = null;
    private String rFirstnmae = null;
    private String rMname = null;
    private String rLname = null;
    private String empType = null;
    private String relation = null;
    private String offCode=null;
    private String assumed=null;
    private String mobile=null;
    private String post=null;
    private String postNmclture=null;
    private int basicpay;
    private int gp;
    private int enable;
    private int cnonexpired;
    private MultipartFile documentFile;
    private String aadharNo=null;
    private String postGroup="";
    private String othNumber=null;
    
    private String sltPayCommission;
    
    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getGpfNo() {
        return gpfNo;
    }

    public void setGpfNo(String gpfNo) {
        this.gpfNo = gpfNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstnmae() {
        return firstnmae;
    }

    public void setFirstnmae(String firstnmae) {
        this.firstnmae = firstnmae;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFaname() {
        return faname;
    }

    public void setFaname(String faname) {
        this.faname = faname;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getGptNumber() {
        return gptNumber;
    }

    public void setGptNumber(String gptNumber) {
        this.gptNumber = gptNumber;
    }

    public String getGpfType() {
        return gpfType;
    }

    public void setGpfType(String gpfType) {
        this.gpfType = gpfType;
    }

    public String getTpfType() {
        return tpfType;
    }

    public void setTpfType(String tpfType) {
        this.tpfType = tpfType;
    }

    public String getDoj() {
        return doj;
    }

    public void setDoj(String doj) {
        this.doj = doj;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getrTitle() {
        return rTitle;
    }

    public void setrTitle(String rTitle) {
        this.rTitle = rTitle;
    }

    public String getrFirstnmae() {
        return rFirstnmae;
    }

    public void setrFirstnmae(String rFirstnmae) {
        this.rFirstnmae = rFirstnmae;
    }

    public String getrMname() {
        return rMname;
    }

    public void setrMname(String rMname) {
        this.rMname = rMname;
    }

    public String getrLname() {
        return rLname;
    }

    public void setrLname(String rLname) {
        this.rLname = rLname;
    }

    public String getEmpType() {
        return empType;
    }

    public void setEmpType(String empType) {
        this.empType = empType;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getOffCode() {
        return offCode;
    }

    public void setOffCode(String offCode) {
        this.offCode = offCode;
    }

    public String getAssumed() {
        return assumed;
    }

    public void setAssumed(String assumed) {
        this.assumed = assumed;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public int getBasicpay() {
        return basicpay;
    }

    public void setBasicpay(int basicpay) {
        this.basicpay = basicpay;
    }

    public int getGp() {
        return gp;
    }

    public void setGp(int gp) {
        this.gp = gp;
    }

    public MultipartFile getDocumentFile() {
        return documentFile;
    }

    public void setDocumentFile(MultipartFile documentFile) {
        this.documentFile = documentFile;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public int getCnonexpired() {
        return cnonexpired;
    }

    public void setCnonexpired(int cnonexpired) {
        this.cnonexpired = cnonexpired;
    }

    public String getPostNmclture() {
        return postNmclture;
    }

    public void setPostNmclture(String postNmclture) {
        this.postNmclture = postNmclture;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getPostGroup() {
        return postGroup;
    }

    public void setPostGroup(String postGroup) {
        this.postGroup = postGroup;
    }

    public String getSltPayCommission() {
        return sltPayCommission;
    }

    public void setSltPayCommission(String sltPayCommission) {
        this.sltPayCommission = sltPayCommission;
    }

    public String getOthNumber() {
        return othNumber;
    }

    public void setOthNumber(String othNumber) {
        this.othNumber = othNumber;
    }
    

}
