package hrms.model.payroll.payslip;

public class ADDetails {

    private String adCode = null;
    private String adCodeDesc;
    private double adAmount = 0;
    private String adDesc = null;
    
    private int adAmt = 0;
    private int principalAmt = 0;
    private int interestAmt = 0;
    private int totRecAmt = 0;
    private String nowDedn;
    private String refDesc;
    private String accNumber;
    
    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public String getAdCodeDesc() {
        return adCodeDesc;
    }

    public void setAdCodeDesc(String adCodeDesc) {
        this.adCodeDesc = adCodeDesc;
    }

    public double getAdAmount() {
        return adAmount;
    }

    public void setAdAmount(double adAmount) {
        this.adAmount = adAmount;
    }

    public String getAdDesc() {
        return adDesc;
    }

    public void setAdDesc(String adDesc) {
        this.adDesc = adDesc;
    }

    public int getAdAmt() {
        return adAmt;
    }

    public void setAdAmt(int adAmt) {
        this.adAmt = adAmt;
    }

    public int getPrincipalAmt() {
        return principalAmt;
    }

    public void setPrincipalAmt(int principalAmt) {
        this.principalAmt = principalAmt;
    }

    public int getInterestAmt() {
        return interestAmt;
    }

    public void setInterestAmt(int interestAmt) {
        this.interestAmt = interestAmt;
    }

    public int getTotRecAmt() {
        return totRecAmt;
    }

    public void setTotRecAmt(int totRecAmt) {
        this.totRecAmt = totRecAmt;
    }

    public String getNowDedn() {
        return nowDedn;
    }

    public void setNowDedn(String nowDedn) {
        this.nowDedn = nowDedn;
    }

    public String getRefDesc() {
        return refDesc;
    }

    public void setRefDesc(String refDesc) {
        this.refDesc = refDesc;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }
}
