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
public class SectionWiseAqBean {

    private String sectionname;
    private ArrayList aqlistSectionWise = null;
    private String sectionwiseOffEn;
    private String offcode;
    private int offCount;

    public SectionWiseAqBean() {
    }

    public void setAqlistSectionWise(ArrayList aqlistSectionWise) {
        this.aqlistSectionWise = aqlistSectionWise;
    }

    public ArrayList getAqlistSectionWise() {
        return aqlistSectionWise;
    }

    public void setSectionname(String sectionname) {
        this.sectionname = sectionname;
    }

    public String getSectionname() {
        return sectionname;
    }

    public String getSectionwiseOffEn() {
        return sectionwiseOffEn;
    }

    public void setSectionwiseOffEn(String sectionwiseOffEn) {
        this.sectionwiseOffEn = sectionwiseOffEn;
    }

    public String getOffcode() {
        return offcode;
    }

    public void setOffcode(String offcode) {
        this.offcode = offcode;
    }

    public int getOffCount() {
        return offCount;
    }

    public void setOffCount(int offCount) {
        this.offCount = offCount;
    }
    
}
