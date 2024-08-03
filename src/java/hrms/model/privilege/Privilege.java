/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.privilege;

/**
 *
 * @author Manas Jena
 */
public class Privilege {

    private int privmapid;
    private String rolename;
    private String modulegroup;
    private String modulename;
    private String empname;
    private String gpfno;
    private String empid;
    private String privspc;
    private String privspn;
    private String offCode;
    
    private String deptCode;
    private String postcode;
    private String distcode;

    public int getPrivmapid() {
        return privmapid;
    }

    public void setPrivmapid(int privmapid) {
        this.privmapid = privmapid;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public String getModulegroup() {
        return modulegroup;
    }

    public void setModulegroup(String modulegroup) {
        this.modulegroup = modulegroup;
    }

    public String getModulename() {
        return modulename;
    }

    public void setModulename(String modulename) {
        this.modulename = modulename;
    }    

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getGpfno() {
        return gpfno;
    }

    public void setGpfno(String gpfno) {
        this.gpfno = gpfno;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getPrivspc() {
        return privspc;
    }

    public void setPrivspc(String privspc) {
        this.privspc = privspc;
    }

    public String getPrivspn() {
        return privspn;
    }

    public void setPrivspn(String privspn) {
        this.privspn = privspn;
    }

    public String getOffCode() {
        return offCode;
    }

    public void setOffCode(String offCode) {
        this.offCode = offCode;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getDistcode() {
        return distcode;
    }

    public void setDistcode(String distcode) {
        this.distcode = distcode;
    }
    
}
