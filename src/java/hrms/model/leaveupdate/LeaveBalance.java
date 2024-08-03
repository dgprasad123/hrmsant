/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.leaveupdate;

import java.io.Serializable;

/**
 *
 * @author lenovo pc
 */
public class LeaveBalance implements Serializable{

    private String empid;
    private String empName;
    private String totalCl;
    private String totalClAvail;
    private String totalEl;
    private String totalElAvail;

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getTotalCl() {
        return totalCl;
    }

    public void setTotalCl(String totalCl) {
        this.totalCl = totalCl;
    }

    public String getTotalClAvail() {
        return totalClAvail;
    }

    public void setTotalClAvail(String totalClAvail) {
        this.totalClAvail = totalClAvail;
    }

    public String getTotalEl() {
        return totalEl;
    }

    public void setTotalEl(String totalEl) {
        this.totalEl = totalEl;
    }

    public String getTotalElAvail() {
        return totalElAvail;
    }

    public void setTotalElAvail(String totalElAvail) {
        this.totalElAvail = totalElAvail;
    }
    

}
