/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.incrementProposal;

import java.io.Serializable;

/**
 *
 * @author Surendra
 */
public class ProposalMaster implements Serializable {

    private int proposalId;
    private int submitproposalId;
    private int exportproposalId;
    private String offCode = null;
    private String propMonth;
    private int propYear;
    private String ordno = null;
    private String orddate = null;
    private int taskid;
    private String createdBy = null;
    private String createdSpc = null;
    private String lastUpdated = null;
    private String approvedAuth = null;
    private String status = null;

    public int getProposalId() {
        return proposalId;
    }

    public void setProposalId(int proposalId) {
        this.proposalId = proposalId;
    }

    public int getSubmitproposalId() {
        return submitproposalId;
    }

    public void setSubmitproposalId(int submitproposalId) {
        this.submitproposalId = submitproposalId;
    }

    public int getExportproposalId() {
        return exportproposalId;
    }

    public void setExportproposalId(int exportproposalId) {
        this.exportproposalId = exportproposalId;
    }

    public String getOffCode() {
        return offCode;
    }

    public void setOffCode(String offCode) {
        this.offCode = offCode;
    }

    public String getPropMonth() {
        return propMonth;
    }

    public void setPropMonth(String propMonth) {
        this.propMonth = propMonth;
    }

    public int getPropYear() {
        return propYear;
    }

    public void setPropYear(int propYear) {
        this.propYear = propYear;
    }

    public String getOrdno() {
        return ordno;
    }

    public void setOrdno(String ordno) {
        this.ordno = ordno;
    }

    public String getOrddate() {
        return orddate;
    }

    public void setOrddate(String orddate) {
        this.orddate = orddate;
    }

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedSpc() {
        return createdSpc;
    }

    public void setCreatedSpc(String createdSpc) {
        this.createdSpc = createdSpc;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getApprovedAuth() {
        return approvedAuth;
    }

    public void setApprovedAuth(String approvedAuth) {
        this.approvedAuth = approvedAuth;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
