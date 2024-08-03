package hrms.model.GPHeadUpdate;

public class GPHeadUpdateForm {
    
    private String txtBillNo;
    
    private String aqslno;
    private String aqyear;
    private String aqmonth;
    
    private String existingGPHead;
    private String newGPHead;
    
    private String billExists;

    public String getTxtBillNo() {
        return txtBillNo;
    }

    public void setTxtBillNo(String txtBillNo) {
        this.txtBillNo = txtBillNo;
    }

    public String getAqslno() {
        return aqslno;
    }

    public void setAqslno(String aqslno) {
        this.aqslno = aqslno;
    }

    public String getAqyear() {
        return aqyear;
    }

    public void setAqyear(String aqyear) {
        this.aqyear = aqyear;
    }

    public String getAqmonth() {
        return aqmonth;
    }

    public void setAqmonth(String aqmonth) {
        this.aqmonth = aqmonth;
    }

    public String getExistingGPHead() {
        return existingGPHead;
    }

    public void setExistingGPHead(String existingGPHead) {
        this.existingGPHead = existingGPHead;
    }

    public String getNewGPHead() {
        return newGPHead;
    }

    public void setNewGPHead(String newGPHead) {
        this.newGPHead = newGPHead;
    }

    public String getBillExists() {
        return billExists;
    }

    public void setBillExists(String billExists) {
        this.billExists = billExists;
    }
    
    
    
    
}
