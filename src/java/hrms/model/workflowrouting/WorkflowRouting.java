/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.workflowrouting;

/**
 *
 * @author Surendra
 */
public class WorkflowRouting {
    
    private int workflowRoutingId;
    private String processAttribute1;
    private String officeCode;
    private String gpc;
    private String postName;
    private String processId;
    private String reportingGpc;
    private String loginOffcode;
    private String levelId;
    
   private String sltDept;
   private String sltOffcode;

    public String getSltDept() {
        return sltDept;
    }

    public void setSltDept(String sltDept) {
        this.sltDept = sltDept;
    }
   
   
    public String getSltOffcode() {
        return sltOffcode;
    }

    public void setSltOffcode(String sltOffcode) {
        this.sltOffcode = sltOffcode;
    }

    public String getLoginOffcode() {
        return loginOffcode;
    }

    public void setLoginOffcode(String loginOffcode) {
        this.loginOffcode = loginOffcode;
    }

    
   
   

    public int getWorkflowRoutingId() {
        return workflowRoutingId;
    }

    public void setWorkflowRoutingId(int workflowRoutingId) {
        this.workflowRoutingId = workflowRoutingId;
    }    

    public String getProcessAttribute1() {
        return processAttribute1;
    }

    public void setProcessAttribute1(String processAttribute1) {
        this.processAttribute1 = processAttribute1;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public String getGpc() {
        return gpc;
    }

    public void setGpc(String gpc) {
        this.gpc = gpc;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getReportingGpc() {
        return reportingGpc;
    }

    public void setReportingGpc(String reportingGpc) {
        this.reportingGpc = reportingGpc;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }
    
    
  
}
