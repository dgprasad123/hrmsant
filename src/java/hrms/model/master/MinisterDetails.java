/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.master;

import java.util.List;

/**
 *
 * @author Devi
 */
public class MinisterDetails {
    
    private String off_as;
    private String fname = null;
    private String lname = null;
    private String mnane = null;
    private String initial = null;
    private String fullname= null;
    private String mobileno = null;
    private String userid = null;
    private String password = null;
    private String ministertype = null;
    private String deptname = null;
    private String officiatId = null;
    private String emailId= null;
    private String ministerId=null;
    private String urertype=null;
    private int hidlmid;
    private String deptcode= null;
    private String activemember = null;
    private  List assigneddeptlist;
    private String hidoff_as;

    
    
    public String getHidoff_as() {
        return hidoff_as;
    }

    public void setHidoff_as(String hidoff_as) {
        this.hidoff_as = hidoff_as;
    }
    
    public List getAssigneddeptlist() {
        return assigneddeptlist;
    }

    public void setAssigneddeptlist(List assigneddeptlist) {
        this.assigneddeptlist = assigneddeptlist;
    }
    
    public String getActivemember() {
        return activemember;
    }

    public void setActivemember(String activemember) {
        this.activemember = activemember;
    }
    
    public String getDeptcode() {
        return deptcode;
    }

    public void setDeptcode(String deptcode) {
        this.deptcode = deptcode;
    }  
    
    public int getHidlmid() {
        return hidlmid;
    }

    public void setHidlmid(int hidlmid) {
        this.hidlmid = hidlmid;
    }
      

    public String getUrertype() {
        return urertype;
    }

    public void setUrertype(String urertype) {
        this.urertype = urertype;
    }
    
   
    

    public String getOff_as() {
        return off_as;
    }

    public void setOff_as(String off_as) {
        this.off_as = off_as;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getMnane() {
        return mnane;
    }

    public void setMnane(String mnane) {
        this.mnane = mnane;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMinistertype() {
        return ministertype;
    }

    public void setMinistertype(String ministertype) {
        this.ministertype = ministertype;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public String getOfficiatId() {
        return officiatId;
    }

    public void setOfficiatId(String officiatId) {
        this.officiatId = officiatId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMinisterId() {
        return ministerId;
    }

    public void setMinisterId(String ministerId) {
        this.ministerId = ministerId;
    }

   
     
    
}
