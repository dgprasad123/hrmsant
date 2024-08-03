/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.payroll.schedule;

import java.util.List;

public class ComputerTokenReportBean extends ScheduleHelper{
 
    private String billNo = null;
    private String billDate = null;
    private String billType = null;
    private String billDesc = null;
    private String ddoCode = null;
    private String ddoName = null;
    private String demandNo = null;
    private String majorHead = null;
    private String subMajorHead = null;
    private String minor=null;
    private String sub = null;
    private String detail = null;
    private String planOrNonPlan = null;
    private String chargedOrVoted = null;
    private String sector = null;
    private String sectorDesc = null;
    private String treasuryName = null;
    private String tokenNo = null;
    private String grossAmt=null;
    private String netAmt=null;
    private double byTransferAmt=0;
    private String benRefNo = null;
        
    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getBillDesc() {
        return billDesc;
    }

    public void setBillDesc(String billDesc) {
        this.billDesc = billDesc;
    }

    public String getDdoCode() {
        return ddoCode;
    }

    public void setDdoCode(String ddoCode) {
        this.ddoCode = ddoCode;
    }

    public String getDdoName() {
        return ddoName;
    }

    public void setDdoName(String ddoName) {
        this.ddoName = ddoName;
    }

    public String getDemandNo() {
        return demandNo;
    }

    public void setDemandNo(String demandNo) {
        this.demandNo = demandNo;
    }

    public String getMajorHead() {
        return majorHead;
    }

    public void setMajorHead(String majorHead) {
        this.majorHead = majorHead;
    }

    public String getSubMajorHead() {
        return subMajorHead;
    }

    public void setSubMajorHead(String subMajorHead) {
        this.subMajorHead = subMajorHead;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPlanOrNonPlan() {
        return planOrNonPlan;
    }

    public void setPlanOrNonPlan(String planOrNonPlan) {
        this.planOrNonPlan = planOrNonPlan;
    }

    public String getChargedOrVoted() {
        return chargedOrVoted;
    }

    public void setChargedOrVoted(String chargedOrVoted) {
        this.chargedOrVoted = chargedOrVoted;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getSectorDesc() {
        return sectorDesc;
    }

    public void setSectorDesc(String sectorDesc) {
        this.sectorDesc = sectorDesc;
    }

    public String getTreasuryName() {
        return treasuryName;
    }

    public void setTreasuryName(String treasuryName) {
        this.treasuryName = treasuryName;
    }

    public String getTokenNo() {
        return tokenNo;
    }

    public void setTokenNo(String tokenNo) {
        this.tokenNo = tokenNo;
    }

    public String getGrossAmt() {
        return grossAmt;
    }

    public void setGrossAmt(String grossAmt) {
        this.grossAmt = grossAmt;
    }

    public String getNetAmt() {
        return netAmt;
    }

    public void setNetAmt(String netAmt) {
        this.netAmt = netAmt;
    }

    public double getByTransferAmt() {
        return byTransferAmt;
    }

    public void setByTransferAmt(double byTransferAmt) {
        this.byTransferAmt = byTransferAmt;
    }

    public String getBenRefNo() {
        return benRefNo;
    }

    public void setBenRefNo(String benRefNo) {
        this.benRefNo = benRefNo;
    }   
    
}
