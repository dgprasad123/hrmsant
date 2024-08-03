/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author Surendra
 */
public class TaskListHelperBean implements Comparable<TaskListHelperBean> {
    private int count = 0;
    private Date dateOfInitiation = null;
    private String dateOfInitiationAsString = null;
    private String processname = null;
    private String applicant = null;
    private String status = null;
    private int taskId = 0;
    private int statusId =0;
    private String pendingAt;
    private String pendingSpc;
    private String appEmpCode = null;
    private String appSpc = null;
    private int processId = 0;
    private String istaskcompleted = null;
    private String submitted_on = null;
    
    private String auth = null;
    private int referenceId;
    private String initiatedByName;
    private String initiatedByPost;
    
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
    
    public HashMap getActionParam() {
        HashMap params = new HashMap();
        params.put("submit", "View");
        params.put("taskId", this.taskId);
        return params;
    }

    public String getPendingAt() {
        return pendingAt;
    }

    public void setPendingAt(String pendingAt) {
        this.pendingAt = pendingAt;
    }

    public String getPendingSpc() {
        return pendingSpc;
    }

    public void setPendingSpc(String pendingSpc) {
        this.pendingSpc = pendingSpc;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getDateOfInitiation() {
        return dateOfInitiation;
    }

    public void setDateOfInitiation(Date dateOfInitiation) {
        this.dateOfInitiation = dateOfInitiation;
    }

    public String getDateOfInitiationAsString() {
        return dateOfInitiationAsString;
    }

    public void setDateOfInitiationAsString(String dateOfInitiationAsString) {
        this.dateOfInitiationAsString = dateOfInitiationAsString;
    }

    public String getProcessname() {
        return processname;
    }

    public void setProcessname(String processname) {
        this.processname = processname;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getAppEmpCode() {
        return appEmpCode;
    }

    public void setAppEmpCode(String appEmpCode) {
        this.appEmpCode = appEmpCode;
    }

    public String getAppSpc() {
        return appSpc;
    }

    public void setAppSpc(String appSpc) {
        this.appSpc = appSpc;
    }

    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }

    public String getIstaskcompleted() {
        return istaskcompleted;
    }

    public void setIstaskcompleted(String istaskcompleted) {
        this.istaskcompleted = istaskcompleted;
    }

    public void setSubmitted_on(String submitted_on) {
        this.submitted_on = submitted_on;
    }

    public String getSubmitted_on() {
        return submitted_on;
    }
    public int compareTo(TaskListHelperBean comparestu) {
              Date comparedate=null;
              Date date1 = null;
              Date date2 =null;
              try{
                  comparedate=((TaskListHelperBean)comparestu).getDateOfInitiation();
                  date1 = comparedate;
                  date2 = dateOfInitiation;
              }catch(Exception e){
                  e.printStackTrace();
              }
              return date1.compareTo(date2);

             
          }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getAuth() {
        return auth;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    public String getInitiatedByName() {
        return initiatedByName;
    }

    public void setInitiatedByName(String initiatedByName) {
        this.initiatedByName = initiatedByName;
    }

    public String getInitiatedByPost() {
        return initiatedByPost;
    }

    public void setInitiatedByPost(String initiatedByPost) {
        this.initiatedByPost = initiatedByPost;
    }
    
}
