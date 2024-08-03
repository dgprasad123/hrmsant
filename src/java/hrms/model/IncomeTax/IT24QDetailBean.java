/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.IncomeTax;

/**
 *
 * @author Manoj PC
 */
public class IT24QDetailBean {
    
    private String amount;
    private String bsrCode;
    private String challanNo;
    private String ddoSerial;
    private String dateDeposited;
    private String tdsAmount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBsrCode() {
        return bsrCode;
    }

    public void setBsrCode(String bsrCode) {
        this.bsrCode = bsrCode;
    }

    public String getChallanNo() {
        return challanNo;
    }

    public void setChallanNo(String challanNo) {
        this.challanNo = challanNo;
    }

    public String getDateDeposited() {
        return dateDeposited;
    }

    public void setDateDeposited(String dateDeposited) {
        this.dateDeposited = dateDeposited;
    }

    public String getDdoSerial() {
        return ddoSerial;
    }

    public void setDdoSerial(String ddoSerial) {
        this.ddoSerial = ddoSerial;
    }

    public String getTdsAmount() {
        return tdsAmount;
    }

    public void setTdsAmount(String tdsAmount) {
        this.tdsAmount = tdsAmount;
    }
    
}
