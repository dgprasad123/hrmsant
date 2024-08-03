/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.employee;

/**
 *
 * @author Manas
 */
public class QuaterAllotment {
    private int qsid;
    private int qaid;
    private int amt;    
    private String btid;
    private String isgethra;
    private String adcode;
    private String addesc;
    private String adcodename;
    private String adtype;
    private String alunit;
    private String dedtype;
    private String schedule;
    private int repcol;
    private int rowno;
    
    
    public int getQsid() {
        return qsid;
    }

    public void setQsid(int qsid) {
        this.qsid = qsid;
    }

    public int getQaid() {
        return qaid;
    }

    public void setQaid(int qaid) {
        this.qaid = qaid;
    }

    public int getAmt() {
        return amt;
    }

    public void setAmt(int amt) {
        this.amt = amt;
    }    

    public String getBtid() {
        return btid;
    }

    public void setBtid(String btid) {
        this.btid = btid;
    }

    public String getIsgethra() {
        return isgethra;
    }

    public void setIsgethra(String isgethra) {
        this.isgethra = isgethra;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getAddesc() {
        return addesc;
    }

    public void setAddesc(String addesc) {
        this.addesc = addesc;
    }

    public String getAdcodename() {
        return adcodename;
    }

    public void setAdcodename(String adcodename) {
        this.adcodename = adcodename;
    }

    public String getAdtype() {
        return adtype;
    }

    public void setAdtype(String adtype) {
        this.adtype = adtype;
    }

    public String getAlunit() {
        return alunit;
    }

    public void setAlunit(String alunit) {
        this.alunit = alunit;
    }

    public String getDedtype() {
        return dedtype;
    }

    public void setDedtype(String dedtype) {
        this.dedtype = dedtype;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public int getRepcol() {
        return repcol;
    }

    public void setRepcol(int repcol) {
        this.repcol = repcol;
    }

    public int getRowno() {
        return rowno;
    }

    public void setRowno(int rowno) {
        this.rowno = rowno;
    }
    
}
