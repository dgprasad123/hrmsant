/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.Rent;

/**
 *
 * @author Manoj PC
 */
public class IfmsTransactionBean {
    private String challanRefNo = "";
    private String paymentMode = "";
    private String bankName = "";
    private String bankTransactionId = "";
    private String transactionstatus = "";
    private String transactionMsg = "";
    private String transactionTime = "";
    private String checksum = "";
    private String transactionAmount = "";

    public String getChallanRefNo() {
        return challanRefNo;
    }

    public void setChallanRefNo(String challanRefNo) {
        this.challanRefNo = challanRefNo;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankTransactionId() {
        return bankTransactionId;
    }

    public void setBankTransactionId(String bankTransactionId) {
        this.bankTransactionId = bankTransactionId;
    }

    public String getTransactionstatus() {
        return transactionstatus;
    }

    public void setTransactionstatus(String transactionstatus) {
        this.transactionstatus = transactionstatus;
    }

    public String getTransactionMsg() {
        return transactionMsg;
    }

    public void setTransactionMsg(String transactionMsg) {
        this.transactionMsg = transactionMsg;
    }


    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
    
}
