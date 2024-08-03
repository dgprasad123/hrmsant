package hrms.model.payroll.schedule;

public class MonthlyUtilizationReportBean {

    private int slno;
    private String empid = null;
    private String empname = null;
    private String desg = null;
    private String bankacc = null;
    private int daysWorked;
    private int daysAbsent;
    private int totalpay;

    public int getSlno() {
        return slno;
    }

    public void setSlno(int slno) {
        this.slno = slno;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getDesg() {
        return desg;
    }

    public void setDesg(String desg) {
        this.desg = desg;
    }

    public String getBankacc() {
        return bankacc;
    }

    public void setBankacc(String bankacc) {
        this.bankacc = bankacc;
    }

    public int getDaysWorked() {
        return daysWorked;
    }

    public void setDaysWorked(int daysWorked) {
        this.daysWorked = daysWorked;
    }

    public int getDaysAbsent() {
        return daysAbsent;
    }

    public void setDaysAbsent(int daysAbsent) {
        this.daysAbsent = daysAbsent;
    }

    public int getTotalpay() {
        return totalpay;
    }

    public void setTotalpay(int totalpay) {
        this.totalpay = totalpay;
    }

    
}
