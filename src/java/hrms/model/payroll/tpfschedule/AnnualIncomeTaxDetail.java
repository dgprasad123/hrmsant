/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.tpfschedule;

/**
 *
 * @author Manas
 */
public class AnnualIncomeTaxDetail {

    private String aqslno;
    private int month;
    private int year;
    private String billDesc;
    private int curbasic;
    private int gp = 0;
    private int ir = 0;
    private int da = 0;
    private int hra = 0;
    private int oa = 0;
    private int ot = 0;
    private int gross = 0;
    private int gpf = 0;
    private int pt = 0;
    private int it = 0;
    private int lic = 0;
    private int hrr = 0;
    private int wrr = 0;
    private int mopaap = 0;
    private int mopaai = 0;
    private int mcyp = 0;
    private int mcyi = 0;
    private int hc = 0;
    private int compadv = 0;
    private int gis = 0;
    private int gpfadv = 0;
    private int fa = 0;
    private int hbap = 0;
    private int hbai = 0;
    private int loan = 0;
    private int totalded = 0;

    public String getAqslno() {
        return aqslno;
    }

    public void setAqslno(String aqslno) {
        this.aqslno = aqslno;
    }

    public int getCurbasic() {
        return curbasic;
    }

    public void setCurbasic(int curbasic) {
        this.curbasic = curbasic;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getBillDesc() {
        return billDesc;
    }

    public void setBillDesc(String billDesc) {
        this.billDesc = billDesc;
    }

    public int getDa() {
        return da;
    }

    public void setDa(int da) {
        this.da = da;
    }

    public int getHra() {
        return hra;
    }

    public void setHra(int hra) {
        this.hra = hra;
    }

    public int getOa() {
        return oa;
    }

    public void setOa(int oa) {
        this.oa = this.oa + oa;
    }

    public int getGp() {
        return gp;
    }

    public void setGp(int gp) {
        this.gp = this.gp + gp;
    }

    public int getIr() {
        return ir;
    }

    public void setIr(int ir) {
        this.ir = ir;
    }

    public int getOt() {
        return ot;
    }

    public void setOt(int ot) {
        this.ot = ot;
    }

    public int getGross() {
        this.gross = this.curbasic + this.ot + this.ir + this.gp + this.oa + this.hra + this.da;
        return gross;
    }

    public void setGross(int gross) {
        this.gross = gross;
    }

    public int getGpf() {
        return gpf;
    }

    public void setGpf(int gpf) {
        this.gpf = gpf;
    }

    public int getPt() {
        return pt;
    }

    public void setPt(int pt) {
        this.pt = pt;
    }

    public int getIt() {
        return it;
    }

    public void setIt(int it) {
        this.it = it;
    }

    public int getLic() {
        return lic;
    }

    public void setLic(int lic) {
        this.lic = this.lic + lic;
    }

    public int getHrr() {
        return hrr;
    }

    public void setHrr(int hrr) {
        this.hrr = hrr;
    }

    public int getWrr() {
        return wrr;
    }

    public void setWrr(int wrr) {
        this.wrr = wrr;
    }

    public int getMopaap() {
        return mopaap;
    }

    public void setMopaap(int mopaap) {
        this.mopaap = this.mopaap + mopaap;
    }

    public int getMopaai() {
        return mopaai;
    }

    public void setMopaai(int mopaai) {
        this.mopaai = this.mopaai + mopaai;
    }

    public int getMcyp() {
        return mcyp;
    }

    public void setMcyp(int mcyp) {
        this.mcyp = this.mcyp + mcyp;
    }

    public int getMcyi() {
        return mcyi;
    }

    public void setMcyi(int mcyi) {
        this.mcyi = this.mcyi + mcyi;
    }

    public int getHc() {
        return hc;
    }

    public void setHc(int hc) {
        this.hc = hc;
    }

    public int getCompadv() {
        return compadv;
    }

    public void setCompadv(int compadv) {
        this.compadv = this.compadv + compadv;
    }

    public int getGis() {
        return gis;
    }

    public void setGis(int gis) {
        this.gis = gis;
    }

    public int getGpfadv() {
        return gpfadv;
    }

    public void setGpfadv(int gpfadv) {
        this.gpfadv = gpfadv;
    }

    public int getFa() {
        return fa;
    }

    public void setFa(int fa) {
        this.fa = fa;
    }

    public int getHbap() {
        return hbap;
    }

    public void setHbap(int hbap) {
        this.hbap = this.mcyi + hbap;
    }

    public int getHbai() {
        return hbai;
    }

    public void setHbai(int hbai) {
        this.hbai = this.mcyi + hbai;
    }

    public int getLoan() {
        return loan;
    }

    public void setLoan(int loan) {
        this.loan = this.loan + loan;
    }

    public int getTotalded() {
        return totalded;
    }

    public void setTotalded(int totalded) {
        this.totalded = totalded;
    }

}
