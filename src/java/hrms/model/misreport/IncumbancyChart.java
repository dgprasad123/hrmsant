/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.misreport;

import java.util.Date;

/**
 *
 * @author Manas Jena
 */
public class IncumbancyChart {
    private String empid;
    private String fname;
    private String mname;
    private String lname;
    private Date joindate;
    private Date relievedate;
    private String jointype;

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public Date getJoindate() {
        return joindate;
    }

    public void setJoindate(Date joindate) {
        this.joindate = joindate;
    }

    public Date getRelievedate() {
        return relievedate;
    }

    public void setRelievedate(Date relievedate) {
        this.relievedate = relievedate;
    }

    public String getJointype() {
        return jointype;
    }

    public void setJointype(String jointype) {
        this.jointype = jointype;
    }
    
}
