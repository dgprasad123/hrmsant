/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.employee;

/**
 *
 * @author Manas Jena
 */
public class Punishment {
    private String note;
    private String ordno;
    private String orddate;
    private String durfrom;
    private String durto;
    private String days;
    private String reason;
    private String ifpayheldup;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOrdno() {
        return ordno;
    }

    public void setOrdno(String ordno) {
        this.ordno = ordno;
    }

    public String getOrddate() {
        return orddate;
    }

    public void setOrddate(String orddate) {
        this.orddate = orddate;
    }

    public String getDurfrom() {
        return durfrom;
    }

    public void setDurfrom(String durfrom) {
        this.durfrom = durfrom;
    }

    public String getDurto() {
        return durto;
    }

    public void setDurto(String durto) {
        this.durto = durto;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getIfpayheldup() {
        return ifpayheldup;
    }

    public void setIfpayheldup(String ifpayheldup) {
        this.ifpayheldup = ifpayheldup;
    }
    
    
}
