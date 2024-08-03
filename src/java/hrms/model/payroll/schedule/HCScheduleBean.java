package hrms.model.payroll.schedule;


public class HCScheduleBean {
 
    private String empCode = null;
    private String empName = null;
    private String desg = null;
    private String basic = null;
    private String amt;
    private String idNumber = null;
    private String aqsl = null;

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getDesg() {
        return desg;
    }

    public void setDesg(String desg) {
        this.desg = desg;
    }

    public String getBasic() {
        return basic;
    }

    public void setBasic(String basic) {
        this.basic = basic;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getAqsl() {
        return aqsl;
    }

    public void setAqsl(String aqsl) {
        this.aqsl = aqsl;
    }
    
}
