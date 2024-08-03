package hrms.model.leave;

import java.util.ArrayList;
import org.apache.commons.lang.builder.ToStringBuilder;

public class CreditLeaveProperties {
    
    private String empId = null;
    private String lcrId = null;
    private String openbaldate = null;
    private String nextopenbaldate = null;
    private String fromDate = null;
    private String toDate = null;
    private String fnan = null;
    private String compMonths = null;
    private String leaveCredited = null;
    private Double creleave;
    private String totEOLNumber = null;
    private String leaveDeduct = null;
    private String creditBal = null;
    private String creditBalShow = null;/*Added to show the cumulative balance leave*/

    private String creditType = null;
    private ArrayList<EmpLeaveAvailedPropeties> availedLeave = new ArrayList();
    private ArrayList<EmpLeaveSurrenderedPropeties> surrenderedLeave = new ArrayList();
    private String ifaddEL = null;    
    private String typeoflv = null;
    private String tolId;

    public String getTolId() {
        return tolId;
    }

    public void setTolId(String tolId) {
        this.tolId = tolId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getLcrId() {
        return lcrId;
    }

    public void setLcrId(String lcrId) {
        this.lcrId = lcrId;
    }
    
    public String getOpenbaldate() {
        return openbaldate;
    }

    public void setOpenbaldate(String openbaldate) {
        this.openbaldate = openbaldate;
    }

    public String getNextopenbaldate() {
        return nextopenbaldate;
    }

    public void setNextopenbaldate(String nextopenbaldate) {
        this.nextopenbaldate = nextopenbaldate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getFnan() {
        return fnan;
    }

    public void setFnan(String fnan) {
        this.fnan = fnan;
    }

    public String getCompMonths() {
        return compMonths;
    }

    public void setCompMonths(String compMonths) {
        this.compMonths = compMonths;
    }

    public String getLeaveCredited() {
        return leaveCredited;
    }

    public void setLeaveCredited(String leaveCredited) {
        this.leaveCredited = leaveCredited;
    }

    public Double getCreleave() {
        return creleave;
    }

    public void setCreleave(Double creleave) {
        this.creleave = creleave;
    }

    public String getTotEOLNumber() {
        return totEOLNumber;
    }

    public void setTotEOLNumber(String totEOLNumber) {
        this.totEOLNumber = totEOLNumber;
    }

    public String getLeaveDeduct() {
        return leaveDeduct;
    }

    public void setLeaveDeduct(String leaveDeduct) {
        this.leaveDeduct = leaveDeduct;
    }

    public String getCreditBal() {
        return creditBal;
    }

    public void setCreditBal(String creditBal) {
        this.creditBal = creditBal;
    }

    public String getCreditBalShow() {
        return creditBalShow;
    }

    public void setCreditBalShow(String creditBalShow) {
        this.creditBalShow = creditBalShow;
    }

    public String getCreditType() {
        return creditType;
    }

    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }

    public ArrayList getAvailedLeave() {
        return availedLeave;
    }

    public void setAvailedLeave(ArrayList availedLeave) {
        this.availedLeave = availedLeave;
    }

    public ArrayList getSurrenderedLeave() {
        return surrenderedLeave;
    }

    public void setSurrenderedLeave(ArrayList surrenderedLeave) {
        this.surrenderedLeave = surrenderedLeave;
    }

    public String getIfaddEL() {
        return ifaddEL;
    }

    public void setIfaddEL(String ifaddEL) {
        this.ifaddEL = ifaddEL;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }    

    public String getTypeoflv() {
        return typeoflv;
    }

    public void setTypeoflv(String typeoflv) {
        this.typeoflv = typeoflv;
    }

}
