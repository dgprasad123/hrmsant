/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.billbrowser;

import java.util.List;

/**
 *
 * @author Surendra
 */
public class GetBillStatusBean {
    
    private String billId;
    private String billno;
    private String billdate;
    private String voucherno;
    private String voucherdate;
    private String tokenno;
    private String tokendate;
    private String treasurysubmissiondate;
    private String billstatus;
    private String paymentadvicedate;
    private String billerror=null;
    private List errMsg=null;
    
    public void setBillno(String billno) {
        this.billno = billno;
    }

    public String getBillno() {
        return billno;
    }

    public void setBilldate(String billdate) {
        this.billdate = billdate;
    }

    public String getBilldate() {
        return billdate;
    }

    public void setVoucherno(String voucherno) {
        this.voucherno = voucherno;
    }

    public String getVoucherno() {
        return voucherno;
    }

    public void setVoucherdate(String voucherdate) {
        this.voucherdate = voucherdate;
    }

    public String getVoucherdate() {
        return voucherdate;
    }

    public void setTokenno(String tokenno) {
        this.tokenno = tokenno;
    }

    public String getTokenno() {
        return tokenno;
    }

    public void setTokendate(String tokendate) {
        this.tokendate = tokendate;
    }

    public String getTokendate() {
        return tokendate;
    }

    public void setTreasurysubmissiondate(String treasurysubmissiondate) {
        this.treasurysubmissiondate = treasurysubmissiondate;
    }

    public String getTreasurysubmissiondate() {
        return treasurysubmissiondate;
    }

    public void setBillstatus(String billstatus) {
        this.billstatus = billstatus;
    }

    public String getBillstatus() {
        return billstatus;
    }

    public void setPaymentadvicedate(String paymentadvicedate) {
        this.paymentadvicedate = paymentadvicedate;
    }

    public String getPaymentadvicedate() {
        return paymentadvicedate;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillerror(String billerror) {
        this.billerror = billerror;
    }

    public String getBillerror() {
        return billerror;
    }

    public List getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(List errMsg) {
        this.errMsg = errMsg;
    }

    

}
