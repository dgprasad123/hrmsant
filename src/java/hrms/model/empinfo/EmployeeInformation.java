package hrms.model.empinfo;

public class EmployeeInformation {
    
    private String empid = null;
    private String gpfno = null;
    private String empname = null;
    private String empdesg = null;
    private String mobile = null;
    private String cadreCode = null;
    private String cadreName = null;
    
    private String paydate = null;
    private String payscale = null;
    private Double basic = 0.0;
    private Double gp = 0.0;
    private int spay = 0;
    private int ppay = 0;
    private int othpay = 0;
    
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

    public String getEmpdesg() {
        return empdesg;
    }

    public void setEmpdesg(String empdesg) {
        this.empdesg = empdesg;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGpfno() {
        return gpfno;
    }

    public void setGpfno(String gpfno) {
        this.gpfno = gpfno;
    }

    public String getCadreCode() {
        return cadreCode;
    }

    public void setCadreCode(String cadreCode) {
        this.cadreCode = cadreCode;
    }

    public String getCadreName() {
        return cadreName;
    }

    public void setCadreName(String cadreName) {
        this.cadreName = cadreName;
    }

    public String getPaydate() {
        return paydate;
    }

    public void setPaydate(String paydate) {
        this.paydate = paydate;
    }

    public String getPayscale() {
        return payscale;
    }

    public void setPayscale(String payscale) {
        this.payscale = payscale;
    }

    public Double getBasic() {
        return basic;
    }

    public void setBasic(Double basic) {
        this.basic = basic;
    }

    public Double getGp() {
        return gp;
    }

    public void setGp(Double gp) {
        this.gp = gp;
    }

    public int getSpay() {
        return spay;
    }

    public void setSpay(int spay) {
        this.spay = spay;
    }

    public int getPpay() {
        return ppay;
    }

    public void setPpay(int ppay) {
        this.ppay = ppay;
    }

    public int getOthpay() {
        return othpay;
    }

    public void setOthpay(int othpay) {
        this.othpay = othpay;
    }

    

 
}
