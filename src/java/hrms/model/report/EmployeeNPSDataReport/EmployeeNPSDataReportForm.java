package hrms.model.report.EmployeeNPSDataReport;

import java.util.ArrayList;

public class EmployeeNPSDataReportForm {
    
    private String txtpranno;
    private String sltYear;
    private String sltMonth;
    
    private String empid;
    private String empname;
    private String designation;
    private String dob;
    private String dos;
    
    private ArrayList regularbillEmpdatalist;
    private ArrayList arrearbillEmpdatalist;
    
    private String txthrmsid;
    private String sltPaySlipYear;
    private String sltPaySlipMonth;
    
    public String getTxtpranno() {
        return txtpranno;
    }

    public void setTxtpranno(String txtpranno) {
        this.txtpranno = txtpranno;
    }

    public String getSltYear() {
        return sltYear;
    }

    public void setSltYear(String sltYear) {
        this.sltYear = sltYear;
    }

    public String getSltMonth() {
        return sltMonth;
    }

    public void setSltMonth(String sltMonth) {
        this.sltMonth = sltMonth;
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDos() {
        return dos;
    }

    public void setDos(String dos) {
        this.dos = dos;
    }    

    public ArrayList getRegularbillEmpdatalist() {
        return regularbillEmpdatalist;
    }

    public void setRegularbillEmpdatalist(ArrayList regularbillEmpdatalist) {
        this.regularbillEmpdatalist = regularbillEmpdatalist;
    }

    public ArrayList getArrearbillEmpdatalist() {
        return arrearbillEmpdatalist;
    }

    public void setArrearbillEmpdatalist(ArrayList arrearbillEmpdatalist) {
        this.arrearbillEmpdatalist = arrearbillEmpdatalist;
    }

    public String getTxthrmsid() {
        return txthrmsid;
    }

    public void setTxthrmsid(String txthrmsid) {
        this.txthrmsid = txthrmsid;
    }

    public String getSltPaySlipYear() {
        return sltPaySlipYear;
    }

    public void setSltPaySlipYear(String sltPaySlipYear) {
        this.sltPaySlipYear = sltPaySlipYear;
    }

    public String getSltPaySlipMonth() {
        return sltPaySlipMonth;
    }

    public void setSltPaySlipMonth(String sltPaySlipMonth) {
        this.sltPaySlipMonth = sltPaySlipMonth;
    }
}
