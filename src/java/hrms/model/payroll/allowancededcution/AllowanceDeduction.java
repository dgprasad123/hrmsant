/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.allowancededcution;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Manas Jena
 */
public class AllowanceDeduction {

    private String refadcode = null;
    private String adcode = null;
    private String addesc = null;
    private String adcodename = null;
    private String adtype = null;
    private String formula = null;
    private String head = null;
    private int advalue = 0;
    private String fixedValue = null;
    private String whereupdated = null;
    private int isfixed = 0;
    private String updationRefCode=null;    
    private String adamttype=null;
    private int isupdated = 0;    
    private String dedType = null;
    private String altUnit = null;
    private int rownum = 0;
    private int repCol = 0;
    
  private int slno;
    private BigDecimal billgroupid;
    private String billgroupdesc;
    private String empName=null;
    private BigDecimal fixValue;
    private String deductionType=null;
    private int cnt=0;
    
    private String pvtDedn;
    private String deduction;
    private MultipartFile uploadedFile;
    private MultipartFile uploadedDednFile;
    
    private String sltAllowanceDeduction;
    private String sltAllowanceDeductionData;
    private String chkEmployee;
    
    public String getFixedValue() {
        return fixedValue;
    }

    public void setFixedValue(String fixedValue) {
        this.fixedValue = fixedValue;
    }
    private String accNo = null;
    private String schedule = null;
    private List postList;
    private String postCode = null;
    
    private String allowance;
    private String sltAllowanceFormula;
    private MultipartFile uploadedAllowanceFile;

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }
    private String postName = null;

    public void setIsupdated(int isupdated) {
        this.isupdated = isupdated;
    }

    public int getIsupdated() {
        return isupdated;
    }

    public String getDedType() {
        return dedType;
    }

    public void setDedType(String dedType) {
        this.dedType = dedType;
    }

    public String getAltUnit() {
        return altUnit;
    }

    public void setAltUnit(String altUnit) {
        this.altUnit = altUnit;
    }

    public int getRownum() {
        return rownum;
    }

    public void setRownum(int rownum) {
        this.rownum = rownum;
    }

    public int getRepCol() {
        return repCol;
    }

    public void setRepCol(int repCol) {
        this.repCol = repCol;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
    
    

    public String getRefadcode() {
        return refadcode;
    }

    public void setRefadcode(String refadcode) {
        this.refadcode = refadcode;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getAddesc() {
        return addesc;
    }

    public void setAddesc(String addesc) {
        this.addesc = addesc;
    }

    public String getAdcodename() {
        return adcodename;
    }

    public void setAdcodename(String adcodename) {
        this.adcodename = adcodename;
    }

    public String getAdtype() {
        return adtype;
    }

    public void setAdtype(String adtype) {
        this.adtype = adtype;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public int getAdvalue() {
        return advalue;
    }

    public void setAdvalue(int advalue) {
        this.advalue = advalue;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getWhereupdated() {
        return whereupdated;
    }

    public void setWhereupdated(String whereupdated) {
        this.whereupdated = whereupdated;
    }

    public int getIsfixed() {
        return isfixed;
    }

    public void setIsfixed(int isfixed) {
        this.isfixed = isfixed;
    }

    public String getUpdationRefCode() {
        return updationRefCode;
    }

    public void setUpdationRefCode(String updationRefCode) {
        this.updationRefCode = updationRefCode;
    }

    public String getAdamttype() {
        return adamttype;
    }

    public void setAdamttype(String adamttype) {
        this.adamttype = adamttype;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public List getPostList() {
        return postList;
    }

    public void setPostList(List postList) {
        this.postList = postList;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPvtDedn() {
        return pvtDedn;
    }

    public void setPvtDedn(String pvtDedn) {
        this.pvtDedn = pvtDedn;
    }

    public MultipartFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(MultipartFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public int getSlno() {
        return slno;
    }

    public void setSlno(int slno) {
        this.slno = slno;
    }

    public BigDecimal getBillgroupid() {
        return billgroupid;
    }

    public void setBillgroupid(BigDecimal billgroupid) {
        this.billgroupid = billgroupid;
    }

    public String getBillgroupdesc() {
        return billgroupdesc;
    }

    public void setBillgroupdesc(String billgroupdesc) {
        this.billgroupdesc = billgroupdesc;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public BigDecimal getFixValue() {
        return fixValue;
    }

    public void setFixValue(BigDecimal fixValue) {
        this.fixValue = fixValue;
    }

    public String getDeductionType() {
        return deductionType;
    }

    public void setDeductionType(String deductionType) {
        this.deductionType = deductionType;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public String getSltAllowanceDeduction() {
        return sltAllowanceDeduction;
    }

    public void setSltAllowanceDeduction(String sltAllowanceDeduction) {
        this.sltAllowanceDeduction = sltAllowanceDeduction;
    }

    public String getSltAllowanceDeductionData() {
        return sltAllowanceDeductionData;
    }

    public void setSltAllowanceDeductionData(String sltAllowanceDeductionData) {
        this.sltAllowanceDeductionData = sltAllowanceDeductionData;
    }

    public String getChkEmployee() {
        return chkEmployee;
    }

    public void setChkEmployee(String chkEmployee) {
        this.chkEmployee = chkEmployee;
    }

    public String getDeduction() {
        return deduction;
    }

    public void setDeduction(String deduction) {
        this.deduction = deduction;
    }

    public MultipartFile getUploadedDednFile() {
        return uploadedDednFile;
    }

    public void setUploadedDednFile(MultipartFile uploadedDednFile) {
        this.uploadedDednFile = uploadedDednFile;
    }

    public String getSltAllowanceFormula() {
        return sltAllowanceFormula;
    }

    public void setSltAllowanceFormula(String sltAllowanceFormula) {
        this.sltAllowanceFormula = sltAllowanceFormula;
    }

    public String getAllowance() {
        return allowance;
    }

    public void setAllowance(String allowance) {
        this.allowance = allowance;
    }

    public MultipartFile getUploadedAllowanceFile() {
        return uploadedAllowanceFile;
    }

    public void setUploadedAllowanceFile(MultipartFile uploadedAllowanceFile) {
        this.uploadedAllowanceFile = uploadedAllowanceFile;
    }
}
