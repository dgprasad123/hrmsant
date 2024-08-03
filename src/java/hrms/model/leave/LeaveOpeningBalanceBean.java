/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.leave;

/**
 *
 * @author Manoj PC
 */
public class LeaveOpeningBalanceBean {
    private String leaveId = null;
    private String leaveType = null;
    private String openingBalanceDate = null;
    private String time = null;
    private String openingBalance = null;
    private String empId = null;
    private String lobId = null;

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getLobId() {
        return lobId;
    }

    public void setLobId(String lobId) {
        this.lobId = lobId;
    }

    public String getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getOpeningBalanceDate() {
        return openingBalanceDate;
    }

    public void setOpeningBalanceDate(String openingBalanceDate) {
        this.openingBalanceDate = openingBalanceDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(String openingBalance) {
        this.openingBalance = openingBalance;
    } 
}
