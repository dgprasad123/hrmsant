/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.schedule;

import java.util.ArrayList;

/**
 *
 * @author Manas
 */
public class AqreportHelperBean {

    private String empname = null;
    private String desg = null;
    private String payscale = null;
    private String slno = null;
    private String accNo = null;
    private String basic = null;
    private String ordno = null;
    private String orddate = null;
    private String gpfacct = null;
    private String gpfNoAssumed = null;
    private String acctype = null;
    private String cadreabbr = null;
    private ArrayList col1 = new ArrayList();
    private ArrayList col2 = new ArrayList();
    private ArrayList col3 = new ArrayList();
    private ArrayList col4 = new ArrayList();
    private ArrayList col5 = new ArrayList();
    private ArrayList col6 = new ArrayList();
    private ArrayList col7 = new ArrayList();
    private ArrayList col8 = new ArrayList();
    private ArrayList col9 = new ArrayList();
    private ArrayList col10 = new ArrayList();
    private ArrayList col11 = new ArrayList();
    private ArrayList col12 = new ArrayList();
    private ArrayList col13 = new ArrayList();
    private ArrayList col14 = new ArrayList();
    private ArrayList col15 = new ArrayList();
    private ArrayList col16 = new ArrayList();
    private ArrayList col17 = new ArrayList();
    private ArrayList col18 = new ArrayList();
    private ArrayList col19 = new ArrayList();
    private ArrayList col20 = new ArrayList();
    private ArrayList col21 = new ArrayList();
    
    
    private String  pagebreakLA=null;
    private StringBuffer pageHeaderLA=null;
   

    public String getPagebreakLA() {
        return pagebreakLA;
    }

    public void setPagebreakLA(String pagebreakLA) {
        this.pagebreakLA = pagebreakLA;
    }

    public StringBuffer getPageHeaderLA() {
        return pageHeaderLA;
    }

    public void setPageHeaderLA(StringBuffer pageHeaderLA) {
        this.pageHeaderLA = pageHeaderLA;
    }
    
    

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getEmpname() {
        return empname;
    }

    public void setDesg(String desg) {
        this.desg = desg;
    }

    public String getDesg() {
        return desg;
    }

    public void setPayscale(String payscale) {
        this.payscale = payscale;
    }

    public String getPayscale() {
        return payscale;
    }

    public void setSlno(String slno) {
        this.slno = slno;
    }

    public String getSlno() {
        return slno;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setBasic(String basic) {
        this.basic = basic;
    }

    public String getBasic() {
        if (basic == null) {
            basic = "0";
        }
        return basic;
    }

    public void setCol1(String adcode, int adAmt, String schedule, String nowdedn, String refdesc) {
        ADDetailsHealperBean adbean = new ADDetailsHealperBean(adcode, adAmt, schedule, nowdedn, refdesc, 0);
        this.col1.add(adbean);

    }

    public ArrayList getCol1() {
        return col1;
    }

    public void setCol2(String adcode, int adAmt, String schedule, String nowdedn, String refdesc) {
        ADDetailsHealperBean adbean = new ADDetailsHealperBean(adcode, adAmt, schedule, nowdedn, refdesc, 0);
        this.col2.add(adbean);

    }

    public ArrayList getCol2() {
        return col2;
    }

    public void setCol3(String adcode, int adAmt, String schedule, String nowdedn, String refdesc) {
        ADDetailsHealperBean adbean = new ADDetailsHealperBean(adcode, adAmt, schedule, nowdedn, refdesc, 0);
        this.col3.add(adbean);

    }

    public ArrayList getCol3() {
        return col3;
    }

    public void setCol4(String adcode, int adAmt, String schedule, String nowdedn, String refdesc) {
        ADDetailsHealperBean adbean = new ADDetailsHealperBean(adcode, adAmt, schedule, nowdedn, refdesc, 0);
        this.col4.add(adbean);

    }

    public ArrayList getCol4() {
        return col4;
    }

    public void setCol5(String adcode, int adAmt, String schedule, String nowdedn, String refdesc) {
        ADDetailsHealperBean adbean = new ADDetailsHealperBean(adcode, adAmt, schedule, nowdedn, refdesc, 0);
        this.col5.add(adbean);

    }

    public ArrayList getCol5() {
        return col5;
    }

    public void setCol6(String adcode, int adAmt, String schedule, String nowdedn, String refdesc, String[] col6Desc) {
        if (col6Desc != null) {
            int len = col6Desc.length;
            for (int i = 0; i < len; i++) {
                if (adcode.equalsIgnoreCase(col6Desc[i])) {
                    ADDetailsHealperBean adbean = new ADDetailsHealperBean(adcode, adAmt, schedule, nowdedn, refdesc, i + 1);
                    this.col6.add(adbean);
                    i = len;
                }
            }
        } else {
            ADDetailsHealperBean adbean = new ADDetailsHealperBean(adcode, adAmt, schedule, nowdedn, refdesc, 0);
            this.col6.add(adbean);
        }
    }

    public ArrayList getCol6() {
        return col6;
    }

    public void setCol7(String adcode, int adAmt, String schedule, String nowdedn, String refdesc, String[] col7Desc) {
        if (col7Desc != null) {
            int len = col7Desc.length;
            for (int i = 0; i < len; i++) {
                if (adcode.equalsIgnoreCase(col7Desc[i])) {
                    ADDetailsHealperBean adbean = new ADDetailsHealperBean(adcode, adAmt, schedule, nowdedn, refdesc, i + 1);
                    this.col7.add(adbean);
                    i = len;
                }
            }
        } else {
            ADDetailsHealperBean adbean = new ADDetailsHealperBean(adcode, adAmt, schedule, nowdedn, refdesc, 0);
            this.col7.add(adbean);
        }
    }

    public ArrayList getCol7() {
        return col7;
    }

    public void setCol8(String adcode, int adAmt, String schedule, String nowdedn, String refdesc) {
        ADDetailsHealperBean adbean = new ADDetailsHealperBean(adcode, adAmt, schedule, nowdedn, refdesc, 0);
        this.col8.add(adbean);

    }

    public ArrayList getCol8() {
        return col8;
    }

    public void setCol9(String adcode, int adAmt, String schedule, String nowdedn, String refdesc) {
        ADDetailsHealperBean adbean = new ADDetailsHealperBean(adcode, adAmt, schedule, nowdedn, refdesc, 0);
        this.col9.add(adbean);

    }

    public ArrayList getCol9() {
        return col9;
    }

    public void setCol10(String adcode, int adAmt, String schedule, String nowdedn, String refdesc) {
        ADDetailsHealperBean adbean = new ADDetailsHealperBean(adcode, adAmt, schedule, nowdedn, refdesc, 0);
        this.col10.add(adbean);

    }

    public ArrayList getCol10() {
        return col10;
    }

    public void setCol11(String adcode, int adAmt, String schedule, String nowdedn, String refdesc) {
        ADDetailsHealperBean adbean = new ADDetailsHealperBean(adcode, adAmt, schedule, nowdedn, refdesc, 0);
        this.col11.add(adbean);

    }

    public ArrayList getCol11() {
        return col11;
    }

    public void setCol12(String adcode, int adAmt, String schedule, String nowdedn, String refdesc) {
        ADDetailsHealperBean adbean = new ADDetailsHealperBean(adcode, adAmt, schedule, nowdedn, refdesc, 0);
        this.col12.add(adbean);

    }

    public ArrayList getCol12() {
        return col12;
    }

    public void setCol13(String adcode, int adAmt, String schedule, String nowdedn, String refdesc) {
        ADDetailsHealperBean adbean = new ADDetailsHealperBean(adcode, adAmt, schedule, nowdedn, refdesc, 0);
        this.col13.add(adbean);

    }

    public ArrayList getCol13() {
        return col13;
    }

    public void setCol14(String adcode, int adAmt, String schedule, String nowdedn, String refdesc) {
        ADDetailsHealperBean adbean = new ADDetailsHealperBean(adcode, adAmt, schedule, nowdedn, refdesc, 0);
        this.col14.add(adbean);

    }

    public ArrayList getCol14() {
        return col14;
    }

    public void setCol15(String adcode, int adAmt, String schedule, String nowdedn, String refdesc) {
        ADDetailsHealperBean adbean = new ADDetailsHealperBean(adcode, adAmt, schedule, nowdedn, refdesc, 0);
        this.col15.add(adbean);

    }

    public ArrayList getCol15() {
        return col15;
    }

    public void setCol16(String adcode, int adAmt, String schedule, String nowdedn, String refdesc, String[] col16Desc) {
        if (col16Desc != null) {
            int len = col16Desc.length;
            for (int i = 0; i < len; i++) {
                if (adcode.equalsIgnoreCase(col16Desc[i])) {
                    ADDetailsHealperBean adbean = new ADDetailsHealperBean(adcode, adAmt, schedule, nowdedn, refdesc, i + 1);
                    this.col16.add(adbean);
                    i = len;
                }
            }
        } else {
            ADDetailsHealperBean adbean = new ADDetailsHealperBean(adcode, adAmt, schedule, nowdedn, refdesc, 0);
            this.col16.add(adbean);
        }

    }

    public ArrayList getCol16() {
        return col16;
    }

    public void setCol17(String adcode, int adAmt, String schedule, String nowdedn, String refdesc, String[] col17Desc) {
        if (col17Desc != null) {
            int len = col17Desc.length;
            for (int i = 0; i < len; i++) {
                if (adcode.equalsIgnoreCase(col17Desc[i])) {
                    ADDetailsHealperBean adbean = new ADDetailsHealperBean(adcode, adAmt, schedule, nowdedn, refdesc, i + 1);
                    this.col17.add(adbean);
                    i = len;
                }
            }
        } else {
            ADDetailsHealperBean adbean = new ADDetailsHealperBean(adcode, adAmt, schedule, nowdedn, refdesc, 0);
            this.col17.add(adbean);
        }
    }

    public ArrayList getCol17() {
        return col17;
    }

    public void setCol18(String adcode, int adAmt, String schedule, String nowdedn, String refdesc, String[] col18Desc) {
        if (col18Desc != null) {
            int len = col18Desc.length;
            for (int i = 0; i < len; i++) {
                if (adcode.equalsIgnoreCase(col18Desc[i])) {
                    ADDetailsHealperBean adbean = new ADDetailsHealperBean(adcode, adAmt, schedule, nowdedn, refdesc, i + 1);
                    this.col18.add(adbean);
                    i = len;
                }
            }
        } else {
            //ADDetailsHealperBean adbean = new ADDetailsHealperBean(adcode, adAmt, schedule, nowdedn, refdesc, 0);
            //this.col18.add(adbean);
            if (adcode != null) {
                if (adcode.equals("OR")||adcode.equals("CC") || adcode.equals("GISA") || adcode.equals("GIS")) {
                    ADDetailsHealperBean adbean = new ADDetailsHealperBean(adcode, adAmt, schedule, nowdedn, refdesc, 0);
                    this.col18.add(adbean);
                }else if (adcode.equals("CMPA")) {
                    if (nowdedn != null) {
                        if (nowdedn.equals("I") && adAmt > 0) {
                            ADDetailsHealperBean adbean = new ADDetailsHealperBean(adcode, adAmt, schedule, nowdedn, refdesc, 0);
                            this.col18.add(adbean);
                        } else if (nowdedn.equals("P") && adAmt > 0) {
                            ADDetailsHealperBean adbean = new ADDetailsHealperBean(adcode, adAmt, schedule, nowdedn, refdesc, 0);
                            this.col18.add(adbean);
                        }
                    }
                }
            }
        }
    }

    public ArrayList getCol18() {
        return col18;
    }

    public void setCol19(String adcode, int adAmt, String schedule, String nowdedn, String refdesc) {
        ADDetailsHealperBean adbean = new ADDetailsHealperBean(adcode, adAmt, schedule, nowdedn, refdesc, 0);
        this.col19.add(adbean);

    }

    public ArrayList getCol19() {
        return col19;
    }

    public void setCol20(String adcode, int adAmt, String schedule, String nowdedn, String refdesc) {
        ADDetailsHealperBean adbean = new ADDetailsHealperBean(adcode, adAmt, schedule, nowdedn, refdesc, 0);
        this.col20.add(adbean);

    }

    public ArrayList getCol20() {
        return col20;
    }

    public void setCol21(String adcode, int adAmt, String schedule, String nowdedn, String refdesc) {
        ADDetailsHealperBean adbean = new ADDetailsHealperBean(adcode, adAmt, schedule, nowdedn, refdesc, 0);
        this.col21.add(adbean);

    }

    public ArrayList getCol21() {
        return col21;
    }

    public void setOrdno(String ordno) {
        this.ordno = ordno;
    }

    public String getOrdno() {
        return ordno;
    }

    public void setOrddate(String orddate) {
        this.orddate = orddate;
    }

    public String getOrddate() {
        return orddate;
    }

    public void setGpfacct(String gpfacct) {
        this.gpfacct = gpfacct;
    }

    public String getGpfacct() {
        return gpfacct;
    }

    public void setAcctype(String acctype) {
        this.acctype = acctype;
    }

    public String getAcctype() {
        if (acctype == null) {
            acctype = "";
        }
        return acctype;
    }

    public void setCadreabbr(String cadreabbr) {
        this.cadreabbr = cadreabbr;
    }

    public String getCadreabbr() {
        return cadreabbr;
    }

    public void setGpfNoAssumed(String gpfNoAssumed) {
        this.gpfNoAssumed = gpfNoAssumed;
    }

    public String getGpfNoAssumed() {
        return gpfNoAssumed;
    }  
   
    
}
