/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.leave;

/**
 *
 * @author Manas
 */
public class EmployeeLeaveAccount {
    private String empid;
    private String leaveType;
    private String leavePeriod;
    private String leavePeriodFrom;
    private String leavePeriodTo;

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getLeavePeriod() {
        return leavePeriod;
    }

    public void setLeavePeriod(String leavePeriod) {
        this.leavePeriod = leavePeriod;
    }

    public String getLeavePeriodFrom() {
        return leavePeriodFrom;
    }

    public void setLeavePeriodFrom(String leavePeriodFrom) {
        this.leavePeriodFrom = leavePeriodFrom;
    }

    public String getLeavePeriodTo() {
        return leavePeriodTo;
    }

    public void setLeavePeriodTo(String leavePeriodTo) {
        this.leavePeriodTo = leavePeriodTo;
    }
    
    
}
