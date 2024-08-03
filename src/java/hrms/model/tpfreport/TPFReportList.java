/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.tpfreport;

public class TPFReportList {
    
    private int slno = 0;
    private String empName = null;
    private String empDesg = null;
    private String basic = null;
    private String gpfseries = null;
    private String gpfNo = null;
    private String ordNo = null;
    private String ordDt = null;
    private int amount = 0;

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

    public String getBasic() {
        return basic;
    }

    public void setBasic(String basic) {
        this.basic = basic;
    }

    public String getGpfseries() {
        return gpfseries;
    }

    public void setGpfseries(String gpfseries) {
        this.gpfseries = gpfseries;
    }

    public String getOrdDt() {
        return ordDt;
    }

    public void setOrdDt(String ordDt) {
        this.ordDt = ordDt;
    }

    public int getSlno() {
        return slno;
    }

    public void setSlno(int slno) {
        this.slno = slno;
    }

    public String getGpfNo() {
        return gpfNo;
    }

    public void setGpfNo(String gpfNo) {
        this.gpfNo = gpfNo;
    }

    public String getOrdNo() {
        return ordNo;
    }

    public void setOrdNo(String ordNo) {
        this.ordNo = ordNo;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
