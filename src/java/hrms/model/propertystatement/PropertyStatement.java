/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.propertystatement;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Manas Jena
 */
public class PropertyStatement {
    private String fullname = null;
    private int slNo = 0;    
    private String empid = null;
    private String payscale = null;
    private  BigDecimal yearlyPropId = null;
    private String fiscalyear = null;
    private int statusid = 0;
    private Date submittedOn = null;
    private Date fromdate = null;
    private Date todate = null;
    private int curbasicsalary = 0;
    private int gradepay = 0;
    private String curcadrecode = null;
    private String curspc = null;
    private String spn = null;
    private String postgroup = null;
    private String submissiontype = null;
    private String prevyearProperty;
    private String isclosed;
    private String gpfNo;
    
    private String createdByIp;
     private String submittedByIp;
    
    private String isImmovableBlank;
    private String isMovableBlank;
    private String loginByIp;
    
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    
    public int getSlNo() {
        return slNo;
    }

    public void setSlNo(int slNo) {
        this.slNo = slNo;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getPayscale() {
        return payscale;
    }

    public void setPayscale(String payscale) {
        this.payscale = payscale;
    }

    public int getCurbasicsalary() {
        return curbasicsalary;
    }

    public void setCurbasicsalary(int curbasicsalary) {
        this.curbasicsalary = curbasicsalary;
    }

    public BigDecimal getYearlyPropId() {
        return yearlyPropId;
    }

    public void setYearlyPropId(BigDecimal yearlyPropId) {
        this.yearlyPropId = yearlyPropId;
    }

    public String getFiscalyear() {
        return fiscalyear;
    }

    public void setFiscalyear(String fiscalyear) {
        this.fiscalyear = fiscalyear;
    }    

    public int getStatusid() {
        return statusid;
    }

    public void setStatusid(int statusid) {
        this.statusid = statusid;
    }

    public Date getSubmittedOn() {
        return submittedOn;
    }

    public void setSubmittedOn(Date submittedOn) {
        this.submittedOn = submittedOn;
    }

    public Date getFromdate() {
        return fromdate;
    }

    public void setFromdate(Date fromdate) {
        this.fromdate = fromdate;
    }

    public Date getTodate() {
        return todate;
    }

    public void setTodate(Date todate) {
        this.todate = todate;
    }

    public String getCurcadrecode() {
        return curcadrecode;
    }

    public void setCurcadrecode(String curcadrecode) {
        this.curcadrecode = curcadrecode;
    }

    public String getCurspc() {
        return curspc;
    }

    public void setCurspc(String curspc) {
        this.curspc = curspc;
    }

    public String getSpn() {
        return spn;
    }

    public void setSpn(String spn) {
        this.spn = spn;
    }

    public String getPostgroup() {
        return postgroup;
    }

    public void setPostgroup(String postgroup) {
        this.postgroup = postgroup;
    }

    public String getSubmissiontype() {
        return submissiontype;
    }

    public void setSubmissiontype(String submissiontype) {
        this.submissiontype = submissiontype;
    }

    public int getGradepay() {
        return gradepay;
    }

    public void setGradepay(int gradepay) {
        this.gradepay = gradepay;
    }

    public String getPrevyearProperty() {
        return prevyearProperty;
    }

    public void setPrevyearProperty(String prevyearProperty) {
        this.prevyearProperty = prevyearProperty;
    }

    public String getIsclosed() {
        return isclosed;
    }

    public void setIsclosed(String isclosed) {
        this.isclosed = isclosed;
    }

    public String getGpfNo() {
        return gpfNo;
    }

    public void setGpfNo(String gpfNo) {
        this.gpfNo = gpfNo;
    }

    public String getIsImmovableBlank() {
        return isImmovableBlank;
    }

    public void setIsImmovableBlank(String isImmovableBlank) {
        this.isImmovableBlank = isImmovableBlank;
    }

    public String getIsMovableBlank() {
        return isMovableBlank;
    }

    public void setIsMovableBlank(String isMovableBlank) {
        this.isMovableBlank = isMovableBlank;
    }

    public String getCreatedByIp() {
        return createdByIp;
    }

    public void setCreatedByIp(String createdByIp) {
        this.createdByIp = createdByIp;
    }

    public String getSubmittedByIp() {
        return submittedByIp;
    }

    public void setSubmittedByIp(String submittedByIp) {
        this.submittedByIp = submittedByIp;
    }

    public String getLoginByIp() {
        return loginByIp;
    }

    public void setLoginByIp(String loginByIp) {
        this.loginByIp = loginByIp;
    }
    
    
}
