/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.task;

import hrms.model.login.Users;
import hrms.model.master.SubstantivePost;
import hrms.model.master.GworkFlowProcess;
import hrms.model.master.ProcessStatus;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Surendra
 */
public class TaskBean implements Serializable {

    private int taskid;

    private Users empMastInitiatedBy;

    private SubstantivePost substantiveinitiatedspc;

    private Date initiatedon;

    private String pendingspc;

    private String pendingat;

    private GworkFlowProcess gworkflow;

    private ProcessStatus status;

    private String parstatus;

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public Users getEmpMastInitiatedBy() {
        return empMastInitiatedBy;
    }

    public void setEmpMastInitiatedBy(Users empMastInitiatedBy) {
        this.empMastInitiatedBy = empMastInitiatedBy;
    }

    public SubstantivePost getSubstantiveinitiatedspc() {
        return substantiveinitiatedspc;
    }

    public void setSubstantiveinitiatedspc(SubstantivePost substantiveinitiatedspc) {
        this.substantiveinitiatedspc = substantiveinitiatedspc;
    }

    public Date getInitiatedon() {
        return initiatedon;
    }

    public void setInitiatedon(Date initiatedon) {
        this.initiatedon = initiatedon;
    }

    public String getPendingspc() {
        return pendingspc;
    }

    public void setPendingspc(String pendingspc) {
        this.pendingspc = pendingspc;
    }

    public String getPendingat() {
        return pendingat;
    }

    public void setPendingat(String pendingat) {
        this.pendingat = pendingat;
    }

    public GworkFlowProcess getGworkflow() {
        return gworkflow;
    }

    public void setGworkflow(GworkFlowProcess gworkflow) {
        this.gworkflow = gworkflow;
    }

    public ProcessStatus getStatus() {
        return status;
    }

    public void setStatus(ProcessStatus status) {
        this.status = status;
    }

    public String getParstatus() {
        return parstatus;
    }

    public void setParstatus(String parstatus) {
        this.parstatus = parstatus;
    }

}
