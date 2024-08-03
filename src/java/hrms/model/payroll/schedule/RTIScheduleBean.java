/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.model.payroll.schedule;

/**
 *
 * @author prashant
 */
public class RTIScheduleBean {
    
    private String slNo = null;
    private String empName = null;
    private String fineImposed = null;
    private String noofInstalment = null;
    private String amtRecvd = null;
    private String totAmtRecvd = null;
    private String balRec = null;
    private String remarks = null;

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

    public String getFineImposed() {
        return fineImposed;
    }

    public void setFineImposed(String fineImposed) {
        this.fineImposed = fineImposed;
    }

    public String getNoofInstalment() {
        return noofInstalment;
    }

    public void setNoofInstalment(String noofInstalment) {
        this.noofInstalment = noofInstalment;
    }

    public String getAmtRecvd() {
        return amtRecvd;
    }

    public void setAmtRecvd(String amtRecvd) {
        this.amtRecvd = amtRecvd;
    }

    public String getTotAmtRecvd() {
        return totAmtRecvd;
    }

    public void setTotAmtRecvd(String totAmtRecvd) {
        this.totAmtRecvd = totAmtRecvd;
    }

    public String getBalRec() {
        return balRec;
    }

    public void setBalRec(String balRec) {
        this.balRec = balRec;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    
}
