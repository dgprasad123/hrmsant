/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.IncomeTax;

import java.util.List;

/**
 *
 * @author Manoj PC
 */
public class AnnexureIBean {
    private String employerName = null;
    private String tanNo = null;
    private String bsrCode = null;
    private String voucherDate = null;
    private String challanNumber = null;
    private String challanAmount = null;
    private String totalTDS = null;
    private String totalInterest = null;
    private List dataList;

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getTanNo() {
        return tanNo;
    }

    public void setTanNo(String tanNo) {
        this.tanNo = tanNo;
    }

    public String getBsrCode() {
        return bsrCode;
    }

    public void setBsrCode(String bsrCode) {
        this.bsrCode = bsrCode;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getChallanNumber() {
        return challanNumber;
    }

    public void setChallanNumber(String challanNumber) {
        this.challanNumber = challanNumber;
    }

    public String getChallanAmount() {
        return challanAmount;
    }

    public void setChallanAmount(String challanAmount) {
        this.challanAmount = challanAmount;
    }

    public String getTotalTDS() {
        return totalTDS;
    }

    public void setTotalTDS(String totalTDS) {
        this.totalTDS = totalTDS;
    }

    public String getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(String totalInterest) {
        this.totalInterest = totalInterest;
    }

    public List getDataList() {
        return dataList;
    }

    public void setDataList(List dataList) {
        this.dataList = dataList;
    }
    
    
}
