package hrms.model.parmast;

import java.util.Date;

public class ParMasterWS {
    private int parid;
    private String fiscalYear;
    private Date periodfrom = null;
    private Date periodto = null;
    private int parstatus;
    private String spn = null;
    private String isClosed = null;
    private String authRemarksClosed = null;
    private String reportingRemarksClosed = null;

    public int getParid() {
        return parid;
    }

    public void setParid(int parid) {
        this.parid = parid;
    }

    public String getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(String fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public Date getPeriodfrom() {
        return periodfrom;
    }

    public void setPeriodfrom(Date periodfrom) {
        this.periodfrom = periodfrom;
    }

    public Date getPeriodto() {
        return periodto;
    }

    public void setPeriodto(Date periodto) {
        this.periodto = periodto;
    }

    public int getParstatus() {
        return parstatus;
    }

    public void setParstatus(int parstatus) {
        this.parstatus = parstatus;
    }

    public String getSpn() {
        return spn;
    }

    public void setSpn(String spn) {
        this.spn = spn;
    }

    public String getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(String isClosed) {
        this.isClosed = isClosed;
    }

    public String getAuthRemarksClosed() {
        return authRemarksClosed;
    }

    public void setAuthRemarksClosed(String authRemarksClosed) {
        this.authRemarksClosed = authRemarksClosed;
    }

    public String getReportingRemarksClosed() {
        return reportingRemarksClosed;
    }

    public void setReportingRemarksClosed(String reportingRemarksClosed) {
        this.reportingRemarksClosed = reportingRemarksClosed;
    }
    
    
}
