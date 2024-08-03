/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.master;

import java.io.Serializable;

/**
 *
 * @author Surendra
 */
public class Department implements Serializable {

    private String deptCode;

    private String deptName;

    /*
     @Column(name = "lmid")
     private int lmid;

     @Column(name = "slmid")
     private int slmid;
     */
    private String active;

    private String btid;

    private String departmentAbbr;

    private String deptAbbr;

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getBtid() {
        return btid;
    }

    public void setBtid(String btid) {
        this.btid = btid;
    }

    public String getDepartmentAbbr() {
        return departmentAbbr;
    }

    public void setDepartmentAbbr(String departmentAbbr) {
        this.departmentAbbr = departmentAbbr;
    }

    public String getDeptAbbr() {
        return deptAbbr;
    }

    public void setDeptAbbr(String deptAbbr) {
        this.deptAbbr = deptAbbr;
    }

}
