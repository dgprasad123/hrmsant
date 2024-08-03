package hrms.model.payroll.schedule;

public class VehicleScheduleBean {
    
    private int totMCAPri;
    private int totMCAInt;
    
    private int totMopaPri;
    private int totMopaInt;
    
    private int totVEPri;
    private int totVEInt;
    
    private String adName = null;
    private String adCode = null;
    private String nowDedt = null;
    private int adAmt = 0;
    

    public int getTotMCAPri() {
        return totMCAPri;
    }

    public void setTotMCAPri(int totMCAPri) {
        this.totMCAPri = totMCAPri;
    }

    public int getTotMCAInt() {
        return totMCAInt;
    }

    public void setTotMCAInt(int totMCAInt) {
        this.totMCAInt = totMCAInt;
    }

    public int getTotMopaPri() {
        return totMopaPri;
    }

    public void setTotMopaPri(int totMopaPri) {
        this.totMopaPri = totMopaPri;
    }

    public int getTotMopaInt() {
        return totMopaInt;
    }

    public void setTotMopaInt(int totMopaInt) {
        this.totMopaInt = totMopaInt;
    }

    public int getTotVEPri() {
        return totVEPri;
    }

    public void setTotVEPri(int totVEPri) {
        this.totVEPri = totVEPri;
    }

    public int getTotVEInt() {
        return totVEInt;
    }

    public void setTotVEInt(int totVEInt) {
        this.totVEInt = totVEInt;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public String getNowDedt() {
        return nowDedt;
    }

    public void setNowDedt(String nowDedt) {
        this.nowDedt = nowDedt;
    }

    public int getAdAmt() {
        return adAmt;
    }

    public void setAdAmt(int adAmt) {
        this.adAmt = adAmt;
    }
    
    
    
}
