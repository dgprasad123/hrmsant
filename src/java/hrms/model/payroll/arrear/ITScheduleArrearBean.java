package hrms.model.payroll.arrear;

import java.util.List;

public class ITScheduleArrearBean {
    
    private int slno;
    private String empname;
    private String empdesg;
    private String empBasicSal;
    private String empPanNo;
    
    private int empDedutAmount;
    private int carryForward;
    
    private String pagebreakIT;
    private String pageHeaderIT;

    public int getSlno() {
        return slno;
    }

    public void setSlno(int slno) {
        this.slno = slno;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getEmpdesg() {
        return empdesg;
    }

    public void setEmpdesg(String empdesg) {
        this.empdesg = empdesg;
    }

    public String getEmpBasicSal() {
        return empBasicSal;
    }

    public void setEmpBasicSal(String empBasicSal) {
        this.empBasicSal = empBasicSal;
    }

    public String getEmpPanNo() {
        return empPanNo;
    }

    public void setEmpPanNo(String empPanNo) {
        this.empPanNo = empPanNo;
    }

    public String getPagebreakIT() {
        return pagebreakIT;
    }

    public void setPagebreakIT(String pagebreakIT) {
        this.pagebreakIT = pagebreakIT;
    }

    public String getPageHeaderIT() {
        return pageHeaderIT;
    }

    public void setPageHeaderIT(String pageHeaderIT) {
        this.pageHeaderIT = pageHeaderIT;
    }

    public int getEmpDedutAmount() {
        return empDedutAmount;
    }

    public void setEmpDedutAmount(int empDedutAmount) {
        this.empDedutAmount = empDedutAmount;
    }

    public int getCarryForward() {
        return carryForward;
    }

    public void setCarryForward(int carryForward) {
        this.carryForward = carryForward;
    }
}
