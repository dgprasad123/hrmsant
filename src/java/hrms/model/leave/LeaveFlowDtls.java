package hrms.model.leave;

import java.util.ArrayList;

public class LeaveFlowDtls {
    private String taskId = null;
    private String taskdate = null;
    private String actionTakenBy = null;
    private String status = null;
    private String note = null;
    private String availString = null;
    private String actionTakenId = null;
    private String actionTaken = null;
    private ArrayList attachments = new ArrayList();
    private String txtApproveFrom = null;
    private String txtApproveTo = null;
    private String statusId = null;

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }
   

   

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskdate() {
        return taskdate;
    }

    public void setTaskdate(String taskdate) {
        this.taskdate = taskdate;
    }

    public String getActionTakenBy() {
        return actionTakenBy;
    }

    public void setActionTakenBy(String actionTakenBy) {
        this.actionTakenBy = actionTakenBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAvailString() {
        return availString;
    }

    public void setAvailString(String availString) {
        this.availString = availString;
    }

    public String getActionTakenId() {
        return actionTakenId;
    }

    public void setActionTakenId(String actionTakenId) {
        this.actionTakenId = actionTakenId;
    }

    public String getActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(String actionTaken) {
        this.actionTaken = actionTaken;
    }

    public ArrayList getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList attachments) {
        this.attachments = attachments;
    }

    public String getTxtApproveFrom() {
        return txtApproveFrom;
    }

    public void setTxtApproveFrom(String txtApproveFrom) {
        this.txtApproveFrom = txtApproveFrom;
    }

    public String getTxtApproveTo() {
        return txtApproveTo;
    }

    public void setTxtApproveTo(String txtApproveTo) {
        this.txtApproveTo = txtApproveTo;
    }

}
