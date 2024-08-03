/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.master;

import java.io.Serializable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

/**
 *
 * @author Surendra
 */
public class SubstantivePost implements Serializable {

    private String spc = null;
    private String spn = null;
    private Office office;
    private GPost gpost;
    private String postname;
    private String empname;
    private String empid;
    private String spcHrmsId;
    private String gpc = null;
    private String postCode;
    private String payscale;
    private String paylevel;
    private String postgrp;
    private String mode;
    private String txtNoOfPost;
    private String subid;
    private String territory;
    private String payscale_7th;
    private String payCommision;
    private String gp;
    private String dept;
    private String cadre;
    
    private String deptCode;
    private String offCode;    
    private String totalPost;    
    private String orderNo;
    private String orderDate;
    
    private String chkGrantInAid;
    private String chkTeachingPost;
    private String chkPlanOrNonPlan;
    private String ifUclean;
    private String isSanctioned;
    private String officeName;
    private String empName;
    private String isRegular;
    private String gpfNo;

    private String aerId;
    
    private MultipartFile spcFileAttch;
    private MultipartFile spcTerminateFileAttch;
    
    private String terminationOrderNo;
    private String terminationOrderDate;
    private String cntDuplicateSpc;
    private String rdSpc;
    private String hidOffCode;
    private String hidGpc;
    private String hidEmpCode;
    private String ifadcharge;
    
    public String getIsRegular() {
        return isRegular;
    }

    public void setIsRegular(String isRegular) {
        this.isRegular = isRegular;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getIfUclean() {
        return ifUclean;
    }

    public void setIfUclean(String ifUclean) {
        this.ifUclean = ifUclean;
    }

    public String getIsSanctioned() {
        return isSanctioned;
    }

    public void setIsSanctioned(String isSanctioned) {
        this.isSanctioned = isSanctioned;
    }
    
    public String getGpc() {
        return gpc;
    }

    public void setGpc(String gpc) {
        this.gpc = gpc;
    }

    public String getSpc() {
        return spc;
    }

    public void setSpc(String spc) {
        this.spc = spc;
    }

    public String getSpn() {
        return spn;
    }

    public void setSpn(String spn) {
        this.spn = spn;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public GPost getGpost() {
        return gpost;
    }

    public void setGpost(GPost gpost) {
        this.gpost = gpost;
    }

    public String getPostname() {
        return postname;
    }

    public void setPostname(String postname) {
        this.postname = postname;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getSpcHrmsId() {
        return spcHrmsId;
    }

    public void setSpcHrmsId(String spcHrmsId) {
        this.spcHrmsId = spcHrmsId;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPayscale() {
        return payscale;
    }

    public void setPayscale(String payscale) {
        this.payscale = payscale;
    }

    public String getPaylevel() {
        return paylevel;
    }

    public void setPaylevel(String paylevel) {
        this.paylevel = paylevel;
    }

    public String getPostgrp() {
        return postgrp;
    }

    public void setPostgrp(String postgrp) {
        this.postgrp = postgrp;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getTxtNoOfPost() {
        return txtNoOfPost;
    }

    public void setTxtNoOfPost(String txtNoOfPost) {
        this.txtNoOfPost = txtNoOfPost;
    }

    public String getSubid() {
        return subid;
    }

    public void setSubid(String subid) {
        this.subid = subid;
    }

    public String getTerritory() {
        return territory;
    }

    public void setTerritory(String territory) {
        this.territory = territory;
    }

    public String getPayscale_7th() {
        return payscale_7th;
    }

    public void setPayscale_7th(String payscale_7th) {
        this.payscale_7th = payscale_7th;
    }

    public String getPayCommision() {
        return payCommision;
    }

    public void setPayCommision(String payCommision) {
        this.payCommision = payCommision;
    }

    public String getGp() {
        return gp;
    }

    public void setGp(String gp) {
        this.gp = gp;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getCadre() {
        return cadre;
    }

    public void setCadre(String cadre) {
        this.cadre = cadre;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getOffCode() {
        return offCode;
    }

    public void setOffCode(String offCode) {
        this.offCode = offCode;
    }

    public String getTotalPost() {
        return totalPost;
    }

    public void setTotalPost(String totalPost) {
        this.totalPost = totalPost;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getChkGrantInAid() {
        return chkGrantInAid;
    }

    public void setChkGrantInAid(String chkGrantInAid) {
        this.chkGrantInAid = chkGrantInAid;
    }

    public String getChkTeachingPost() {
        return chkTeachingPost;
    }

    public void setChkTeachingPost(String chkTeachingPost) {
        this.chkTeachingPost = chkTeachingPost;
    }

    public String getChkPlanOrNonPlan() {
        return chkPlanOrNonPlan;
    }

    public void setChkPlanOrNonPlan(String chkPlanOrNonPlan) {
        this.chkPlanOrNonPlan = chkPlanOrNonPlan;
    }

    public String getGpfNo() {
        return gpfNo;
    }

    public void setGpfNo(String gpfNo) {
        this.gpfNo = gpfNo;
    }

    public String getAerId() {
        return aerId;
    }

    public void setAerId(String aerId) {
        this.aerId = aerId;
    }

    public MultipartFile getSpcFileAttch() {
        return spcFileAttch;
    }

    public void setSpcFileAttch(MultipartFile spcFileAttch) {
        this.spcFileAttch = spcFileAttch;
    }

    public MultipartFile getSpcTerminateFileAttch() {
        return spcTerminateFileAttch;
    }

    public void setSpcTerminateFileAttch(MultipartFile spcTerminateFileAttch) {
        this.spcTerminateFileAttch = spcTerminateFileAttch;
    }

    public String getTerminationOrderNo() {
        return terminationOrderNo;
    }

    public void setTerminationOrderNo(String terminationOrderNo) {
        this.terminationOrderNo = terminationOrderNo;
    }

    public String getTerminationOrderDate() {
        return terminationOrderDate;
    }

    public void setTerminationOrderDate(String terminationOrderDate) {
        this.terminationOrderDate = terminationOrderDate;
    }

    public String getCntDuplicateSpc() {
        return cntDuplicateSpc;
    }

    public void setCntDuplicateSpc(String cntDuplicateSpc) {
        this.cntDuplicateSpc = cntDuplicateSpc;
    }

    public String getRdSpc() {
        return rdSpc;
    }

    public void setRdSpc(String rdSpc) {
        this.rdSpc = rdSpc;
    }

    public String getHidOffCode() {
        return hidOffCode;
    }

    public void setHidOffCode(String hidOffCode) {
        this.hidOffCode = hidOffCode;
    }

    public String getHidGpc() {
        return hidGpc;
    }

    public void setHidGpc(String hidGpc) {
        this.hidGpc = hidGpc;
    }

    public String getHidEmpCode() {
        return hidEmpCode;
    }

    public void setHidEmpCode(String hidEmpCode) {
        this.hidEmpCode = hidEmpCode;
    }

    public String getIfadcharge() {
        return ifadcharge;
    }

    public void setIfadcharge(String ifadcharge) {
        this.ifadcharge = ifadcharge;
    }

   
    
    
    

    
}
