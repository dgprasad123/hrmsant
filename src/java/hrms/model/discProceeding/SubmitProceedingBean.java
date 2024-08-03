/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.discProceeding;

import java.util.Date;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author manisha
 */
public class SubmitProceedingBean {

    private int taskId;
    private int processId;
    private String initiatedBy;
    private Date initiatedOn;
    private int Status;
    private String pendingAt;
    private String note;
    private String applyTo;
    private String initiatedSpc;
    private String pendingSpc;
    private String applytoSpc;
    private String isSeen;
    private int daId;

    public int getDaId() {
        return daId;
    }

    public void setDaId(int daId) {
        this.daId = daId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }

    public String getInitiatedBy() {
        return initiatedBy;
    }

    public void setInitiatedBy(String initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

    public Date getInitiatedOn() {
        return initiatedOn;
    }

    public void setInitiatedOn(Date initiatedOn) {
        this.initiatedOn = initiatedOn;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getPendingAt() {
        return pendingAt;
    }

    public void setPendingAt(String pendingAt) {
        this.pendingAt = pendingAt;
    }

   

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getApplyTo() {
        return applyTo;
    }

    public void setApplyTo(String applyTo) {
        this.applyTo = applyTo;
    }

    public String getInitiatedSpc() {
        return initiatedSpc;
    }

    public void setInitiatedSpc(String initiatedSpc) {
        this.initiatedSpc = initiatedSpc;
    }

    public String getPendingSpc() {
        return pendingSpc;
    }

    public void setPendingSpc(String pendingSpc) {
        this.pendingSpc = pendingSpc;
    }

    public String getApplytoSpc() {
        return applytoSpc;
    }

    public void setApplytoSpc(String applytoSpc) {
        this.applytoSpc = applytoSpc;
    }

    public String getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(String isSeen) {
        this.isSeen = isSeen;
    }
    

}
