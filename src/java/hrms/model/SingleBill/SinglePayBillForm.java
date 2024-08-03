/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.SingleBill;

/**
 *
 * @author prashant
 */
public class SinglePayBillForm {
    
    private String billType;
    private String ddoName;
    private String sltYear;
    private String sltMonth;
    
    
    private String billNo;
    private String billDesc;
    private String billGroupDesc;
    private String billStatusId;
    private String typeOfBill;
    
    private String treasury;
    private String majorhead;
    private String tokenNo="";
    private String tokenDate="";
    private String voucherNo="";
    private String voucherDate="";
    
    private String arrearType;
    
    public String getDdoName() {
        return ddoName;
    }

    public void setDdoName(String ddoName) {
        this.ddoName = ddoName;
    }

    public String getSltYear() {
        return sltYear;
    }

    public void setSltYear(String sltYear) {
        this.sltYear = sltYear;
    }

    public String getSltMonth() {
        return sltMonth;
    }

    public void setSltMonth(String sltMonth) {
        this.sltMonth = sltMonth;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

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

    public String getBillGroupDesc() {
        return billGroupDesc;
    }

    public void setBillGroupDesc(String billGroupDesc) {
        this.billGroupDesc = billGroupDesc;
    }

    public String getBillStatusId() {
        return billStatusId;
    }

    public void setBillStatusId(String billStatusId) {
        this.billStatusId = billStatusId;
    }

    public String getTypeOfBill() {
        return typeOfBill;
    }

    public void setTypeOfBill(String typeOfBill) {
        this.typeOfBill = typeOfBill;
    }

    public String getTreasury() {
        return treasury;
    }

    public void setTreasury(String treasury) {
        this.treasury = treasury;
    }

    public String getMajorhead() {
        return majorhead;
    }

    public void setMajorhead(String majorhead) {
        this.majorhead = majorhead;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getTokenNo() {
        return tokenNo;
    }

    public void setTokenNo(String tokenNo) {
        this.tokenNo = tokenNo;
    }

    public String getTokenDate() {
        return tokenDate;
    }

    public void setTokenDate(String tokenDate) {
        this.tokenDate = tokenDate;
    }

    public String getArrearType() {
        return arrearType;
    }

    public void setArrearType(String arrearType) {
        this.arrearType = arrearType;
    }
}
