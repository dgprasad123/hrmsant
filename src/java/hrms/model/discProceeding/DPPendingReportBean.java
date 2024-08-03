/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.discProceeding;

import java.util.List;

/**
 *
 * @author Manisha
 */
public class DPPendingReportBean {
    private String departmentName;
    private String OfficeCode;
    private String OfficeName;
    private String totalNumberInitiated;
    private String cadreName;
    private List deptWiseDP;
    

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getOfficeCode() {
        return OfficeCode;
    }

    public void setOfficeCode(String OfficeCode) {
        this.OfficeCode = OfficeCode;
    }

    public String getOfficeName() {
        return OfficeName;
    }

    public void setOfficeName(String OfficeName) {
        this.OfficeName = OfficeName;
    }

    public String getTotalNumberInitiated() {
        return totalNumberInitiated;
    }

    public void setTotalNumberInitiated(String totalNumberInitiated) {
        this.totalNumberInitiated = totalNumberInitiated;
    }

    public String getCadreName() {
        return cadreName;
    }

    public void setCadreName(String cadreName) {
        this.cadreName = cadreName;
    }

    public List getDeptWiseDP() {
        return deptWiseDP;
    }

    public void setDeptWiseDP(List deptWiseDP) {
        this.deptWiseDP = deptWiseDP;
    }
    
    
}
