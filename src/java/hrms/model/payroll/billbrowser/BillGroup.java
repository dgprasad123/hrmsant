/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.billbrowser;

import java.math.BigDecimal;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Manas Jena
 */
public class BillGroup {

    private int slno;
    private BigDecimal billgroupid;
    private String billgroupdesc;
    private String plan = null;
    private String sector = null;
    private String majorHead = null;
    private String majorHeadDesc = null;
    private String subMajorHead = null;
    private String subMajorHeadDesc = null;
    private String minorHead = null;
    private String minorHeadDesc = null;
    private String subMinorHead1 = null;
    private String subMinorHeadDesc1 = null;
    private String subMinorHead2 = null;
    private String subMinorHeadDesc2 = null;
    private String subMinorHead3 = null;
    private String subMinorHeadDesc3 = null;
    private String postclass = null;
    private String demandNo = null;
    private String chartofaccount = null;
    private String offcode = null;
    private String billtype = null;
    private String configlvl = null;
    private String payhead = null;

    private int fromYear;
    private int toYear;
    private int fromMonth;
    private int toMonth;
    private String empName = null;
    private String empCode = null;
    private int grossAmount = 0;
    private int netAmount = 0;
    private int grossAmount1 = 0;
    private int netAmount1 = 0;
    
    private String showInAquitance;
    private String submitToTreasury;
    
    private String sltCOList;
    private String offName;
    private String sectionId;
    private String sectionName;
    private String offlvl;
    
    public String getOffcode() {
        return offcode;
    }

    public void setOffcode(String offcode) {
        this.offcode = offcode;
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

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getMajorHead() {
        return majorHead;
    }

    public void setMajorHead(String majorHead) {
        this.majorHead = majorHead;
    }

    public String getMajorHeadDesc() {
        return majorHeadDesc;
    }

    public void setMajorHeadDesc(String majorHeadDesc) {
        this.majorHeadDesc = majorHeadDesc;
    }

    public String getSubMajorHead() {
        return subMajorHead;
    }

    public void setSubMajorHead(String subMajorHead) {
        this.subMajorHead = subMajorHead;
    }

    public String getSubMajorHeadDesc() {
        return subMajorHeadDesc;
    }

    public void setSubMajorHeadDesc(String subMajorHeadDesc) {
        this.subMajorHeadDesc = subMajorHeadDesc;
    }

    public String getMinorHead() {
        return minorHead;
    }

    public void setMinorHead(String minorHead) {
        this.minorHead = minorHead;
    }

    public String getMinorHeadDesc() {
        return minorHeadDesc;
    }

    public void setMinorHeadDesc(String minorHeadDesc) {
        this.minorHeadDesc = minorHeadDesc;
    }

    public String getSubMinorHead1() {
        return subMinorHead1;
    }

    public void setSubMinorHead1(String subMinorHead1) {
        this.subMinorHead1 = subMinorHead1;
    }

    public String getSubMinorHeadDesc1() {
        return subMinorHeadDesc1;
    }

    public void setSubMinorHeadDesc1(String subMinorHeadDesc1) {
        this.subMinorHeadDesc1 = subMinorHeadDesc1;
    }

    public String getSubMinorHead2() {
        return subMinorHead2;
    }

    public void setSubMinorHead2(String subMinorHead2) {
        this.subMinorHead2 = subMinorHead2;
    }

    public String getSubMinorHeadDesc2() {
        return subMinorHeadDesc2;
    }

    public void setSubMinorHeadDesc2(String subMinorHeadDesc2) {
        this.subMinorHeadDesc2 = subMinorHeadDesc2;
    }

    public String getSubMinorHead3() {
        return subMinorHead3;
    }

    public void setSubMinorHead3(String subMinorHead3) {
        this.subMinorHead3 = subMinorHead3;
    }

    public String getSubMinorHeadDesc3() {
        return subMinorHeadDesc3;
    }

    public void setSubMinorHeadDesc3(String subMinorHeadDesc3) {
        this.subMinorHeadDesc3 = subMinorHeadDesc3;
    }

    public String getPostclass() {
        return postclass;
    }

    public void setPostclass(String postclass) {
        this.postclass = postclass;
    }

    public String getDemandNo() {
        return demandNo;
    }

    public void setDemandNo(String demandNo) {
        this.demandNo = demandNo;
    }

    public String getChartofaccount() {
        chartofaccount = StringUtils.defaultString(demandNo) + "-" + StringUtils.defaultString(majorHead) + "-" + StringUtils.defaultString(subMajorHead) + "-" + StringUtils.defaultString(minorHead) + "-" + StringUtils.defaultString(subMinorHead1) + "-" + StringUtils.defaultString(subMinorHead2) + "-" + StringUtils.defaultString(payhead) + "-" + StringUtils.defaultString(plan)+ "-" + StringUtils.defaultString(subMinorHead3)+ "-" + StringUtils.defaultString(sector);
        return chartofaccount;
    }

    public void setChartofaccount(String chartofaccount) {
        this.chartofaccount = chartofaccount;
    }

    public String getBilltype() {
        return billtype;
    }

    public void setBilltype(String billtype) {
        this.billtype = billtype;
    }

    public String getConfiglvl() {
        return configlvl;
    }

    public void setConfiglvl(String configlvl) {
        this.configlvl = configlvl;
    }

    public String getPayhead() {
        return payhead;
    }

    public void setPayhead(String payhead) {
        this.payhead = payhead;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public int getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(int grossAmount) {
        this.grossAmount = grossAmount;
    }

    public int getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(int netAmount) {
        this.netAmount = netAmount;
    }

    public int getGrossAmount1() {
        return grossAmount1;
    }

    public void setGrossAmount1(int grossAmount1) {
        this.grossAmount1 = grossAmount1;
    }

    public int getNetAmount1() {
        return netAmount1;
    }

    public void setNetAmount1(int netAmount1) {
        this.netAmount1 = netAmount1;
    }

    public int getFromYear() {
        return fromYear;
    }

    public void setFromYear(int fromYear) {
        this.fromYear = fromYear;
    }

    public int getToYear() {
        return toYear;
    }

    public void setToYear(int toYear) {
        this.toYear = toYear;
    }

    public int getFromMonth() {
        return fromMonth;
    }

    public void setFromMonth(int fromMonth) {
        this.fromMonth = fromMonth;
    }

    public int getToMonth() {
        return toMonth;
    }

    public void setToMonth(int toMonth) {
        this.toMonth = toMonth;
    }

    public String getShowInAquitance() {
        return showInAquitance;
    }

    public void setShowInAquitance(String showInAquitance) {
        this.showInAquitance = showInAquitance;
    }

    public String getSubmitToTreasury() {
        return submitToTreasury;
    }

    public void setSubmitToTreasury(String submitToTreasury) {
        this.submitToTreasury = submitToTreasury;
    }

    public String getSltCOList() {
        return sltCOList;
    }

    public void setSltCOList(String sltCOList) {
        this.sltCOList = sltCOList;
    }

    public String getOffName() {
        return offName;
    }

    public void setOffName(String offName) {
        this.offName = offName;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getOfflvl() {
        return offlvl;
    }

    public void setOfflvl(String offlvl) {
        this.offlvl = offlvl;
    }

    

}
