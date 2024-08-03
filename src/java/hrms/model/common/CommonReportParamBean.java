package hrms.model.common;

import java.math.BigDecimal;

public class CommonReportParamBean {

    private String billdesc;
    private String billgroupDesc;
    private BigDecimal billgroupid;
    private String billNo;
    private String ddoname;
    private String officename;
    private String officeen = null;
    private String offcode = null;
    private String deptname;
    private int aqyear;
    private int aqmonth;
    private String aqMonthAsName = null;
    private String statename;
    private String district;
    private String billdate = null;
    private String ddoregno = null;
    private String tanno = null;
    private String dtoregno = null;
    private String fabtid = null;
    private String ddocode = null;
    private String treasuryname = null;
    private String branchname = null;
    private String branchmanager = null;
    private String pacode = null;
    private String suffix = null;
    private String typeofBill=null;
    private String billType=null;
    private int billStatusId; 
    private int fromMonth;
    private int fromYear;
    private int toMonth;
    private int toYear;
    
    private int billGrossAmt;
    private int billDedAmt;
    private int billPvtDedAmt;
    
    private String vchNo = null;
    private String vchDate = null;
    
    private String requiredReports = null;
                            
    private String benRefNo;
    
    private int percentageArraer;
    private String poffcode=null;
    
    private String ddoName;
    
    public String getTypeofBill() {
        return typeofBill;
    }

    public void setTypeofBill(String typeofBill) {
        this.typeofBill = typeofBill;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }
   
    public String getBilldesc() {
        return billdesc;
    }

    public void setBilldesc(String billdesc) {
        this.billdesc = billdesc;
    }

    public String getBillgroupDesc() {
        return billgroupDesc;
    }

    public void setBillgroupDesc(String billgroupDesc) {
        this.billgroupDesc = billgroupDesc;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public BigDecimal getBillgroupid() {
        return billgroupid;
    }

    public void setBillgroupid(BigDecimal billgroupid) {
        this.billgroupid = billgroupid;
    }

    public String getDdoname() {
        return ddoname;
    }

    public void setDdoname(String ddoname) {
        this.ddoname = ddoname;
    }

    public String getOfficename() {
        return officename;
    }

    public void setOfficename(String officename) {
        this.officename = officename;
    }

    public String getOfficeen() {
        return officeen;
    }

    public void setOfficeen(String officeen) {
        this.officeen = officeen;
    }

    public String getOffcode() {
        return offcode;
    }

    public void setOffcode(String offcode) {
        this.offcode = offcode;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public int getAqyear() {
        return aqyear;
    }

    public void setAqyear(int aqyear) {
        this.aqyear = aqyear;
    }

    
    public int getAqmonth() {
        return aqmonth;
    }

    public void setAqmonth(int aqmonth) {
        this.aqmonth = aqmonth;
    }

    public String getStatename() {
        return statename;
    }

    public void setStatename(String statename) {
        this.statename = statename;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getBilldate() {
        return billdate;
    }

    public void setBilldate(String billdate) {
        this.billdate = billdate;
    }

    public String getDdoregno() {
        return ddoregno;
    }

    public void setDdoregno(String ddoregno) {
        this.ddoregno = ddoregno;
    }

    public String getTanno() {
        return tanno;
    }

    public void setTanno(String tanno) {
        this.tanno = tanno;
    }

    public String getDtoregno() {
        return dtoregno;
    }

    public void setDtoregno(String dtoregno) {
        this.dtoregno = dtoregno;
    }

    public String getFabtid() {
        return fabtid;
    }

    public void setFabtid(String fabtid) {
        this.fabtid = fabtid;
    }

    public String getDdocode() {
        return ddocode;
    }

    public void setDdocode(String ddocode) {
        this.ddocode = ddocode;
    }

    public String getTreasuryname() {
        return treasuryname;
    }

    public void setTreasuryname(String treasuryname) {
        this.treasuryname = treasuryname;
    }

    public String getBranchname() {
        return branchname;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }

    public String getBranchmanager() {
        return branchmanager;
    }

    public void setBranchmanager(String branchmanager) {
        this.branchmanager = branchmanager;
    }

    public String getPacode() {
        return pacode;
    }

    public void setPacode(String pacode) {
        this.pacode = pacode;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public int getBillStatusId() {
        return billStatusId;
    }

    public void setBillStatusId(int billStatusId) {
        this.billStatusId = billStatusId;
    }

    public int getBillGrossAmt() {
        return billGrossAmt;
    }

    public void setBillGrossAmt(int billGrossAmt) {
        this.billGrossAmt = billGrossAmt;
    }

    public int getBillDedAmt() {
        return billDedAmt;
    }

    public void setBillDedAmt(int billDedAmt) {
        this.billDedAmt = billDedAmt;
    }

    public int getBillPvtDedAmt() {
        return billPvtDedAmt;
    }

    public void setBillPvtDedAmt(int billPvtDedAmt) {
        this.billPvtDedAmt = billPvtDedAmt;
    }

    public String getAqMonthAsName() {
        return aqMonthAsName;
    }

    public void setAqMonthAsName(String aqMonthAsName) {
        this.aqMonthAsName = aqMonthAsName;
    }

    public String getVchNo() {
        return vchNo;
    }

    public void setVchNo(String vchNo) {
        this.vchNo = vchNo;
    }

    public String getVchDate() {
        return vchDate;
    }

    public void setVchDate(String vchDate) {
        this.vchDate = vchDate;
    }

    public int getFromMonth() {
        return fromMonth;
    }

    public void setFromMonth(int fromMonth) {
        this.fromMonth = fromMonth;
    }

    public int getFromYear() {
        return fromYear;
    }

    public void setFromYear(int fromYear) {
        this.fromYear = fromYear;
    }

    public int getToMonth() {
        return toMonth;
    }

    public void setToMonth(int toMonth) {
        this.toMonth = toMonth;
    }

    public int getToYear() {
        return toYear;
    }

    public void setToYear(int toYear) {
        this.toYear = toYear;
    }

    public String getRequiredReports() {
        return requiredReports;
    }

    public void setRequiredReports(String requiredReports) {
        this.requiredReports = requiredReports;
    }

    public String getBenRefNo() {
        return benRefNo;
    }

    public void setBenRefNo(String benRefNo) {
        this.benRefNo = benRefNo;
    }

    public int getPercentageArraer() {
        return percentageArraer;
    }

    public void setPercentageArraer(int percentageArraer) {
        this.percentageArraer = percentageArraer;
    }

    public String getPoffcode() {
        return poffcode;
    }

    public void setPoffcode(String poffcode) {
        this.poffcode = poffcode;
    }

    public String getDdoName() {
        return ddoName;
    }

    public void setDdoName(String ddoName) {
        this.ddoName = ddoName;
    }
}
