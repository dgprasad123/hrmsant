/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.parmast;

/**
 *
 * @author manisha
 */
public class DepartmentPromotionBean {

    private int dpcId;
    private String cadrecode;
    private String cadrename;
    private String fiscalYearFrom = null;
    private String fiscalYearTo = null;
    private String createdByEmpId;
    private String createdOn;     
    private String postGroupType;
    private String dpcName;

    public int getDpcId() {
        return dpcId;
    }

    public void setDpcId(int dpcId) {
        this.dpcId = dpcId;
    }    

    public String getCadrecode() {
        return cadrecode;
    }

    public void setCadrecode(String cadrecode) {
        this.cadrecode = cadrecode;
    }

    public String getCadrename() {
        return cadrename;
    }

    public void setCadrename(String cadrename) {
        this.cadrename = cadrename;
    }

    public String getFiscalYearFrom() {
        return fiscalYearFrom;
    }

    public void setFiscalYearFrom(String fiscalYearFrom) {
        this.fiscalYearFrom = fiscalYearFrom;
    }

    public String getFiscalYearTo() {
        return fiscalYearTo;
    }

    public void setFiscalYearTo(String fiscalYearTo) {
        this.fiscalYearTo = fiscalYearTo;
    }

    public String getCreatedByEmpId() {
        return createdByEmpId;
    }

    public void setCreatedByEmpId(String createdByEmpId) {
        this.createdByEmpId = createdByEmpId;
    }

   

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getPostGroupType() {
        return postGroupType;
    }

    public void setPostGroupType(String postGroupType) {
        this.postGroupType = postGroupType;
    }

    public String getDpcName() {
        return dpcName;
    }

    public void setDpcName(String dpcName) {
        this.dpcName = dpcName;
    }
    
}
