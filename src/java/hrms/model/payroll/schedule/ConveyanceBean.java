/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.payroll.schedule;

/**
 *
 * @author PKM
 */
public class ConveyanceBean {
    
    private String slNo = null;
    private String empName = null;
    private String empDesg = null;
    private double amtDed = 0;

    public String getSlNo() {
        return slNo;
    }

    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpDesg() {
        return empDesg;
    }

    public void setEmpDesg(String empDesg) {
        this.empDesg = empDesg;
    }

    public double getAmtDed() {
        return amtDed;
    }

    public void setAmtDed(double amtDed) {
        this.amtDed = amtDed;
    }

    

    
}
