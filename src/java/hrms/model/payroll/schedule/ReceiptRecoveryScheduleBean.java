package hrms.model.payroll.schedule;

public class ReceiptRecoveryScheduleBean {
    
    private String employeeName;
    private String employeeDesignation;
    private String amtdeducted;

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getAmtdeducted() {
        return amtdeducted;
    }

    public void setAmtdeducted(String amtdeducted) {
        this.amtdeducted = amtdeducted;
    }

    public String getEmployeeDesignation() {
        return employeeDesignation;
    }

    public void setEmployeeDesignation(String employeeDesignation) {
        this.employeeDesignation = employeeDesignation;
    }    
}
