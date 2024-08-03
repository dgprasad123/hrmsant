/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.billbrowser;

import java.util.Date;

/**
 *
 * @author Manas Jena
 */
public class BillBean {

    private String billdate = null;
    private String billtype = null;
    private String billdesc = null;
    private String billGroupDesc = null;
    private String majorhead = null;
    private String subMajorHead = null;
    private String minorHead = null;
    private String subMinorHead1 = null;
    private String subMinorHead2 = null;
    private String subMinorHead3 = null;
    private String voucherno = null;
    private String voucherdate = null;
    private String vouchermonth = null;
    private String showLink = null;
    private int voucherDay = 0;
    private int billMonth = 0;
    private int billYear = 0;
    private int nooofEmployee = 0;

    private String finyear = null;
    private String onlinebillapproved = null;
    private int lockBill = 0;
    private String billgroupId = "";
    private String isbillPrepared = "";

    private String treasurycode = null;
    private String ddocode = null;

    private String billno = null;
    private int adjYear = 0;
    private String percentageArraer = "";

    private int ftcount = 0;
    private String isbillVerified = null;
    private String sltbillgroupId = null;

    private String checkESignStatus = "";
    private String frontpageslno;
    private int billGross = 0;
    private int billDed = 0;
    private int billNet = 0;
    private String billmonthNm = null;
    private String aqslno = null;
    private String incrementReason = null;
    private int curbasic = 0;
    private int incrBasic = 0;
    private String hasIncrement="N";
    private String hidempid=null;
    private String nodummypran;
    private String noteligible;
    private String selectedBilltype;
    private String hidBillGrpId;
    private String officeCode;
   


    public int getBillMonth() {
        return billMonth;
    }

    public void setBillMonth(int billMonth) {
        this.billMonth = billMonth;
    }

    public int getBillYear() {
        return billYear;
    }

    public void setBillYear(int billYear) {
        this.billYear = billYear;
    }

    public int getAdjYear() {
        return adjYear;
    }

    public void setAdjYear(int adjYear) {
        this.adjYear = adjYear;
    }

    public String getBilldate() {
        return billdate;
    }

    public void setBilldate(String billdate) {
        this.billdate = billdate;
    }

    public String getBilltype() {
        return billtype;
    }

    public void setBilltype(String billtype) {
        this.billtype = billtype;
    }

    public String getBilldesc() {
        return billdesc;
    }

    public void setBilldesc(String billdesc) {
        this.billdesc = billdesc;
    }

    public String getBillGroupDesc() {
        return billGroupDesc;
    }

    public void setBillGroupDesc(String billGroupDesc) {
        this.billGroupDesc = billGroupDesc;
    }

    public String getVouchermonth() {
        return vouchermonth;
    }

    public void setVouchermonth(String vouchermonth) {
        this.vouchermonth = vouchermonth;
    }

    public String getVoucherno() {
        return voucherno;
    }

    public void setVoucherno(String voucherno) {
        this.voucherno = voucherno;
    }

    public String getVoucherdate() {
        return voucherdate;
    }

    public void setVoucherdate(String voucherdate) {
        this.voucherdate = voucherdate;
    }

    public String getShowLink() {
        return showLink;
    }

    public void setShowLink(String showLink) {
        this.showLink = showLink;
    }

    public String getFinyear() {
        return finyear;
    }

    public void setFinyear(String finyear) {
        this.finyear = finyear;
    }

    public String getOnlinebillapproved() {
        return onlinebillapproved;
    }

    public void setOnlinebillapproved(String onlinebillapproved) {
        this.onlinebillapproved = onlinebillapproved;
    }

    public int getLockBill() {
        return lockBill;
    }

    public void setLockBill(int lockBill) {
        this.lockBill = lockBill;
    }

    public String getBillgroupId() {
        return billgroupId;
    }

    public void setBillgroupId(String billgroupId) {
        this.billgroupId = billgroupId;
    }

    public String getIsbillPrepared() {
        return isbillPrepared;
    }

    public void setIsbillPrepared(String isbillPrepared) {
        this.isbillPrepared = isbillPrepared;
    }

    public String getTreasurycode() {
        return treasurycode;
    }

    public void setTreasurycode(String treasurycode) {
        this.treasurycode = treasurycode;
    }

    public String getDdocode() {
        return ddocode;
    }

    public void setDdocode(String ddocode) {
        this.ddocode = ddocode;
    }

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }

    public String getMajorhead() {
        return majorhead;
    }

    public void setMajorhead(String majorhead) {
        this.majorhead = majorhead;
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

    public String getSubMinorHead1() {
        return subMinorHead1;
    }

    public void setSubMinorHead1(String subMinorHead1) {
        this.subMinorHead1 = subMinorHead1;
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

    public int getVoucherDay() {
        return voucherDay;
    }

    public void setVoucherDay(int voucherDay) {
        this.voucherDay = voucherDay;
    }

    public String getPercentageArraer() {
        return percentageArraer;
    }

    public void setPercentageArraer(String percentageArraer) {
        this.percentageArraer = percentageArraer;
    }

    public int getNooofEmployee() {
        return nooofEmployee;
    }

    public void setNooofEmployee(int nooofEmployee) {
        this.nooofEmployee = nooofEmployee;
    }

    public int getFtcount() {
        return ftcount;
    }

    public void setFtcount(int ftcount) {
        this.ftcount = ftcount;
    }

    public String getIsbillVerified() {
        return isbillVerified;
    }

    public void setIsbillVerified(String isbillVerified) {
        this.isbillVerified = isbillVerified;
    }

    public String getSltbillgroupId() {
        return sltbillgroupId;
    }

    public void setSltbillgroupId(String sltbillgroupId) {
        this.sltbillgroupId = sltbillgroupId;
    }

    public String getCheckESignStatus() {
        return checkESignStatus;
    }

    public void setCheckESignStatus(String checkESignStatus) {
        this.checkESignStatus = checkESignStatus;
    }

    public String getFrontpageslno() {
        return frontpageslno;
    }

    public void setFrontpageslno(String frontpageslno) {
        this.frontpageslno = frontpageslno;
    }

    public int getBillGross() {
        return billGross;
    }

    public void setBillGross(int billGross) {
        this.billGross = billGross;
    }

    public int getBillDed() {
        return billDed;
    }

    public void setBillDed(int billDed) {
        this.billDed = billDed;
    }

    public int getBillNet() {
        return billNet;
    }

    public void setBillNet(int billNet) {
        this.billNet = billNet;
    }

    public String getBillmonthNm() {
        return billmonthNm;
    }

    public void setBillmonthNm(String billmonthNm) {
        this.billmonthNm = billmonthNm;
    }

    public String getAqslno() {
        return aqslno;
    }

    public void setAqslno(String aqslno) {
        this.aqslno = aqslno;
    }

    public String getIncrementReason() {
        return incrementReason;
    }

    public void setIncrementReason(String incrementReason) {
        this.incrementReason = incrementReason;
    }

    public int getCurbasic() {
        return curbasic;
    }

    public void setCurbasic(int curbasic) {
        this.curbasic = curbasic;
    }

    public int getIncrBasic() {
        return incrBasic;
    }

    public void setIncrBasic(int incrBasic) {
        this.incrBasic = incrBasic;
    }

    public String getHasIncrement() {
        return hasIncrement;
    }

    public void setHasIncrement(String hasIncrement) {
        this.hasIncrement = hasIncrement;
    }

    public String getHidempid() {
        return hidempid;
    }

    public void setHidempid(String hidempid) {
        this.hidempid = hidempid;
    }

    public String getNodummypran() {
        return nodummypran;
    }

    public void setNodummypran(String nodummypran) {
        this.nodummypran = nodummypran;
    }

    public String getNoteligible() {
        return noteligible;
    }

    public void setNoteligible(String noteligible) {
        this.noteligible = noteligible;
    }

    public String getSelectedBilltype() {
        return selectedBilltype;
    }

    public void setSelectedBilltype(String selectedBilltype) {
        this.selectedBilltype = selectedBilltype;
    }

    public String getHidBillGrpId() {
        return hidBillGrpId;
    }

    public void setHidBillGrpId(String hidBillGrpId) {
        this.hidBillGrpId = hidBillGrpId;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

   
    

}
