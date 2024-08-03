package hrms.model.payroll.officewisesecondschedulelist;

public class OfficeWiseSecondScheduleForm {
    
    private String empid = null;
    private String empname = null;
    private String offcode = null;
    private String designation = null;
    private String payscale = null;
    private int gp = 0;
    private String rdOptionChosen = null;
    private String txtDateEntered = null;
    
    private String hidPostCode = null;
    
    private String hidPayScale = null;
    private int hidGP;
    
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

    

    public String getPayscale() {
        return payscale;
    }

    public void setPayscale(String payscale) {
        this.payscale = payscale;
    }

    public int getGp() {
        return gp;
    }

    public void setGp(int gp) {
        this.gp = gp;
    }

    public String getRdOptionChosen() {
        return rdOptionChosen;
    }

    public void setRdOptionChosen(String rdOptionChosen) {
        this.rdOptionChosen = rdOptionChosen;
    }

    public String getTxtDateEntered() {
        return txtDateEntered;
    }

    public void setTxtDateEntered(String txtDateEntered) {
        this.txtDateEntered = txtDateEntered;
    }

    public String getOffcode() {
        return offcode;
    }

    public void setOffcode(String offcode) {
        this.offcode = offcode;
    }

    public String getHidPostCode() {
        return hidPostCode;
    }

    public void setHidPostCode(String hidPostCode) {
        this.hidPostCode = hidPostCode;
    }

    public String getHidPayScale() {
        return hidPayScale;
    }

    public void setHidPayScale(String hidPayScale) {
        this.hidPayScale = hidPayScale;
    }

    public int getHidGP() {
        return hidGP;
    }

    public void setHidGP(int hidGP) {
        this.hidGP = hidGP;
    }
}
