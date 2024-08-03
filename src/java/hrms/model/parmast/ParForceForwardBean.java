/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.parmast;

import java.util.ArrayList;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author Manisha
 */
public class ParForceForwardBean {

    public static final int PENDING_AT_REPORTING_STATUS = 6;
    public static final int PENDING_AT_REVIEWING_STATUS = 7;
    public static final int PENDING_AT_ACCEPTING_STATUS = 8;
    public static final int PAR_COMPLETED_STATUS = 9;
    private int logId;
    private String fiscalyear;
    private String deptcode;
    private String deptName;
    private String cadreCode;
    private String cadreName;
    private String fromAuthority;
    private String toAuthority;
    private String forceforwardOn;
    private int parstatus;
    private int parId;
    private int taskId;
    private String pendingat;
    private String pendingspc;
    private int refid;
    private String forceForwardType;
    private String empId;
    private String empName;
    private String empPost;

    public int getRefid() {
        return refid;
    }

    public void setRefid(int refid) {
        this.refid = refid;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getFiscalyear() {
        return fiscalyear;
    }

    public void setFiscalyear(String fiscalyear) {
        this.fiscalyear = fiscalyear;
    }

    public String getDeptcode() {
        return deptcode;
    }

    public void setDeptcode(String deptcode) {
        this.deptcode = deptcode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getCadreCode() {
        return cadreCode;
    }

    public void setCadreCode(String cadreCode) {
        this.cadreCode = cadreCode;
    }

    public String getCadreName() {
        return cadreName;
    }

    public void setCadreName(String cadreName) {
        this.cadreName = cadreName;
    }

    public String getFromAuthority() {
        return fromAuthority;
    }

    public void setFromAuthority(String fromAuthority) {
        this.fromAuthority = fromAuthority;
    }

    public String getToAuthority() {
        return toAuthority;
    }

    public void setToAuthority(String toAuthority) {
        this.toAuthority = toAuthority;
    }

    public String getForceforwardOn() {
        return forceforwardOn;
    }

    public void setForceforwardOn(String forceforwardOn) {
        this.forceforwardOn = forceforwardOn;
    }

    public int getParstatus() {
        return parstatus;
    }

    public void setParstatus(int parstatus) {
        this.parstatus = parstatus;
    }

    public int getParId() {
        return parId;
    }

    public void setParId(int parId) {
        this.parId = parId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getForceForwardType() {
        return forceForwardType;
    }

    public void setForceForwardType(String forceForwardType) {
        this.forceForwardType = forceForwardType;
    }

    public String getPendingat() {
        return pendingat;
    }

    public void setPendingat(String pendingat) {
        this.pendingat = pendingat;
    }

    public String getPendingspc() {
        return pendingspc;
    }

    public void setPendingspc(String pendingspc) {
        this.pendingspc = pendingspc;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpPost() {
        return empPost;
    }

    public void setEmpPost(String empPost) {
        this.empPost = empPost;
    }

    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
