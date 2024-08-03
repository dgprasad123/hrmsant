/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.trainingadmin;

/**
 *
 * @author Manoj PC
 */
public class EmpAttendanceBean {
        private String employeeId;
        private String empName;
        private String attDate;
        private String inTime;
        private String outTime;
        private String workingHour;
        private String txtperiodFrom;
        private String txtperiodTo;
        private int empId;
        private String applicantName;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getAttDate() {
        return attDate;
    }

    public void setAttDate(String attDate) {
        this.attDate = attDate;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getWorkingHour() {
        return workingHour;
    }

    public void setWorkingHour(String workingHour) {
        this.workingHour = workingHour;
    }

    public String getTxtperiodFrom() {
        return txtperiodFrom;
    }

    public void setTxtperiodFrom(String txtperiodFrom) {
        this.txtperiodFrom = txtperiodFrom;
    }

    public String getTxtperiodTo() {
        return txtperiodTo;
    }

    public void setTxtperiodTo(String txtperiodTo) {
        this.txtperiodTo = txtperiodTo;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }
        
    
}
