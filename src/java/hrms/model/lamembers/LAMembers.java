package hrms.model.lamembers;

import java.io.Serializable;

public class LAMembers implements Serializable {

    private int empId;

    private String initials;

    private String fname;

    private String mname;

    private String lname;

    private String fullName;

    private String off_as;

    private int rank;

    private String active;

    private String mobile;

    private String designation;

    private String offName;

    private String usertype;

    private String hasManageRent;

    private String hasAERAuthorization;

   // private String hasGroupCcustodianAuthorization;
    public String getHasManageRent() {
        return hasManageRent;
    }

    public void setHasManageRent(String hasManageRent) {
        this.hasManageRent = hasManageRent;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOff_as() {
        return off_as;
    }

    public void setOff_as(String off_as) {
        this.off_as = off_as;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getOffName() {
        return offName;
    }

    public void setOffName(String offName) {
        this.offName = offName;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getHasAERAuthorization() {
        return hasAERAuthorization;
    }

    public void setHasAERAuthorization(String hasAERAuthorization) {
        this.hasAERAuthorization = hasAERAuthorization;
    }

}
