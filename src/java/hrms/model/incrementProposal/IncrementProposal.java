/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.incrementProposal;

import hrms.model.login.Users;
import hrms.model.master.Office;
import hrms.model.master.SubstantivePost;
import hrms.model.task.TaskBean;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Surendra
 */
public class IncrementProposal implements Serializable {

    private int proposalId;

    private String emplist;

    private Office office;

    private int proposalMonth;

    private String monthasString = null;

    private int proposalYear;

    private String orderno;

    private Date orderDate;

    private TaskBean task;

    private Users emp;

    private SubstantivePost createdBy;

    private Date lastUpdatedOn;

    private SubstantivePost approvedBy;

    private String authspc;

    private int processStatus;

    private String processStatusName;
    
    private String empId;
    
    private String lastUpdated;
    
    private String propMonth;
    
    private String propYear;
    
    private String ordno;
    
    private String orddate;
    
    private String opt;
    
    private String note;
    
    private String payCommission;
    
    private String matrixLevel;
    
    private String matrixCell;
    
    private String authspc1;
    
    private String isOrderUpdated;
    
    private String wefDate;
    
    private String auth;
    
    private String auth1;
    
    private String vAuthorityName;
    private String aAuthorityName;
    
    

    public int getProposalId() {
        return proposalId;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setProposalId(int proposalId) {
        this.proposalId = proposalId;
    }

    public String getEmplist() {
        return emplist;
    }

    public void setEmplist(String emplist) {
        this.emplist = emplist;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public int getProposalMonth() {
        return proposalMonth;
    }

    public void setProposalMonth(int proposalMonth) {
        this.proposalMonth = proposalMonth;
    }

    public String getMonthasString() {
        return monthasString;
    }

    public void setMonthasString(String monthasString) {
        this.monthasString = monthasString;
    }

    public int getProposalYear() {
        return proposalYear;
    }

    public void setProposalYear(int proposalYear) {
        this.proposalYear = proposalYear;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public TaskBean getTask() {
        return task;
    }

    public void setTask(TaskBean task) {
        this.task = task;
    }

    public Users getEmp() {
        return emp;
    }

    public void setEmp(Users emp) {
        this.emp = emp;
    }

    public SubstantivePost getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(SubstantivePost createdBy) {
        this.createdBy = createdBy;
    }

    public Date getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(Date lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public SubstantivePost getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(SubstantivePost approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getAuthspc() {
        return authspc;
    }

    public void setAuthspc(String authspc) {
        this.authspc = authspc;
    }

    public int getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(int processStatus) {
        this.processStatus = processStatus;
    }

    public String getProcessStatusName() {
        return processStatusName;
    }

    public void setProcessStatusName(String processStatusName) {
        this.processStatusName = processStatusName;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getPropMonth() {
        return propMonth;
    }

    public void setPropMonth(String propMonth) {
        this.propMonth = propMonth;
    }

    public String getPropYear() {
        return propYear;
    }

    public void setPropYear(String propYear) {
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

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPayCommission() {
        return payCommission;
    }

    public void setPayCommission(String payCommission) {
        this.payCommission = payCommission;
    }

    public String getMatrixLevel() {
        return matrixLevel;
    }

    public void setMatrixLevel(String matrixLevel) {
        this.matrixLevel = matrixLevel;
    }

    public String getMatrixCell() {
        return matrixCell;
    }

    public void setMatrixCell(String matrixCell) {
        this.matrixCell = matrixCell;
    }

   
    public String getAuthspc1() {
        return authspc1;
    }

    public void setAuthspc1(String authspc1) {
        this.authspc1 = authspc1;
    }

    public String getIsOrderUpdated() {
        return isOrderUpdated;
    }

    public void setIsOrderUpdated(String isOrderUpdated) {
        this.isOrderUpdated = isOrderUpdated;
    }

    public String getWefDate() {
        return wefDate;
    }

    public void setWefDate(String wefDate) {
        this.wefDate = wefDate;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getAuth1() {
        return auth1;
    }

    public void setAuth1(String auth1) {
        this.auth1 = auth1;
    }

    public String getvAuthorityName() {
        return vAuthorityName;
    }

    public void setvAuthorityName(String vAuthorityName) {
        this.vAuthorityName = vAuthorityName;
    }

    public String getaAuthorityName() {
        return aAuthorityName;
    }

    public void setaAuthorityName(String aAuthorityName) {
        this.aAuthorityName = aAuthorityName;
    }

}
