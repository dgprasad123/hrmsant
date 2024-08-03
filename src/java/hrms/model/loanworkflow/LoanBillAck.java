/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.loanworkflow;

/**
 *
 * @author lenovo
 */
public class LoanBillAck {

    private boolean status;
    private String data;
    private String rek;
    private String hmac;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getRek() {
        return rek;
    }

    public void setRek(String rek) {
        this.rek = rek;
    }

    public String getHmac() {
        return hmac;
    }

    public void setHmac(String hmac) {
        this.hmac = hmac;
    }

}
