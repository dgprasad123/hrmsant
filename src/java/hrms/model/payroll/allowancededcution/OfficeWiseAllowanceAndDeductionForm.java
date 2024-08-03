package hrms.model.payroll.allowancededcution;

public class OfficeWiseAllowanceAndDeductionForm {
    
    private String sltBillGroup;
    private String sltDeductionName;
    
    private String chkEmployee;
    private String adAmt;
    
    public String getSltBillGroup() {
        return sltBillGroup;
    }

    public void setSltBillGroup(String sltBillGroup) {
        this.sltBillGroup = sltBillGroup;
    }

    public String getSltDeductionName() {
        return sltDeductionName;
    }

    public void setSltDeductionName(String sltDeductionName) {
        this.sltDeductionName = sltDeductionName;
    }

    public String getChkEmployee() {
        return chkEmployee;
    }

    public void setChkEmployee(String chkEmployee) {
        this.chkEmployee = chkEmployee;
    }

    public String getAdAmt() {
        return adAmt;
    }

    public void setAdAmt(String adAmt) {
        this.adAmt = adAmt;
    }
}
