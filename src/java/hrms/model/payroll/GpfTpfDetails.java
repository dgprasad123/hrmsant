package hrms.model.payroll;

public class GpfTpfDetails {
    private String hrmsgeneratedRefno=null;
    private String hrmsgeneratedRefdate=null;    
    private String btserialno=null;
    private String gpfSeries=null;
    private String gpfnumber=null;
    private String subscribName=null;
    private String desig=null;
    private String dob=null;
    private String dos=null;
    private String monthlySubscrip=null;
    private String periodFrom=null;
    private String periodTo=null;
    private String refundWithdrawl=null;
    private String instNumber=null;
    private double otherDeposit=0;
    private double totRealised=0;
    private String remarks=null;

    public void setHrmsgeneratedRefno(String hrmsgeneratedRefno) {
        this.hrmsgeneratedRefno = hrmsgeneratedRefno;
    }

    public String getHrmsgeneratedRefno() {
        if(hrmsgeneratedRefno == null){
            hrmsgeneratedRefno = "";
        }
        return hrmsgeneratedRefno;
    }

    public void setHrmsgeneratedRefdate(String hrmsgeneratedRefdate) {
        this.hrmsgeneratedRefdate = hrmsgeneratedRefdate;
    }

    public String getHrmsgeneratedRefdate() {
        if(hrmsgeneratedRefdate == null){
            hrmsgeneratedRefdate = "";
        }
        return hrmsgeneratedRefdate;
    }    

    public void setGpfSeries(String gpfSeries) {
        this.gpfSeries = gpfSeries;
    }

    public String getGpfSeries() {
        if(gpfSeries == null){
            gpfSeries = "";
        }
        return gpfSeries;
    }

    public void setGpfnumber(String gpfnumber) {
        this.gpfnumber = gpfnumber;
    }

    public String getGpfnumber() {
        if(gpfnumber == null){
            gpfnumber = "";
        }
        return gpfnumber;
    }

    public void setSubscribName(String subscribName) {
        this.subscribName = subscribName;
    }

    public String getSubscribName() {
        if(subscribName == null){
            subscribName = "";
        }
        return subscribName;
    }

    public void setDesig(String desig) {
        this.desig = desig;
    }

    public String getDesig() {
        if(desig == null){
            desig = "";
        }
        return desig;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDob() {
        if(dob == null){
            dob = "";
        }
        return dob;
    }

    public void setDos(String dos) {
        this.dos = dos;
    }

    public String getDos() {
        if(dos == null){
            dos = "";
        }
        return dos;
    }

    public void setMonthlySubscrip(String monthlySubscrip) {
        this.monthlySubscrip = monthlySubscrip;
    }

    public String getMonthlySubscrip() {
        if(monthlySubscrip == null){
            monthlySubscrip = "0";
        }
        return monthlySubscrip;
    }

    public void setPeriodFrom(String periodFrom) {
        this.periodFrom = periodFrom;
    }

    public String getPeriodFrom() {
        if(periodFrom == null){
            periodFrom = "";
        }
        return periodFrom;
    }

    public void setPeriodTo(String periodTo) {
        this.periodTo = periodTo;
    }

    public String getPeriodTo() {
        if(periodTo == null){
            periodTo = "";
        }
        return periodTo;
    }

    public void setRefundWithdrawl(String refundWithdrawl) {
        this.refundWithdrawl = refundWithdrawl;
    }

    public String getRefundWithdrawl() {
        if(refundWithdrawl == null){
            refundWithdrawl = "0";
        }
        return refundWithdrawl;
    }

    public void setInstNumber(String instNumber) {
        this.instNumber = instNumber;
    }

    public String getInstNumber() {
        if(instNumber == null){
            instNumber = "";
        }
        return instNumber;
    }

    public void setOtherDeposit(double otherDeposit) {
        this.otherDeposit = otherDeposit;
    }

    public double getOtherDeposit() {        
        return otherDeposit;
    }

    public void setTotRealised(double totRealised) {
        this.totRealised = totRealised;
    }

    public double getTotRealised() {
        return totRealised;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        if(remarks == null){
            remarks = "";            
        }        
        return remarks;
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
}
