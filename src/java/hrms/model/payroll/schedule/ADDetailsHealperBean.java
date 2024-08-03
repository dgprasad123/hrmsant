/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.schedule;

/**
 *
 * @author Manas
 */
public class ADDetailsHealperBean implements Comparable {

    private String adcode;
    private int adamt;
    private String schedule;
    private String nowdedn;
    private String refdesc;
    private int rowNum;

    public int compareTo(Object ob) {

        /*
         If passed object is of type other than ADDetailsHealperBean, throw ClassCastException.
         */
        if (!(ob instanceof ADDetailsHealperBean)) {
            throw new ClassCastException("Invalid object");
        }

        int rn = ((ADDetailsHealperBean) ob).getRowNum();
        if (rn == 0) {
            return 0;
        }
        if (this.getRowNum() > rn) {
            return 1;
        } else if (this.getRowNum() < rn) {
            return -1;
        } else {
            return 0;
        }

    }

    public ADDetailsHealperBean() {

    }

    public ADDetailsHealperBean(String adcode, int adAmt, String schedule, String nowdedn, String refdesc, int rowNum) {
        this.adcode = adcode;
        this.adamt = adAmt;
        this.schedule = schedule;
        this.nowdedn = nowdedn;
        this.refdesc = refdesc;
        this.rowNum = rowNum;

    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdamt(int adamt) {
        this.adamt = adamt;
    }

    public int getAdamt() {
        return adamt;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setNowdedn(String nowdedn) {
        this.nowdedn = nowdedn;
    }

    public String getNowdedn() {
        return nowdedn;
    }

    public void setRefdesc(String refdesc) {
        this.refdesc = refdesc;
    }

    public String getRefdesc() {
        if (refdesc != null && !refdesc.equals("")) {
            refdesc = "(" + refdesc + ")";
        } else {
            refdesc = "";
        }
        return refdesc;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public int getRowNum() {
        return rowNum;
    }
}
