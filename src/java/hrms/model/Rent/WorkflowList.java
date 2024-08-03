/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.Rent;

/**
 *
 * @author Manoj PC
 */
public class WorkflowList {
    private String fromName = null;
    private String fromDesignation = null;
    private String toName = null;
    private String toDesignation = null;
    private String remarks = null;
    private String dateCreated = null;
    private String gpc = null;

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromDesignation() {
        return fromDesignation;
    }

    public void setFromDesignation(String fromDesignation) {
        this.fromDesignation = fromDesignation;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getToDesignation() {
        return toDesignation;
    }

    public void setToDesignation(String toDesignation) {
        this.toDesignation = toDesignation;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getGpc() {
        return gpc;
    }

    public void setGpc(String gpc) {
        this.gpc = gpc;
    }
    
}
