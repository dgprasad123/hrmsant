package hrms.model.notification;

public class EmployeePayRecordSt {

    private String payid;
    private String nottype;
    private String notid;
    private String empid;
    private String wef;
    private String weft;
    private String payscale;
    private String gradePay;
    private String pay;
    private String spay;
    private String ppay;
    private String othpay;
    private String othDesc;
    private boolean dataExist = false;

    public String getPayid() {
        return payid;
    }

    public void setPayid(String payid) {
        this.payid = payid;
    }

    public String getNottype() {
        return nottype;
    }

    public void setNottype(String nottype) {
        this.nottype = nottype;
    }

    public String getNotid() {
        return notid;
    }

    public void setNotid(String notid) {
        this.notid = notid;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getWef() {
        return wef;
    }

    public void setWef(String wef) {
        this.wef = wef;
    }

    public String getWeft() {
        return weft;
    }

    public void setWeft(String weft) {
        this.weft = weft;
    }

    public String getPayscale() {
        return payscale;
    }

    public void setPayscale(String payscale) {
        this.payscale = payscale;
    }

    public String getGradePay() {
        return gradePay;
    }

    public void setGradePay(String gradePay) {
        this.gradePay = gradePay;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getSpay() {
        return spay;
    }

    public void setSpay(String spay) {
        this.spay = spay;
    }

    public String getPpay() {
        return ppay;
    }

    public void setPpay(String ppay) {
        this.ppay = ppay;
    }

    public String getOthpay() {
        return othpay;
    }

    public void setOthpay(String othpay) {
        this.othpay = othpay;
    }

    public String getOthDesc() {
        return othDesc;
    }

    public void setOthDesc(String othDesc) {
        this.othDesc = othDesc;
    }

    public boolean isDataExist() {
        return dataExist;
    }

    public void setDataExist(boolean dataExist) {
        this.dataExist = dataExist;
    }
}
