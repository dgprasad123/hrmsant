/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payfixation;

/**
 *
 * @author lenovo pc
 */
public class PayFixationList {

    private String notId = null;
    private String notType = null;
    private String doe = null;
    private String ordno = null;
    private String ordDate = null;
    
    private String gradePay;
    private String revPayScale;
    private String curbasic;
    private String wefDate;
    private String wefTime;
    private String dateOfNextIncrement;
    
    private String isValidated;
    
    private String canceltype;
    private String cancelnotid;
    
    public String getNotId() {
        return notId;
    }

    public void setNotId(String notId) {
        this.notId = notId;
    }

    public String getNotType() {
        return notType;
    }

    public void setNotType(String notType) {
        this.notType = notType;
    }

    public String getDoe() {
        return doe;
    }

    public void setDoe(String doe) {
        this.doe = doe;
    }

    public String getOrdno() {
        return ordno;
    }

    public void setOrdno(String ordno) {
        this.ordno = ordno;
    }

    public String getOrdDate() {
        return ordDate;
    }

    public void setOrdDate(String ordDate) {
        this.ordDate = ordDate;
    }

    public String getGradePay() {
        return gradePay;
    }

    public void setGradePay(String gradePay) {
        this.gradePay = gradePay;
    }

    public String getRevPayScale() {
        return revPayScale;
    }

    public void setRevPayScale(String revPayScale) {
        this.revPayScale = revPayScale;
    }

    public String getCurbasic() {
        return curbasic;
    }

    public void setCurbasic(String curbasic) {
        this.curbasic = curbasic;
    }

    public String getWefDate() {
        return wefDate;
    }

    public void setWefDate(String wefDate) {
        this.wefDate = wefDate;
    }

    public String getWefTime() {
        return wefTime;
    }

    public void setWefTime(String wefTime) {
        this.wefTime = wefTime;
    }

    public String getDateOfNextIncrement() {
        return dateOfNextIncrement;
    }

    public void setDateOfNextIncrement(String dateOfNextIncrement) {
        this.dateOfNextIncrement = dateOfNextIncrement;
    }

    public String getIsValidated() {
        return isValidated;
    }

    public void setIsValidated(String isValidated) {
        this.isValidated = isValidated;
    }

    public String getCanceltype() {
        return canceltype;
    }

    public void setCanceltype(String canceltype) {
        this.canceltype = canceltype;
    }

    public String getCancelnotid() {
        return cancelnotid;
    }

    public void setCancelnotid(String cancelnotid) {
        this.cancelnotid = cancelnotid;
    }

}
