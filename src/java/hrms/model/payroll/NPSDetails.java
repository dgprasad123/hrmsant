package hrms.model.payroll;

import java.util.Date;

public class NPSDetails {
    private int hrmsgeneratedRefno=0;
    private String hrmsgeneratedRefdate=null;
    private String ddoRegno=null;
    private String pran=null;
    private String nameofSubscrib=null;
    private double basic=0;
    private double gp=0;
    private double da=0;
    private double ppay=0;
    private String sc=null;
    private String gc=null;
    private double instAmt=0;
    private String paymonth=null;
    private String payYear=null;
    private String contType=null;
    private String remarks=null;
    private String btserialno = null;
    
    private String ifGPFAssumed;
    private String ifStopPayNps;
    private String userType;
    private Date dosDate;
    private String npsEmpid=null;
    
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

    public void setDdoRegno(String ddoRegno) {        
        this.ddoRegno = ddoRegno;
    }

    public String getDdoRegno() {
        if(ddoRegno == null){
            ddoRegno = "";
        }
        return ddoRegno;
    }

    public void setPran(String pran) {
        this.pran = pran;
    }

    public String getPran() {
        if(pran == null){
            pran = "";
        }
        return pran;
    }

    public void setNameofSubscrib(String nameofSubscrib) {
        this.nameofSubscrib = nameofSubscrib;
    }

    public String getNameofSubscrib() {
        if(nameofSubscrib == null){
            nameofSubscrib = "";
        }
        return nameofSubscrib;
    }

    
    public void setDa(double da) {
        this.da = da;
    }

    public double getDa() {
        return da;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public String getSc() {
        if(sc == null){
            sc = "";
        }
        return sc;
    }

    public void setGc(String gc) {
        this.gc = gc;
    }

    public String getGc() {
        if(gc == null){
            gc = "";
        }
        return gc;
    }

    public void setInstAmt(double instAmt) {
        this.instAmt = instAmt;
    }

    public double getInstAmt() {
        return instAmt;
    }

    public void setPaymonth(String paymonth) {
        this.paymonth = paymonth;
    }

    public String getPaymonth() {
        if(paymonth == null){
            paymonth = "";
        }
        return paymonth;
    }

    public void setPayYear(String payYear) {
        this.payYear = payYear;
    }

    public String getPayYear() {
        if(payYear == null){
            payYear = "";
        }
        return payYear;
    }

    public void setContType(String contType) {
        this.contType = contType;
    }

    public String getContType() {
        if(contType == null){
            contType = "";
        }
        return contType;
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

    

    public void setBasic(double basic) {
        this.basic = basic;
    }

    public double getBasic() {
        return basic;
    }

    public void setGp(double gp) {
        this.gp = gp;
    }

    public double getGp() {
        return gp;
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

    public void setPpay(double ppay) {
        this.ppay = ppay;
    }

    public double getPpay() {
        return ppay;
    }

    public String getIfGPFAssumed() {
        return ifGPFAssumed;
    }

    public void setIfGPFAssumed(String ifGPFAssumed) {
        this.ifGPFAssumed = ifGPFAssumed;
    }

    public String getIfStopPayNps() {
        return ifStopPayNps;
    }

    public void setIfStopPayNps(String ifStopPayNps) {
        this.ifStopPayNps = ifStopPayNps;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Date getDosDate() {
        return dosDate;
    }

    public void setDosDate(Date dosDate) {
        this.dosDate = dosDate;
    }

    public String getNpsEmpid() {
        return npsEmpid;
    }

    public void setNpsEmpid(String npsEmpid) {
        this.npsEmpid = npsEmpid;
    }
    
    
}
