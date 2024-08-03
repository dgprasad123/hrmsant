/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.login;

import java.io.Serializable;

/**
 *
 * @author Durga
 */
public class AdminUsers implements Serializable {

    private String empId;
    private String fullName;
    private String mobile;
    private String usertype;
    private String offName;
    private String designation;
    private String refcode; // Refcode means any code need to stroe for further reference like distcode for district coordinator
    private String searchBox;
    private int pageno;
    private String hasparreviewingAuthorization; 
    private String distCode;
    
    
    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getOffName() {
        return offName;
    }

    public void setOffName(String offName) {
        this.offName = offName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getRefcode() {
        return refcode;
    }

    public void setRefcode(String refcode) {
        this.refcode = refcode;
    }

    public String getSearchBox() {
        return searchBox;
    }

    public void setSearchBox(String searchBox) {
        this.searchBox = searchBox;
    }

    public int getPageno() {
        return pageno;
    }

    public void setPageno(int pageno) {
        this.pageno = pageno;
    }

    public String getHasparreviewingAuthorization() {
        return hasparreviewingAuthorization;
    }

    public void setHasparreviewingAuthorization(String hasparreviewingAuthorization) {
        this.hasparreviewingAuthorization = hasparreviewingAuthorization;
    }

    public String getDistCode() {
        return distCode;
    }

    public void setDistCode(String distCode) {
        this.distCode = distCode;
    }
    
    

}
