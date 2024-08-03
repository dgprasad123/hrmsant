/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.discProceeding;

/**
 *
 * @author manisha
 */
public class ForwardDiscChargeBean {
    private int daId;
    private String deptCode;   
    private String offCode;
    private String postCode;
    private String selectedAuthority;
    private int processId;

    public int getDaId() {
        return daId;
    }

    public void setDaId(int daId) {
        this.daId = daId;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }    

    public String getOffCode() {
        return offCode;
    }

    public void setOffCode(String offCode) {
        this.offCode = offCode;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }  

    public String getSelectedAuthority() {
        return selectedAuthority;
    }

    public void setSelectedAuthority(String selectedAuthority) {
        this.selectedAuthority = selectedAuthority;
    }

    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }
    
    
}
