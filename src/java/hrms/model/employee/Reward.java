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
public class Reward {
    private String rwdlvl;
    private String note;
    private String ordno;
    private String orddate;
    private String offcode;

    public String getRwdlvl() {
        return rwdlvl;
    }

    public void setRwdlvl(String rwdlvl) {
        this.rwdlvl = rwdlvl;
    }

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

    public String getOffcode() {
        return offcode;
    }

    public void setOffcode(String offcode) {
        this.offcode = offcode;
    }
    
    
}
