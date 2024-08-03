/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.report.annualestablishmentreport;

/**
 *
 * @author Surendra
 */
public class AnnualEstablishmentReport {
    public int serialno=0;
    public String postname="";
    public String group="";
    public String scaleofPay="";
    public String level="";
    public int sanctionedStrength=0;
    public int meninPosition=0;
    public int vacancyPosition=0;

    public int getSerialno() {
        return serialno;
    }

    public void setSerialno(int serialno) {
        this.serialno = serialno;
    }

    public String getPostname() {
        return postname;
    }

    public void setPostname(String postname) {
        this.postname = postname;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getScaleofPay() {
        return scaleofPay;
    }

    public void setScaleofPay(String scaleofPay) {
        this.scaleofPay = scaleofPay;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getSanctionedStrength() {
        return sanctionedStrength;
    }

    public void setSanctionedStrength(int sanctionedStrength) {
        this.sanctionedStrength = sanctionedStrength;
    }

    public int getMeninPosition() {
        return meninPosition;
    }

    public void setMeninPosition(int meninPosition) {
        this.meninPosition = meninPosition;
    }

    public int getVacancyPosition() {
        return vacancyPosition;
    }

    public void setVacancyPosition(int vacancyPosition) {
        this.vacancyPosition = vacancyPosition;
    }
    
    
            
}
