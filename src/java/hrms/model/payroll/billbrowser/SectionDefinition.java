/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.billbrowser;

/**
 *
 * @author Manas Jena
 */
public class SectionDefinition {
    
    private int sectionId = 0;
    private String section = null;
    private int secslno = 0;
    private int nofpost = 0;    
    private String radoption = null;
    private int menInPos = 0;
    private String billgroup = null;
    private String billType = null;


    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }           

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getSecslno() {
        return secslno;
    }

    public void setSecslno(int secslno) {
        this.secslno = secslno;
    }

    public int getNofpost() {
        return nofpost;
    }

    public void setNofpost(int nofpost) {
        this.nofpost = nofpost;
    }

    public String getRadoption() {
        return radoption;
    }

    public void setRadoption(String radoption) {
        this.radoption = radoption;
    }

    public int getMenInPos() {
        return menInPos;
    }

    public void setMenInPos(int menInPos) {
        this.menInPos = menInPos;
    }

    public String getBillgroup() {
        return billgroup;
    }

    public void setBillgroup(String billgroup) {
        this.billgroup = billgroup;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }
    
    
}
