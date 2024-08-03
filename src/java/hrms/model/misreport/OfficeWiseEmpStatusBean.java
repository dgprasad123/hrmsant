/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.misreport;

/**
 *
 * @author Manas
 */
public class OfficeWiseEmpStatusBean {

    private int generalmale;
    private int generalfemale;
    private int scmale;
    private int scfemale;
    private int obcmale;
    private int obcfemale;
    private int stmale;
    private int stfemale;
    private int sebcmale;
    private int sebcfemale;
    private int ucatnogender;
    private int uncatmale;
    private int uncatfemale;
    private int gentotal;
    private int sctotal;
    private int sttotal;
    private int sebctot;
    private int obctot;
    private int uncattotal;
    private int total;
    private String offcode = null;
    private String offen = null;
    private String deptcode = null;
    private int slno;
    private int totgenmale;

    public void setGeneralmale(int generalmale) {
        this.generalmale = generalmale;
    }

    public int getGeneralmale() {

        return generalmale;
    }

    public void setGeneralfemale(int generalfemale) {
        this.generalfemale = generalfemale;
    }

    public int getGeneralfemale() {
        return generalfemale;
    }

    public void setScmale(int scmale) {
        this.scmale = scmale;
    }

    public int getScmale() {
        return scmale;
    }

    public void setScfemale(int scfemale) {
        this.scfemale = scfemale;
    }

    public int getScfemale() {
        return scfemale;
    }

    public void setStmale(int stmale) {
        this.stmale = stmale;
    }

    public int getStmale() {
        return stmale;
    }

    public void setStfemale(int stfemale) {
        this.stfemale = stfemale;
    }

    public int getStfemale() {
        return stfemale;
    }

    public void setSebcmale(int sebcmale) {
        this.sebcmale = sebcmale;
    }

    public int getSebcmale() {
        return sebcmale;
    }

    public void setSebcfemale(int sebcfemale) {
        this.sebcfemale = sebcfemale;
    }

    public int getSebcfemale() {
        return sebcfemale;
    }

    public void setUcatnogender(int ucatnogender) {
        this.ucatnogender = ucatnogender;
    }

    public int getUcatnogender() {
        return ucatnogender;
    }

    public void setUncatmale(int uncatmale) {
        this.uncatmale = uncatmale;
    }

    public int getUncatmale() {
        return uncatmale;
    }

    public void setUncatfemale(int uncatfemale) {
        this.uncatfemale = uncatfemale;
    }

    public int getUncatfemale() {
        return uncatfemale;
    }

    public void setGentotal(int gentotal) {
        this.gentotal = gentotal;
    }

    public int getGentotal() {
        gentotal = generalmale + generalfemale;
        return gentotal;
    }

    public void setSctotal(int sctotal) {
        this.sctotal = sctotal;
    }

    public int getSctotal() {
        sctotal = scmale + scfemale;
        return sctotal;
    }

    public void setSttotal(int sttotal) {
        this.sttotal = sttotal;
    }

    public int getSttotal() {
        sttotal = stmale + stfemale;
        return sttotal;
    }

    public void setUncattotal(int uncattotal) {
        this.uncattotal = uncattotal;
    }

    public int getUncattotal() {
        uncattotal = uncatmale + uncatfemale + ucatnogender;
        return uncattotal;
    }

    public void setSebctot(int sebctot) {
        this.sebctot = sebctot;
    }

    public int getSebctot() {
        sebctot = sebcmale + sebcfemale;
        return sebctot;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        total = getUncattotal() + getGentotal() + getSctotal() + getSttotal() + getSebctot() + getObctot();
        return total;
    }

    public void setObctot(int obctot) {
        this.obctot = obctot;
    }

    public int getObctot() {
        obctot = obcmale + obcfemale;
        return obctot;
    }

    public void setObcmale(int obcmale) {
        this.obcmale = obcmale;
    }

    public int getObcmale() {
        return obcmale;
    }

    public void setObcfemale(int obcfemale) {
        this.obcfemale = obcfemale;
    }

    public int getObcfemale() {
        return obcfemale;
    }

    public void setOffcode(String offcode) {
        this.offcode = offcode;
    }

    public String getOffcode() {
        return offcode;
    }

    public void setOffen(String offen) {
        this.offen = offen;
    }

    public String getOffen() {
        return offen;
    }

    public void setSlno(int slno) {
        this.slno = slno;
    }

    public int getSlno() {
        return slno;
    }

    public void setTotgenmale(int totgenmale) {
        this.totgenmale = totgenmale;
    }

    public int getTotgenmale() {
        return totgenmale;
    }

    public void setDeptcode(String deptcode) {
        this.deptcode = deptcode;
    }

    public String getDeptcode() {
        return deptcode;
    }
}
