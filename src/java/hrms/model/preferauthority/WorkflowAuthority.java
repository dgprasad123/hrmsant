package hrms.model.preferauthority;

import java.io.Serializable;

public class WorkflowAuthority implements Serializable {
    
    private String empid = null;
    private String empname = null;
    private String spc = null;
    private String post = null;
    private String postcode = null;
    private String authoritySpc = null;
    
    private String hidDeptCode = null;
    private String hidOffCode = null;
    
    private String chkAuth = null;
    
    private String combination = null;
    
    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getAuthoritySpc() {
        return authoritySpc;
    }

    public void setAuthoritySpc(String authoritySpc) {
        this.authoritySpc = authoritySpc;
    }

    public String getHidDeptCode() {
        return hidDeptCode;
    }

    public void setHidDeptCode(String hidDeptCode) {
        this.hidDeptCode = hidDeptCode;
    }

    public String getHidOffCode() {
        return hidOffCode;
    }

    public void setHidOffCode(String hidOffCode) {
        this.hidOffCode = hidOffCode;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getChkAuth() {
        return chkAuth;
    }

    public void setChkAuth(String chkAuth) {
        this.chkAuth = chkAuth;
    }

    public String getCombination() {
        return combination;
    }

    public void setCombination(String combination) {
        this.combination = combination;
    }

    public String getSpc() {
        return spc;
    }

    public void setSpc(String spc) {
        this.spc = spc;
    }
}
