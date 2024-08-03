package hrms.model.GIS;

public class GISBean {
    
    private String gisid;
    private String instrumentNo;
    private String dateOfDeposit;
    private String voucherNo;
    private String treasuryName;
    private String amount;

    public String getInstrumentNo() {
        return instrumentNo;
    }

    public void setInstrumentNo(String instrumentNo) {
        this.instrumentNo = instrumentNo;
    }

    public String getDateOfDeposit() {
        return dateOfDeposit;
    }

    public void setDateOfDeposit(String dateOfDeposit) {
        this.dateOfDeposit = dateOfDeposit;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getTreasuryName() {
        return treasuryName;
    }

    public void setTreasuryName(String treasuryName) {
        this.treasuryName = treasuryName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getGisid() {
        return gisid;
    }

    public void setGisid(String gisid) {
        this.gisid = gisid;
    }
    
}
