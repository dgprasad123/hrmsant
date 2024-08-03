package hrms.model.payroll;

public class BytransferDetails {
    private int hrmsgeneratedRefno=0;
    private String hrmsgeneratedRefdate=null;
    private String scheduleName="";
    private String bytransferType=null;
    private String btserialno = null;
    private double amount=0;
    private String treasuryCode=null;

    public int getHrmsgeneratedRefno() {
        return hrmsgeneratedRefno;
    }

    public void setHrmsgeneratedRefno(int hrmsgeneratedRefno) {
        this.hrmsgeneratedRefno = hrmsgeneratedRefno;
    }
    
    

    public void setHrmsgeneratedRefdate(String hrmsgeneratedRefdate) {
        this.hrmsgeneratedRefdate = hrmsgeneratedRefdate;
    }

    public String getHrmsgeneratedRefdate() {
        return hrmsgeneratedRefdate;
    }

    

    public void setBytransferType(String bytransferType) {
        this.bytransferType = bytransferType;
    }

    public String getBytransferType() {
        if(bytransferType == null){
            bytransferType = "";
        }
        return bytransferType;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setTreasuryCode(String treasuryCode) {
        this.treasuryCode = treasuryCode;
    }

    public String getTreasuryCode() {
        if(treasuryCode == null){
            treasuryCode = "";
        }
        return treasuryCode;
    }

    public void setBtserialno(String btserialno) {
        this.btserialno = btserialno;
    }

    public String getBtserialno() {
        if(btserialno == null){
            btserialno = "";
        }
        return btserialno;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }
}
