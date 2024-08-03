package hrms.model.payroll.managePvtDeduction;

public class ManagePvtDeductionBean {
    
    private int pvtdednId;
    private String ddoName;
    private String billName;
    private String month;
    private String amount;
    
    private String isDdo;
    
    public String getDdoName() {
        return ddoName;
    }

    public void setDdoName(String ddoName) {
        this.ddoName = ddoName;
    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getPvtdednId() {
        return pvtdednId;
    }

    public void setPvtdednId(int pvtdednId) {
        this.pvtdednId = pvtdednId;
    }

    public String getIsDdo() {
        return isDdo;
    }

    public void setIsDdo(String isDdo) {
        this.isDdo = isDdo;
    }
    
}
