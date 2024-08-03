/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.parmast;

/**
 *
 * @author Manisha
 */
public class PARMessageCommunication {
    
    private String empId;
    private String appraiseName;
    private String appraiseSpc;
    private String appraisePost;
    private int parId;
    private int parStatus;
    private String statusName;
    private String fiscalyear;
    private String mobileNumber;
    private String parCreatedOn;
    private String appraiseEmpId;
    private String searchCriteria;

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }
    
    public String getAppraiseName() {
        return appraiseName;
    }

    public void setAppraiseName(String appraiseName) {
        this.appraiseName = appraiseName;
    }

    public String getAppraiseSpc() {
        return appraiseSpc;
    }

    public void setAppraiseSpc(String appraiseSpc) {
        this.appraiseSpc = appraiseSpc;
    }

    public String getAppraisePost() {
        return appraisePost;
    }

    public void setAppraisePost(String appraisePost) {
        this.appraisePost = appraisePost;
    }

    public int getParId() {
        return parId;
    }

    public void setParId(int parId) {
        this.parId = parId;
    }

    public int getParStatus() {
        return parStatus;
    }

    public void setParStatus(int parStatus) {
        this.parStatus = parStatus;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public String getFiscalyear() {
        return fiscalyear;
    }

    public void setFiscalyear(String fiscalyear) {
        this.fiscalyear = fiscalyear;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getParCreatedOn() {
        return parCreatedOn;
    }

    public void setParCreatedOn(String parCreatedOn) {
        this.parCreatedOn = parCreatedOn;
    }

    public String getAppraiseEmpId() {
        return appraiseEmpId;
    }

    public void setAppraiseEmpId(String appraiseEmpId) {
        this.appraiseEmpId = appraiseEmpId;
    }

    

}
