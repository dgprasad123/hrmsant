/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.master;

/**
 *
 * @author lenovo
 */
public class OtherSPC {

    private String offType;
    private String otherSPC;
     private String strOtherSPC;
    private String offName;
    
    private String hiddenoffType;
    
    private String sltCategory;
    private String sltDept;
    
    public String getOffType() {
        return offType;
    }

    public void setOffType(String offType) {
        this.offType = offType;
    }

    public String getOtherSPC() {
        return otherSPC;
    }

    public void setOtherSPC(String otherSPC) {
        this.otherSPC = otherSPC;
    }

    public String getOffName() {
        return offName;
    }

    public void setOffName(String offName) {
        this.offName = offName;
    }

    public String getStrOtherSPC() {
        return strOtherSPC;
    }

    public void setStrOtherSPC(String strOtherSPC) {
        this.strOtherSPC = strOtherSPC;
    }

    public String getHiddenoffType() {
        return hiddenoffType;
    }

    public void setHiddenoffType(String hiddenoffType) {
        this.hiddenoffType = hiddenoffType;
    }

    public String getSltCategory() {
        return sltCategory;
    }

    public void setSltCategory(String sltCategory) {
        this.sltCategory = sltCategory;
    }

    public String getSltDept() {
        return sltDept;
    }

    public void setSltDept(String sltDept) {
        this.sltDept = sltDept;
    }
}
