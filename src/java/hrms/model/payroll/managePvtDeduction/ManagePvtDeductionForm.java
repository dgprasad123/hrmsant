package hrms.model.payroll.managePvtDeduction;

public class ManagePvtDeductionForm {
    
    private int ddoAmount;
    private int billNo;
    
    private String sltDistrict;
    private String sltDDO;
    private String sltBank;
    private String sltBranch;
    private String bankAccNo;
    private String amount;
    
    private String pvtdednid;
    
    public int getDdoAmount() {
        return ddoAmount;
    }

    public void setDdoAmount(int ddoAmount) {
        this.ddoAmount = ddoAmount;
    }

    public String getSltDistrict() {
        return sltDistrict;
    }

    public void setSltDistrict(String sltDistrict) {
        this.sltDistrict = sltDistrict;
    }

    public String getSltDDO() {
        return sltDDO;
    }

    public void setSltDDO(String sltDDO) {
        this.sltDDO = sltDDO;
    }

    public String getSltBank() {
        return sltBank;
    }

    public void setSltBank(String sltBank) {
        this.sltBank = sltBank;
    }

    public String getSltBranch() {
        return sltBranch;
    }

    public void setSltBranch(String sltBranch) {
        this.sltBranch = sltBranch;
    }

    public String getBankAccNo() {
        return bankAccNo;
    }

    public void setBankAccNo(String bankAccNo) {
        this.bankAccNo = bankAccNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getBillNo() {
        return billNo;
    }

    public void setBillNo(int billNo) {
        this.billNo = billNo;
    }

    public String getPvtdednid() {
        return pvtdednid;
    }

    public void setPvtdednid(String pvtdednid) {
        this.pvtdednid = pvtdednid;
    }
    
}
