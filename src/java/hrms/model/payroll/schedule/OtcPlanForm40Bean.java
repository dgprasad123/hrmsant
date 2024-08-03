/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.payroll.schedule;

import java.util.ArrayList;

public class OtcPlanForm40Bean extends ScheduleHelper{
    
    private String billNo=null;
    private String billDesc=null;
    private String billDate=null;
    private String ddoName=null;
    private String billYear=null;
    private String officeName=null;
    private String tanNo=null;
    private String billMonth=null;
    
    private String benRefNo=null;
    private String otcCode = null;
    private String otcStatus=null;
    private String treasuryName=null;
    
 //Head of Account
    private String demandNo=null;
    private String majorHead=null;
    private String subMajorHead=null;
    private String minorHead=null;
    private String subMinorHead=null;
    private String subMinorHead2 = null;
    private String subMinorHead3 = null;
    private String grossAmountWord=null;
    private String ddoCode=null;
    private String token=null;
    
    private ArrayList deductionList=null;
    private ArrayList allowanceList=null;
    
    private String netAmount=null;
    private String grossTot=null;
    private String deductTot = null;
    private String netAmtUnder=null;
    private String netAmountWord=null;
    private String netAmtUnderWord=null;
    private String basicPlusGp=null;
    
    private String totalDa=null;
    private String totalGpf=null;
    private String grandTotinWord=null;
    
    
    private String payhead = null;
    private String dahead = null;
    
    private int adAmt;
    private String btId=null;
    private int gpAmt;
    private String nowDedn=null;
    private String adCode=null;
    
    
    
    
    
    
    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillDesc() {
        return billDesc;
    }

    public void setBillDesc(String billDesc) {
        this.billDesc = billDesc;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getDdoName() {
        return ddoName;
    }

    public void setDdoName(String ddoName) {
        this.ddoName = ddoName;
    }

    public String getBillYear() {
        return billYear;
    }

    public void setBillYear(String billYear) {
        this.billYear = billYear;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getTanNo() {
        return tanNo;
    }

    public void setTanNo(String tanNo) {
        this.tanNo = tanNo;
    }

    public String getBillMonth() {
        return billMonth;
    }

    public void setBillMonth(String billMonth) {
        this.billMonth = billMonth;
    }

    public String getBenRefNo() {
        return benRefNo;
    }

    public void setBenRefNo(String benRefNo) {
        this.benRefNo = benRefNo;
    }

    public String getOtcCode() {
        return otcCode;
    }

    public void setOtcCode(String otcCode) {
        this.otcCode = otcCode;
    }

    public String getOtcStatus() {
        return otcStatus;
    }

    public void setOtcStatus(String otcStatus) {
        this.otcStatus = otcStatus;
    }

    public String getTreasuryName() {
        return treasuryName;
    }

    public void setTreasuryName(String treasuryName) {
        this.treasuryName = treasuryName;
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

    public String getMinorHead() {
        return minorHead;
    }

    public void setMinorHead(String minorHead) {
        this.minorHead = minorHead;
    }

    public String getSubMinorHead() {
        return subMinorHead;
    }

    public void setSubMinorHead(String subMinorHead) {
        this.subMinorHead = subMinorHead;
    }

    public String getSubMinorHead2() {
        return subMinorHead2;
    }

    public void setSubMinorHead2(String subMinorHead2) {
        this.subMinorHead2 = subMinorHead2;
    }

    public String getSubMinorHead3() {
        return subMinorHead3;
    }

    public void setSubMinorHead3(String subMinorHead3) {
        this.subMinorHead3 = subMinorHead3;
    }

    public String getGrossAmountWord() {
        return grossAmountWord;
    }

    public void setGrossAmountWord(String grossAmountWord) {
        this.grossAmountWord = grossAmountWord;
    }

    public String getDdoCode() {
        return ddoCode;
    }

    public void setDdoCode(String ddoCode) {
        this.ddoCode = ddoCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPayhead() {
        return payhead;
    }

    public void setPayhead(String payhead) {
        this.payhead = payhead;
    }

    public String getDahead() {
        return dahead;
    }

    public void setDahead(String dahead) {
        this.dahead = dahead;
    }

    public ArrayList getDeductionList() {
        return deductionList;
    }

    public void setDeductionList(ArrayList deductionList) {
        this.deductionList = deductionList;
    }

    public ArrayList getAllowanceList() {
        return allowanceList;
    }

    public void setAllowanceList(ArrayList allowanceList) {
        this.allowanceList = allowanceList;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getGrossTot() {
        return grossTot;
    }

    public void setGrossTot(String grossTot) {
        this.grossTot = grossTot;
    }

    public String getNetAmtUnder() {
        return netAmtUnder;
    }

    public void setNetAmtUnder(String netAmtUnder) {
        this.netAmtUnder = netAmtUnder;
    }

    public String getNetAmountWord() {
        return netAmountWord;
    }

    public void setNetAmountWord(String netAmountWord) {
        this.netAmountWord = netAmountWord;
    }

    public String getNetAmtUnderWord() {
        return netAmtUnderWord;
    }

    public void setNetAmtUnderWord(String netAmtUnderWord) {
        this.netAmtUnderWord = netAmtUnderWord;
    }

    public String getBasicPlusGp() {
        return basicPlusGp;
    }

    public void setBasicPlusGp(String basicPlusGp) {
        this.basicPlusGp = basicPlusGp;
    }

    public String getTotalDa() {
        return totalDa;
    }

    public void setTotalDa(String totalDa) {
        this.totalDa = totalDa;
    }

    public String getTotalGpf() {
        return totalGpf;
    }

    public void setTotalGpf(String totalGpf) {
        this.totalGpf = totalGpf;
    }

    public String getGrandTotinWord() {
        return grandTotinWord;
    }

    public void setGrandTotinWord(String grandTotinWord) {
        this.grandTotinWord = grandTotinWord;
    }

    
    public int getGpAmt() {
        return gpAmt;
    }

    public void setGpAmt(int gpAmt) {
        this.gpAmt = gpAmt;
    }

    public String getNowDedn() {
        return nowDedn;
    }

    public void setNowDedn(String nowDedn) {
        this.nowDedn = nowDedn;
    }

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public int getAdAmt() {
        return adAmt;
    }

    public void setAdAmt(int adAmt) {
        this.adAmt = adAmt;
    }

    public String getBtId() {
        return btId;
    }

    public void setBtId(String btId) {
        this.btId = btId;
    }

    public String getDeductTot() {
        return deductTot;
    }

    public void setDeductTot(String deductTot) {
        this.deductTot = deductTot;
    }

    

    
    
    
    
}
