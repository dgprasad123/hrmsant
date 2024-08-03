package hrms.model.report.EmployeeNPSDataReport;

public class EmployeeNPSDataReportBean {
    
    private String billno;
    private String billdesc;
    private String billyear;
    private String billmonth;
    
    private String empcontribution;
    private String governmentcontribution;

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }

    public String getBilldesc() {
        return billdesc;
    }

    public void setBilldesc(String billdesc) {
        this.billdesc = billdesc;
    }

    public String getBillyear() {
        return billyear;
    }

    public void setBillyear(String billyear) {
        this.billyear = billyear;
    }

    public String getBillmonth() {
        return billmonth;
    }

    public void setBillmonth(String billmonth) {
        this.billmonth = billmonth;
    }

    public String getEmpcontribution() {
        return empcontribution;
    }

    public void setEmpcontribution(String empcontribution) {
        this.empcontribution = empcontribution;
    }

    public String getGovernmentcontribution() {
        return governmentcontribution;
    }

    public void setGovernmentcontribution(String governmentcontribution) {
        this.governmentcontribution = governmentcontribution;
    }
}
