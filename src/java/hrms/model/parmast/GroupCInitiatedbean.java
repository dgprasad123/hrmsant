/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.parmast;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author manisha
 */
public class GroupCInitiatedbean {

    private int groupCpromotionId;
    private String createdondate;
    private int taskId;
    private String pendingAtempId;
    private String pendingAtspc;

    private String reportingspc;
    private String reportingempId;
    private String reportingempname;
    private String reportingpost;
    private String reviewingempname;
    private String reviewingpost;
    private String fiscalyear;
    private Date periodFromReporting;
    private Date periodToReporting;
    private int promotionId;
    private int statusId;
    private String mode;
    
    private String assessmentTypeReporting;
    
    private ArrayList reportingdata = null;

    public int getGroupCpromotionId() {
        return groupCpromotionId;
    }

    public void setGroupCpromotionId(int groupCpromotionId) {
        this.groupCpromotionId = groupCpromotionId;
    }

    public String getCreatedondate() {
        return createdondate;
    }

    public void setCreatedondate(String createdondate) {
        this.createdondate = createdondate;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getPendingAtempId() {
        return pendingAtempId;
    }

    public void setPendingAtempId(String pendingAtempId) {
        this.pendingAtempId = pendingAtempId;
    }

    public String getPendingAtspc() {
        return pendingAtspc;
    }

    public void setPendingAtspc(String pendingAtspc) {
        this.pendingAtspc = pendingAtspc;
    }

    public String getReportingspc() {
        return reportingspc;
    }

    public void setReportingspc(String reportingspc) {
        this.reportingspc = reportingspc;
    }

    public String getReportingempId() {
        return reportingempId;
    }

    public void setReportingempId(String reportingempId) {
        this.reportingempId = reportingempId;
    }

    public String getReportingempname() {
        return reportingempname;
    }

    public void setReportingempname(String reportingempname) {
        this.reportingempname = reportingempname;
    }

    public String getReportingpost() {
        return reportingpost;
    }

    public void setReportingpost(String reportingpost) {
        this.reportingpost = reportingpost;
    }

    public String getReviewingempname() {
        return reviewingempname;
    }

    public void setReviewingempname(String reviewingempname) {
        this.reviewingempname = reviewingempname;
    }

    public String getReviewingpost() {
        return reviewingpost;
    }

    public void setReviewingpost(String reviewingpost) {
        this.reviewingpost = reviewingpost;
    }

    public String getFiscalyear() {
        return fiscalyear;
    }

    public void setFiscalyear(String fiscalyear) {
        this.fiscalyear = fiscalyear;
    }

    public Date getPeriodFromReporting() {
        return periodFromReporting;
    }

    public void setPeriodFromReporting(Date periodFromReporting) {
        this.periodFromReporting = periodFromReporting;
    }

    public Date getPeriodToReporting() {
        return periodToReporting;
    }

    public void setPeriodToReporting(Date periodToReporting) {
        this.periodToReporting = periodToReporting;
    }

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getAssessmentTypeReporting() {
        return assessmentTypeReporting;
    }

    public void setAssessmentTypeReporting(String assessmentTypeReporting) {
        this.assessmentTypeReporting = assessmentTypeReporting;
    }

    public ArrayList getReportingdata() {
        return reportingdata;
    }

    public void setReportingdata(ArrayList reportingdata) {
        this.reportingdata = reportingdata;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

   

}
